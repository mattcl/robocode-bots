package rampancy.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import rampancy.Const;
import rampancy.move.RMoveChoice;

public class RWave implements RDrawable {

    public RPoint origin;
    public Color color;
    public long timeFired;
    public double power;
    public double velocity;
    public double distanceTraveled;
    public RState shooterState;
    public RState targetState;
    public boolean broken;
    public RMoveChoice moveChoice;
    public boolean dummy;
    
    public double maxEscapeClockwise;
    public double maxEscapeCounterClockwise;
    
	public RWave(RPoint origin, long timeFired, double power, double velocity, double distanceTraveled,
			RState shooterState, RState targetState, boolean broken) {
		this.origin = origin.clone();
		this.timeFired = timeFired;
		this.power = power;
		this.velocity = velocity;
		this.distanceTraveled = velocity;
		this.color = Const.WAVE_COLOR;
		this.shooterState = shooterState;
		this.targetState = targetState;
		this.broken = broken;
		this.maxEscapeClockwise = RUtil.roughMaxEscapeAngle(this.velocity);
		this.maxEscapeCounterClockwise = RUtil.roughMaxEscapeAngle(this.velocity);
	}

    public RWave(RPoint origin, long timeFired, double power, RState shooterState, RState targetState) {
    	this(
			origin, 
			timeFired, 
			power, 
			RUtil.getBulletVelocity(power),
			RUtil.getBulletVelocity(power), // velocity is distance because of when wave was detected
			shooterState, 
			targetState,
			false
		);
    }
    
    public RWave copy() {
    	RWave newWave = new RWave(
			this.origin, 
			this.timeFired, 
			this.power, 
			this.velocity,
			this.distanceTraveled,
			this.shooterState, 
			this.targetState,
			this.broken
		);
    	newWave.dummy = this.dummy;
    	newWave.moveChoice = this.moveChoice;
    	newWave.maxEscapeClockwise = this.maxEscapeClockwise;
    	newWave.maxEscapeCounterClockwise = this.maxEscapeCounterClockwise;
    	return newWave;
    }
    
    public RWave asDummy() {
    	this.dummy = true;
    	return this;
    }
    
    public RMoveChoice getMoveChoice() {
    	return this.moveChoice;
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
    	Color color = Const.WAVE_COLOR;
    	if (this.dummy) {
    		color = Color.pink;
    	} else {
			RPoint cw = this.origin.projectTo(this.maxEscapeClockwise + this.targetState.absoluteBearing, 500);
			g.setColor(Color.red);
			g.draw(new Line2D.Double(this.origin, cw));
			
			RPoint ccw = this.origin.projectTo(this.targetState.absoluteBearing - this.maxEscapeCounterClockwise, 500);
			g.setColor(Color.green);
			g.draw(new Line2D.Double(this.origin, ccw));
    	}
    	
        RCircle circle = new RCircle(origin, distanceTraveled, color);
        circle.draw(g);
    }
}
