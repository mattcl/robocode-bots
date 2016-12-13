package rampancy.util;

import java.awt.Graphics2D;
import java.util.HashMap;

import rampancy.RampantRobot;

public class REnemyManager implements RDrawable {
    protected HashMap<String, REnemy> enemies;
    protected RampantRobot referenceBot;

    public REnemyManager(RampantRobot referenceBot) {
        this.referenceBot = referenceBot;
        this.enemies = new HashMap<String, REnemy>();
    }

    public void updateReferenceBot(RampantRobot referenceBot) {
        this.referenceBot = referenceBot;
        for (REnemy enemy : enemies.values()) {
            enemy.updateReferenceBot(referenceBot);
        }
    }

    public REnemy get(String name) {
        if (!enemies.containsKey(name)) {
            enemies.put(name, new REnemy(name, referenceBot));
        }
        return enemies.get(name);
    }

    public void draw(Graphics2D g) {
        for (REnemy enemy : enemies.values()) {
            enemy.draw(g);
        }
    }
}
