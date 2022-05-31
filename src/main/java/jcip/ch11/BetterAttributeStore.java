package jcip.ch11;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import jcip.annotations.GuardedBy;
import jcip.annotations.ThreadSafe;

@ThreadSafe
class BetterAttributeStore {
    @GuardedBy("this") private final Map<String, String> attributes = new HashMap<>();

    public boolean userLocationMatchers(String name, String regexp) {
        String key = "users." + name + ".location";
        String location;
        synchronized (this) {
            location = attributes.get(key);
        }
        if (location == null) return false;
        else return Pattern.matches(regexp, location);
    }
}
