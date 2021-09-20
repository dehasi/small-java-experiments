package jcip.ch4;

import java.util.HashMap;
import java.util.Map;
import jcip.annotations.GuardedBy;
import jcip.annotations.ThreadSafe;

import static java.util.Collections.unmodifiableMap;

@ThreadSafe
class MonitorVehicleTracker {
    @GuardedBy("this")
    private final Map<String, MutablePoint> locations;

    MonitorVehicleTracker(Map<String, MutablePoint> locations) {this.locations = deepCopy(locations);}

    synchronized Map<String, MutablePoint> getLocations() {
        return deepCopy(locations);
    }

    synchronized MutablePoint getLocation(String id) {
        MutablePoint loc = locations.get(id);
        return loc == null ? null : new MutablePoint(loc);
    }

    synchronized void setLocation(String id, int x, int y) {
        MutablePoint loc = locations.get(id);
        if (loc == null)
            throw new IllegalArgumentException("No such ID: " + id);
        loc.x = x;
        loc.y = y;
    }

    private static Map<String, MutablePoint> deepCopy(Map<String, MutablePoint> m) {
        Map<String, MutablePoint> result = new HashMap<>();
        for (String id : m.keySet())
            result.put(id, new MutablePoint(m.get(id)));

        return unmodifiableMap(result);
    }
}
