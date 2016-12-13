package rampancy.util;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class RWaveManager implements RDrawable {
    protected ArrayList<RWave> waves;

    public RWaveManager() {
        this.waves = new ArrayList<RWave>();
    }

    public void add(RWave wave) {
        this.waves.add(wave);
    }

    public void update(long time) {

    }

    public void draw(Graphics2D g) {
        for (RWave wave : waves) {
            wave.draw(g);
        }
    }
}
