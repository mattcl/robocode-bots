package rampancy.gun;

import java.awt.Graphics2D;

import rampancy.util.RDrawable;
import rampancy.util.REnemy;
import rampancy.util.RPoint;
import rampancy.util.RWave;

public class RFiringSolution implements RDrawable {
    public RGun gun;
    public double power;
    public double firingAngle;
    public RPoint targetLocation;
    public long flightTime;
    public double hitProbability;
    public RDrawable drawable;
    public REnemy targetEnemy;
    public RWave wave;

    public RFiringSolution(RGun gun, double power, double firingAngle) {
        this(gun, power, firingAngle, null);
    }

    public RFiringSolution(RGun gun, double power, double firingAngle, RDrawable drawable) {
    	this.gun = gun;
        this.power = power;
        this.firingAngle = firingAngle;
        this.drawable = drawable;
    }
    
    public RFiringSolution withTarget(REnemy enemy) {
    	this.targetEnemy = enemy;
    	return this;
    }

    public RFiringSolution withProbability(double probability) {
        this.hitProbability = probability;
        return this;
    }
    
    public RFiringSolution withWave(RWave wave) {
    	this.wave = wave;
    	return this;
    }

    public void draw(Graphics2D g) {
        if (drawable != null) {
            drawable.draw(g);
        }
    }
}
