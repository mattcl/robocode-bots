package rampancy.move;

import java.awt.Color;
import java.awt.Graphics2D;

import rampancy.Const;
import rampancy.util.RDrawable;
import rampancy.util.RPoint;
import rampancy.util.RRect;
import rampancy.util.RUtil;
import rampancy.util.RWave;

public class RMoveChoice implements RDrawable {
	public RPoint position;
	public double danger;
	public int direction;
	public boolean selected;
	public Color color;
	public long eta;
	public RWave wave;

    public RMoveChoice(RPoint position, double danger) {
    	this.position = position;
    	this.danger = danger;
    	this.selected = false;
    	this.color = Color.WHITE;
    	this.eta = -1;
    }
    
    public RMoveChoice(RPoint position, double danger, int direction) {
    	this.position = position;
    	this.danger = danger;
    	this.direction = direction;
    	this.selected = false;
    	this.color = Color.WHITE;
    	this.eta = -1;
    }
    
    public RMoveChoice(RPoint position, double danger, int direction, long eta) {
    	this.position = position;
    	this.danger = danger;
    	this.direction = direction;
    	this.selected = false;
    	this.color = Color.WHITE;
    	this.eta = eta;
    }
    
    public boolean hasETA() {
    	return this.eta >= 0;
    }
    
    public boolean hasWave() {
    	return this.wave != null;
    }
    
    public boolean reachedTime(long time) {
    	return time >= this.eta;
    }
    
    public boolean reached(RPoint location, double threshold) {
    	return this.position.distance(location) < threshold;
    }
    
    public void setColor(Color color) {
    	this.color = color;
    }

    public void draw(Graphics2D g) {
		Color old = g.getColor();
		g.setColor(this.color);
    	this.position.draw(g);
    	
    	if (this.selected) {
			RRect outline = new RRect(this.position, Const.BOT_RADIUS);
			outline.draw(g);
    	}
    	
		g.setColor(old);
    }
}
