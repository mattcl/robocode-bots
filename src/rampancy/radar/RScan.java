package rampancy.radar;

import rampancy.util.RPoint;

public class RScan {
    public long time;
    public RPoint location;

    public RScan(long time, RPoint location) {
        this.time = time;
        this.location = location.clone();
    }
}
