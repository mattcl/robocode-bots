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
import rampancy.util.data.SegmentFn;
import rampancy.util.data.SegmentTree;

import robocode.Bullet;
import robocode.HitByBulletEvent;
import robocode.util.Utils;

public class RSurfer extends RMovement {
    protected RWaveManager waveManager;
    protected SegmentTree segmentTree;

    public RSurfer(RampantRobot referenceBot) {
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
    }

    public void update(REnemy enemy) {
        this.waveManager.maybeAddForEnemy(enemy);
    }

    public void onHitByBullet(HitByBulletEvent e) {
        Bullet bullet = e.getBullet();
        RPoint location = new RPoint(bullet.getX(), bullet.getY());
        RWave wave = waveManager.getWaveForBullet(bullet);
        if (wave == null) {
            System.out.println("Could not find wave for bullet hit!");
            return;
        }
        double offsetAngle = (wave.origin.absoluteBearingTo(location) - wave.targetState.absoluteBearing);
        double factor = Utils.normalRelativeAngle(offsetAngle) / RUtil.roughMaxEscapeAngle(wave.velocity) * wave.targetState.directionTraveling;
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

        double factor = segmentTree.getSegment(wave.targetState).stats.bestFactor();
        // simulate move clockwise
        // simulate move counter-clockwise
        // simulate stopping


    }

    public void draw(Graphics2D g) {
        this.waveManager.draw(g);
    }
}
