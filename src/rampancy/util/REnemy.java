package rampancy.util;

import java.awt.Graphics2D;

import rampancy.Const;

public class REnemy implements RDrawable {
    protected String name;

    public RPoint location() {
        return null;
    }

    public RRect botRect() {
        return new RRect(this.location(), Const.BOT_RADIUS);
    }

    public void draw(Graphics2D g) {

    }
}
