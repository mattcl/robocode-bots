package rampancy.gun;

import java.util.HashMap;

import rampancy.RampantRobot;
import rampancy.util.REnemy;

public class RGunManager {
    protected RampantRobot referenceBot;
    protected long fireTime;
    protected HashMap<String, RGun> guns;

    public RGunManager(RampantRobot referenceBot) {
        this.referenceBot = referenceBot;
        this.guns = new HashMap<String, RGun>();
    }

    public void updateReferenceBot(RampantRobot referenceBot) {
        this.referenceBot = referenceBot;
        this.fireTime = 0;
    }

    public void update(REnemy enemy) {

    }

    public void execute() {
        if (fireTime == referenceBot.getTime() && referenceBot.getGunTurnRemainingRadians() == 0) {

        }
    }
}
