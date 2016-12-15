package rampancy.mega;

import rampancy.RampantRobot;
import rampancy.gun.RCTGun;

public class Durandal extends RampantRobot {
    @Override
    public void firstTimeSetup() {
        super.firstTimeSetup();

        gunManager.updateReferenceBot(this);
        gunManager.addGun(new RCTGun());
    }
}
