package rampancy.move;

import java.awt.Graphics2D;
import java.util.ArrayList;

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

    public RSimpleSurfer(RampantRobot referenceBot) {
        updateReferenceBot(referenceBot);
        ArrayList<SegmentFn> segmentFns = new ArrayList<SegmentFn>();
        segmentFns.add(SegmentFns.DISTANCE);
        segmentFns.add(SegmentFns.DELTA_H);
        segmentFns.add(SegmentFns.ACCELERATING);
        segmentFns.add(SegmentFns.WALL_DISTANCE);
        this.segmentTree = new SegmentTree(segmentFns);
    }

    public void updateReferenceBot(RampantRobot referenceBot) {
        super.updateReferenceBot(referenceBot);
        this.waveManager = new RWaveManager(referenceBot);
        this.currentExaminedMoves = new ArrayList<RMoveChoice>();
    }

    public void update(REnemy enemy) {
        this.waveManager.maybeAddForEnemy(enemy);
    }
    
    public double getFactor(RWave wave, RPoint location) {
        double offsetAngle = (wave.origin.absoluteBearingTo(location) - wave.targetState.absoluteBearing);
        return Utils.normalRelativeAngle(offsetAngle) / RUtil.roughMaxEscapeAngle(wave.velocity) * wave.targetState.directionTraveling;
    }

    public void onHitByBullet(HitByBulletEvent e) {
        Bullet bullet = e.getBullet();
        RPoint location = new RPoint(bullet.getX(), bullet.getY());
        RWave wave = waveManager.getWaveForBullet(bullet);
        if (wave == null) {
            System.out.println("Could not find wave for bullet hit!");
            return;
        }
        double factor = this.getFactor(wave, location);
        SegmentTree.Node segment = segmentTree.getSegment(wave.targetState);
        segment.stats.update(factor);
        segment.markVisit();
    }

    public void execute() {
        this.waveManager.update(this.referenceBot.getTime());
        RWave wave = this.waveManager.getClosestWave(this.referenceBot.location());
        if (wave == null) {
            // TODO
            return;
        }

        GuessFactorArray segment = segmentTree.getSegment(wave.targetState).stats;
        
        // simulate move clockwise
        ArrayList<RMoveChoice> choices = this.simulateDirection(wave, segment, 1);
        // simulate move counter-clockwise
        choices.addAll(this.simulateDirection(wave, segment, -1));
        // simulate stopping
		double danger = segment.probabilityAt(this.getFactor(wave, this.referenceBot.location()));
        choices.add(new RMoveChoice(this.referenceBot.location(), danger, 0));
        
        double min = 2;
        RMoveChoice best = null;
        
        for (RMoveChoice choice: choices) {
        	if (choice.danger < min) {
        		best = choice;
        		min = choice.danger;
        	}
        }
        
        if (best == null) {
        	return;
        }
        
        best.selected = true;
        
        this.currentExaminedMoves = choices;
       
        double orbitAngle = RUtil.getOrbitAngle(this.referenceBot.location(), wave.origin, best.direction);
        double dist = 1000;
        if (best.direction == 0) {
        	dist = 0;
        }
        this.move(orbitAngle, dist);
    }
    
    public ArrayList<RMoveChoice> simulateDirection(RWave wave, GuessFactorArray segment, int direction) {
    	wave = wave.copy();
    	ArrayList<RMoveChoice> locations = new ArrayList<RMoveChoice>();
    	
        MoveSim sim = RUtil.makeMoveSim(this.referenceBot.currentState());
        
        for(int i = 0; i < 1000; i++) {
        	RPoint loc = RUtil.simulateOrbit(sim, wave.origin, direction);
        	double danger = segment.probabilityAt(this.getFactor(wave, loc));
        	RMoveChoice location = new RMoveChoice(loc, danger, direction);
			locations.add(location);
			
			wave.tick();
			if (wave.hasBroken(location.position)) {
				break;
			}
        }
    	return locations;
    }

    public void draw(Graphics2D g) {
        this.waveManager.draw(g);
        for (RMoveChoice choice : this.currentExaminedMoves) {
        	choice.draw(g);
        }
    }
}
