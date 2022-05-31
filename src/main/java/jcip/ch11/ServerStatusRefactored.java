package jcip.ch11;

import java.util.HashSet;
import java.util.Set;
import jcip.annotations.GuardedBy;
import jcip.annotations.ThreadSafe;

@ThreadSafe
class ServerStatusRefactored {
    @GuardedBy("users") final Set<String> users = new HashSet<>();
    @GuardedBy("queries") final Set<String> queries = new HashSet<>();

    void addUser(String u) {
        synchronized (users) {
            users.add(u);
        }
    }

    void addQuery(String q) {
        synchronized (queries) {
            queries.add(q);
        }
    }

    void removeUser(String u) {
        synchronized (users) {
            users.remove(u);
        }
    }

    void removeQuery(String q) {
        synchronized (queries) {
            queries.remove(q);
        }
    }
}
