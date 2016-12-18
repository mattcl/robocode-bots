package rampancy.move;

import java.awt.Graphics2D;
import java.util.ArrayList;

import rampancy.RampantRobot;
import rampancy.SegmentFns;
import rampancy.util.REnemy;
import rampancy.util.RPoint;
import rampancy.util.RState;
import rampancy.util.RWave;
import rampancy.util.RWaveManager;
import rampancy.util.data.SegmentFn;
import rampancy.util.data.SegmentTree;

import robocode.Bullet;
import robocode.HitByBulletEvent;

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
        RWave wave = waveManager.getWaveForBullet(e.getBullet());
    }

    public void execute() {
        this.waveManager.update(this.referenceBot.getTime());
    }

    public void draw(Graphics2D g) {
        this.waveManager.draw(g);
    }
}
