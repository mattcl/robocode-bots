package rampancy.util;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import rampancy.Const;
import rampancy.RampantRobot;

import robocode.Bullet;

public class RWaveManager implements RDrawable {
    protected RampantRobot referenceBot;
    protected ArrayList<RWave> waves;

    public RWaveManager(RampantRobot referenceBot) {
        updateReferenceBot(referenceBot);
    }

    public void updateReferenceBot(RampantRobot referenceBot) {
        this.referenceBot = referenceBot;
        this.waves = new ArrayList<RWave>();
    }

    public void add(RWave wave) {
        this.waves.add(wave);
    }

    public void maybeAddForEnemy(REnemy enemy) {
        RState state = enemy.currentState();
        if (state != null && state.adjustedDeltaE < 0) {
            if (Math.abs(state.deltaV) < 2) { // TODO better wall hit detection
                double power = Math.abs(state.adjustedDeltaE);
                if (power >= Const.MIN_BULLET_POWER && power <= Const.MAX_BULLET_POWER) {
                    RState useableState = this.referenceBot.lastUsableState();
                    if (useableState != null) {
                        useableState = useableState.fromPerspective(state);
                    }

                    // we have to adjust by 2 ticks, since the bullet fires with 1
                    // tick of movement then we detect it 1 tick after it's been fired.
                    this.add(new RWave(
                        state.location.clone(),
                        state.time - 2,
                        power,
                        state,
                        useableState));
                }
            }
        }
    }

    public void update(long time) {
        List<RWave> broken = new ArrayList<RWave>();
        for (RWave wave : waves) {
            wave.update(time);
            if (wave.hasBroken(this.referenceBot.location())) {
                broken.add(wave);
            }
        }
        waves.removeAll(broken);
    }

    public RWave getWaveForBullet(Bullet bullet) {
        ArrayList<RWave> candidates = new ArrayList<RWave>();
        RPoint location = new RPoint(bullet.getX(), bullet.getY());
        double power = bullet.getPower();
        for (RWave wave : waves) {
            if (RUtil.almostEqual(wave.distanceTraveled, location.distance(wave.origin), 40) &&
                    RUtil.almostEqual(wave.power, power, 0.3)) {
                candidates.add(wave);
            }
        }

        if (candidates.isEmpty()) {
            return null;
        }

        // TODO: better candidate selection
        return candidates.get(0);
    }

    public void draw(Graphics2D g) {
        for (RWave wave : waves) {
            wave.draw(g);
        }
    }
}
