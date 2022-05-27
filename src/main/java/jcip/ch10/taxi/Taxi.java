package jcip.ch10.taxi;

import jcip.annotations.GuardedBy;

class Taxi {

    @GuardedBy("this") private Point location, destination;
    private final Dispatcher dispatcher;

    Taxi(Dispatcher dispatcher) {this.dispatcher = dispatcher;}

    public synchronized Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        boolean reachedDestination;
        synchronized (this) {
            this.location = location;
            reachedDestination = location.equals(destination);
        }
        if (reachedDestination)
            dispatcher.notifyAvailable(this);
    }
}
