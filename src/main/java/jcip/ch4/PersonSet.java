package jcip.ch4;

import java.util.HashSet;
import java.util.Set;
import jcip.annotations.GuardedBy;
import jcip.annotations.ThreadSafe;

@ThreadSafe
class PersonSet {
    @GuardedBy("this")
    private final Set<Person> mySet = new HashSet<>();

    synchronized void addPerson(Person p) {
        mySet.add(p);
    }

    synchronized boolean containsPerson(Person p) {
        return mySet.contains(p);
    }

    private class Person {}
}
