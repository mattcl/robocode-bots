package rampancy.gun;

import java.util.ArrayList;
import java.util.HashMap;

import rampancy.util.REnemy;

public class RPMGun extends RGun {
	protected HashMap<String, REnemy> enemyStates;
	
	public RPMGun() {
		this.enemyStates = new HashMap<String, REnemy>();
	}
	
    public String name() {
        return "Pattern Matching Gun";
    }

    public void update(REnemy enemy) {
    	
    }

    public RFiringSolution firingSolution(REnemy enemy, double desiredPower) {
        return null;
    }
}
