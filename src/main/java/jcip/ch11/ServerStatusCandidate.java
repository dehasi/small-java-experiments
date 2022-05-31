package jcip.ch11;

import java.util.HashSet;
import java.util.Set;
import jcip.annotations.GuardedBy;
import jcip.annotations.ThreadSafe;

@ThreadSafe
class ServerStatusCandidate {
    @GuardedBy("this") final Set<String> users = new HashSet<>();
    @GuardedBy("this") final Set<String> queries = new HashSet<>();

    synchronized void addUser(String u) {users.add(u);}

    synchronized void addQuery(String q) {queries.add(q);}

    synchronized void removeUser(String u) {users.remove(u);}

    synchronized void removeQuery(String q) {queries.remove(q);}
}
