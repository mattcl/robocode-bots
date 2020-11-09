package rampancy.mega;

import java.awt.Color;

import rampancy.RampantRobot;
import rampancy.gun.RCTGun;
import rampancy.gun.RDisabledGun;
import rampancy.move.RSimpleSurfer;

public class Durandal extends RampantRobot {
    @Override
    public void firstTimeSetup() {
        super.firstTimeSetup();

        gunManager.updateReferenceBot(this);
        gunManager.addGun(new RCTGun());
        gunManager.addGun(new RDisabledGun());
        
        movementManager.setMovement(new RSimpleSurfer(this));
    }

    @Override
    public void setUpColors() {
        setColors(Color.black, new Color(0x0D5E10), new Color(0x0D5E10), Color.white, Color.blue);
    }
}
