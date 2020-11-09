package rampancy.util;

import java.awt.Color;
import java.awt.Graphics2D;

import rampancy.Const;

public class RWave implements RDrawable {

    public RPoint origin;
    public Color color;
    public long timeFired;
    public double power;
    public double velocity;
    public double distanceTraveled;
    public RState shooterState;
    public RState targetState;
    
	public RWave(RPoint origin, long timeFired, double power, double velocity, double distanceTraveled,
			RState shooterState, RState targetState) {
		this.origin = origin.clone();
		this.timeFired = timeFired;
		this.power = power;
		this.velocity = velocity;
		this.distanceTraveled = velocity;
		this.color = Const.WAVE_COLOR;
		this.shooterState = shooterState;
		this.targetState = targetState;
	}

    public RWave(RPoint origin, long timeFired, double power, RState shooterState, RState targetState) {
    	this(
			origin, 
			timeFired, 
			power, 
			RUtil.getBulletVelocity(power),
			RUtil.getBulletVelocity(power), // velocity is distance because of when wave was detected
			shooterState, 
			targetState
		);
    }
    
    public RWave copy() {
    	return new RWave(
			this.origin, 
			this.timeFired, 
			this.power, 
			this.velocity,
			this.distanceTraveled,
			this.shooterState, 
			this.targetState
		);
    }
   
    // used for simulating waves during surfing
    public void tick() {
    	this.distanceTraveled += velocity;
    }
    
    public void update(long time) {
        this.distanceTraveled = (time - timeFired) * velocity;
    }

    public double distanceFrom(RPoint point) {
        return point.distance(origin) - distanceTraveled;
    }

    public double timeToImpact(RPoint point) {
        return distanceFrom(point) / velocity;
    }

    public boolean hasBroken(RPoint target) {
        return target.distance(origin) + Const.WAVE_BREAK_TOLERANCE < distanceTraveled;
    }

    public void draw(Graphics2D g) {
        RCircle circle = new RCircle(origin, distanceTraveled, Const.WAVE_COLOR);
        circle.draw(g);
    }
}
