package rampancy.gun;

import java.awt.Graphics2D;

import rampancy.util.RDrawable;

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
    }
}
