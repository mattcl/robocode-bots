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

    public boolean maybeAddForEnemy(REnemy enemy) {
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
                    
                    for (int i = this.waves.size() - 1; i >= 0; i--) {
                    	if (this.waves.get(i).dummy) {
                    		this.waves.remove(i);
                    	}
                    }
                    
                    return true;
                }
            }
        }
        
        return false;
    }
    
    public boolean hasDummyWaves() {
    	for (RWave wave : this.waves) {
    		if (wave.dummy) {
    			return true;
    		}
    	}
    	
    	return false;
    }

    public ArrayList<RWave> update(long time) {
        ArrayList<RWave> broken = new ArrayList<RWave>();
        for (RWave wave : waves) {
            wave.update(time);
            if (wave.hasBroken(this.referenceBot.location())) {
                broken.add(wave);
                wave.broken = true;
            }
        }
        waves.removeAll(broken);
        return broken;
    }

    public RWave getWaveForBullet(Bullet bullet) {
        ArrayList<RWave> candidates = new ArrayList<RWave>();
        RPoint location = new RPoint(bullet.getX(), bullet.getY());
        double power = bullet.getPower();
        for (RWave wave : waves) {
            if (RUtil.almostEqual(wave.distanceTraveled, location.distance(wave.origin), Const.WAVE_BREAK_TOLERANCE + 2) &&
                    RUtil.almostEqual(wave.power, power, 0.5)) {
                candidates.add(wave);
            }
        }

        if (candidates.isEmpty()) {
            return null;
        }

        // TODO: better candidate selection
        return candidates.get(0);
    }

    public RWave getClosestWave(RPoint location) {
        double closest = Double.POSITIVE_INFINITY;
        RWave closestWave = null;
        for (RWave wave : waves) {
        	if (!wave.broken) {
				double time = wave.timeToImpact(location);
				if (time < closest) {
					closest = time;
					closestWave = wave;
				}
        	}
        }
        return closestWave;
    }

    public void draw(Graphics2D g) {
        for (RWave wave : waves) {
            wave.draw(g);
        }
    }
}
