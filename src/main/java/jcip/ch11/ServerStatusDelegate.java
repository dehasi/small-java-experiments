package jcip.ch11;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import jcip.annotations.ThreadSafe;

import static java.util.Collections.synchronizedSet;

@ThreadSafe
class ServerStatusDelegate {
    final Set<String> users = new ConcurrentSkipListSet<>();
    final Set<String> queries = synchronizedSet(new HashSet<>());

    void addUser(String u) {
        users.add(u);
    }

    void addQuery(String q) {
        queries.add(q);
    }

    void removeUser(String u) {
        users.remove(u);
    }

    void removeQuery(String q) {
        queries.remove(q);
    }
}
