package rampancy.move;

import java.awt.Color;
import java.awt.Graphics2D;

import rampancy.Const;
import rampancy.util.RDrawable;
import rampancy.util.RPoint;
import rampancy.util.RRect;

public class RMoveChoice implements RDrawable {
	public RPoint position;
	public double danger;
	public int direction;
	public boolean selected;

    public RMoveChoice(RPoint position, double danger) {
    	this.position = position;
    	this.danger = danger;
    	this.selected = false;
    }
    
    public RMoveChoice(RPoint position, double danger, int direction) {
    	this.position = position;
    	this.danger = danger;
    	this.direction = direction;
    	this.selected = false;
    }

    public void draw(Graphics2D g) {
		Color old = g.getColor();
		g.setColor(Color.white);
    	this.position.draw(g);
    	
    	if (this.selected) {
			RRect outline = new RRect(this.position, Const.BOT_RADIUS);
			outline.draw(g);
    	}
    	
		g.setColor(old);
    }
}
