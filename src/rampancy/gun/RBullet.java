package rampancy.gun;

import java.awt.Graphics2D;

import rampancy.util.RCircle;
import rampancy.util.RDrawable;
import rampancy.util.RPoint;

import robocode.Bullet;

public class RBullet implements RDrawable {
    protected RFiringSolution firingSolution;
    protected Bullet bullet;

    public RBullet(Bullet bullet, RFiringSolution firingSolution) {
        this.bullet = bullet;
        this.firingSolution = firingSolution;
    }

    public boolean active() {
        return this.bullet.isActive();
    }

    public void draw(Graphics2D g) {
        firingSolution.draw(g);
        new RCircle(new RPoint(bullet.getX(), bullet.getY()), 2).draw(g);
    }
}
