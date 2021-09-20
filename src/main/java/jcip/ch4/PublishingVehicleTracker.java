package jcip.ch4;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import jcip.annotations.ThreadSafe;

import static java.util.Collections.unmodifiableMap;

@ThreadSafe
class PublishingVehicleTracker {
    private final Map<String, SafePoint> locations;
    private final Map<String, SafePoint> unmodifiableMap;

    public PublishingVehicleTracker(Map<String, SafePoint> locations) {
        this.locations = new ConcurrentHashMap<>(locations);
        this.unmodifiableMap = unmodifiableMap(this.locations);
    }

    Map<String, SafePoint> getLocations() {
        return unmodifiableMap;
    }

    SafePoint getLocation(String id) {
        return locations.get(id);
    }

    void setLocation(String id, int x, int y) {
        if (!locations.containsKey(id))
            throw new IllegalArgumentException("Invalid vehicle name: " + id);
        locations.get(id).set(x, y);
    }
}
