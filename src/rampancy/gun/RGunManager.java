package rampancy.gun;

import java.util.ArrayList;
import java.util.HashMap;

import rampancy.RampantRobot;
import rampancy.util.REnemy;

import robocode.Bullet;

public class RGunManager {
    protected RampantRobot referenceBot;
    protected long fireTime;
    protected HashMap<String, RGun> guns;
    protected RFiringSolution lockedSolution;
    protected ArrayList<RBullet> bullets;

    public RGunManager(RampantRobot referenceBot) {
        this.referenceBot = referenceBot;
        this.bullets = new ArrayList<RBullet>();
        this.guns = new HashMap<String, RGun>();
    }

    public void updateReferenceBot(RampantRobot referenceBot) {
        this.referenceBot = referenceBot;
        this.bullets = new ArrayList<RBullet>();
        this.fireTime = 0;
    }

    public void update(REnemy enemy) {
        for (RGun gun : guns.values()) {
            gun.update(enemy);
        }
    }

    public void execute() {
        if (fireTime == referenceBot.getTime() && referenceBot.getGunTurnRemainingRadians() == 0) {
            Bullet shot = referenceBot.setFireBullet(lockedSolution.power);
            if (shot != null) {
                RBullet bullet = new RBullet(shot, lockedSolution);
                bullets.add(bullet);
            }
        }

        for (RBullet bullet : bullets) {

        }
    }
}
