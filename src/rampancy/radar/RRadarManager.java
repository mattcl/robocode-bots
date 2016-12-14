package rampancy.radar;

import java.util.HashMap;

public class RRadarManager {
    protected HashMap<String, RScan> scans;

    public RRadarManager() {
        this.scans = new HashMap<String, RScan>();
    }
}
