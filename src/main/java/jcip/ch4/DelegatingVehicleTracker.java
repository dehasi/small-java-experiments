package jcip.ch4;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static java.util.Collections.unmodifiableMap;

class DelegatingVehicleTracker {
    private final ConcurrentMap<String, Point> locations;
    private final Map<String, Point> unmodifiableMap;

    DelegatingVehicleTracker(Map<String, Point> points) {
        locations = new ConcurrentHashMap<>(points);
        unmodifiableMap = unmodifiableMap(locations);
    }

    Map<String, Point> getLocations() {
        return unmodifiableMap;
    }

    Point getLocation(String id) {
        return locations.get(id);
    }

    void setLocation(String id, int x, int y) {
        if (locations.replace(id, new Point(x, y)) == null)
            throw new IllegalArgumentException("Invalid vehicle name: " + id);
    }
}
