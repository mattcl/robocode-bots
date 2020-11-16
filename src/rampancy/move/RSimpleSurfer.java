package rampancy.move;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;

import rampancy.RampantRobot;
import rampancy.SegmentFns;
import rampancy.util.REnemy;
import rampancy.util.RPoint;
import rampancy.util.RState;
import rampancy.util.RUtil;
import rampancy.util.RWave;
import rampancy.util.RWaveManager;
import rampancy.util.data.GuessFactorArray;
import rampancy.util.data.SegmentFn;
import rampancy.util.data.SegmentTree;

import rampancy.extern.MoveSim;

import robocode.Bullet;
import robocode.HitByBulletEvent;
import robocode.util.Utils;

public class RSimpleSurfer extends RMovement {
    protected RWaveManager waveManager;
    protected SegmentTree segmentTree;
    protected ArrayList<RMoveChoice> currentExaminedMoves;
    protected RMoveChoice lastChoice;

    public RSimpleSurfer(RampantRobot referenceBot) {
        updateReferenceBot(referenceBot);
        ArrayList<SegmentFn> segmentFns = new ArrayList<SegmentFn>();
        segmentFns.add(SegmentFns.DISTANCE);
        segmentFns.add(SegmentFns.DELTA_H);
        segmentFns.add(SegmentFns.ACCELERATING);
        segmentFns.add(SegmentFns.WALL_DISTANCE);
        this.segmentTree = new SegmentTree(segmentFns, 10);
    }

    public void updateReferenceBot(RampantRobot referenceBot) {
        super.updateReferenceBot(referenceBot);
        this.waveManager = new RWaveManager(referenceBot);
        this.currentExaminedMoves = new ArrayList<RMoveChoice>();
        this.lastChoice = null;
    }

    public void update(REnemy enemy) {
        if (this.waveManager.maybeAddForEnemy(enemy)) {
        	return;
        }
        
        // FIXME: get rid of magic value
        if (this.referenceBot.location().distance(enemy.location()) < 200 && !this.waveManager.hasDummyWaves()) {
        	RState state = enemy.currentState();
        	RWave wave = new RWave(
        		state.location.clone(),
        		state.time - 2,
        		2.0,
        		state,
        		this.referenceBot.currentState()
			).asDummy();
        	this.waveManager.add(wave);
        }
    }
    
    public double getFactor(RWave wave, RPoint location) {
        double offsetAngle = (wave.origin.absoluteBearingTo(location) - wave.targetState.absoluteBearing);
        double maxEscape = wave.maxEscapeClockwise;
        // how do we know if we should have been counter-clockwise?
        if (wave.targetState.directionTraveling < 0) {
        	maxEscape = wave.maxEscapeCounterClockwise;
        }
        
        return Utils.normalRelativeAngle(offsetAngle) / maxEscape * wave.targetState.directionTraveling;
    }

    public void onHitByBullet(HitByBulletEvent e) {
        Bullet bullet = e.getBullet();
        RPoint location = new RPoint(bullet.getX(), bullet.getY());
        RWave wave = waveManager.getWaveForBullet(bullet);
        if (wave == null) {
            System.out.println("Could not find wave for bullet hit!");
            return;
        }
        wave.broken = true;
        this.markVisit(location, wave, 1.0);
    }
    
    public void markVisit(RPoint location, RWave wave, double weight) {
        double factor = this.getFactor(wave, location);
        SegmentTree.Node segment = segmentTree.getSegment(wave.targetState);
        segment.stats.update(factor, weight);
        if (weight == 1.0) {
			segment.markVisit();
        }
    }

    public void execute() {
        ArrayList<RWave> brokenWaves = this.waveManager.update(this.referenceBot.getTime());
        for (RWave wave : brokenWaves) {
        	this.markVisit(this.referenceBot.location(), wave, 0.5);
        }
        
        
        RWave wave = this.waveManager.getClosestWave(this.referenceBot.location());
        RMoveChoice best = null;
        if (wave != null) {
			best = wave.getMoveChoice();
			
			if (best == null) {
				GuessFactorArray segment = segmentTree.getSegment(wave.targetState).stats;
				
				long curTime = this.referenceBot.getTime();
				RState rootState = this.referenceBot.currentState();
				
				// simulate move clockwise
				ArrayList<RMoveChoice> choices = this.simulateDirection(curTime, rootState, wave, segment, 1);
				
				// simulate move counter-clockwise
				ArrayList<RMoveChoice> counterCW = this.simulateDirection(curTime, rootState, wave, segment, -1);
				
				choices.addAll(counterCW);
				// simulate stopping
				double danger = segment.probabilityAt(this.getFactor(wave, this.referenceBot.location()));
				choices.add(new RMoveChoice(this.referenceBot.location(), danger, 0));
				
				Collections.shuffle(choices);
				
				double min = 2;
				double maxDanger = -1.0;
				for (RMoveChoice choice: choices) {
					double approx = Math.round(choice.danger * 1000.0) / 1000.0;
					if (approx > maxDanger) {
						maxDanger = approx;
					}
					if (approx < min) {
						best = choice;
						min = choice.danger;
					}
				}
				
				for (RMoveChoice choice : choices) {
					Color dangerColor = Color.getHSBColor((float)RUtil.normalize(choice.danger, min, maxDanger) * 0.8f + 0.33f, 1.0f, 1.0f);
					choice.setColor(dangerColor);
				}
				
				
				this.currentExaminedMoves = choices;
				
				if (best != null) {
					wave.moveChoice = best;
				}
			}
		} else {
			best = this.lastChoice;
		}
        
        if (best == null || !best.hasWave()) {
        	return;
        }
        
		if ((best.hasETA() && best.reachedTime(this.referenceBot.getTime())) || best.reached(this.referenceBot.location(), 2.0)) {
			best.direction = 0;
		}
        
        best.selected = true;
        this.lastChoice = best;
       
        double orbitAngle = RUtil.getOrbitAngle(this.referenceBot.location(), best.wave.origin, best.direction);
        double dist = 1000;
        if (best.direction == 0) {
        	dist = 0;
        }
        this.move(orbitAngle, dist);
    }
    
    public ArrayList<RMoveChoice> simulateDirection(long startTime, RState rootState, RWave wave, GuessFactorArray segment, int direction) {
		ArrayList<RMoveChoice> choices = RUtil.simulateDirection(startTime, rootState, wave, direction);
		double maxAngle = Math.abs(Utils.normalRelativeAngle(wave.origin.absoluteBearingTo(choices.get(choices.size() - 1).position) - wave.targetState.absoluteBearing));
		if (direction > 0) {
			wave.maxEscapeClockwise = maxAngle;
		} else {
			wave.maxEscapeCounterClockwise = maxAngle;
		}
		
		// make a pass to calculate dangers
		for (RMoveChoice choice : choices) {
			double offsetAngle = (wave.origin.absoluteBearingTo(choice.position) - wave.targetState.absoluteBearing);
			double factor = Utils.normalRelativeAngle(offsetAngle) / maxAngle * wave.targetState.directionTraveling;
			choice.danger = segment.probabilityAt(factor);
		}
		
		return choices;
    }

    public void draw(Graphics2D g) {
        this.waveManager.draw(g);
        for (RMoveChoice choice : this.currentExaminedMoves) {
        	choice.draw(g);
        }
    }
}
