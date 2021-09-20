package jcip.ch7;

public class Shutdown {

    public static void main(String[] args) {
        new Shutdown().run();
    }

    synchronized void run() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> System.out.println("Bye")));
        Thread thread = new Thread(() -> {
            long i = 0;
            while (true)
                System.err.printf("aaa: %s", ++i);
        });
        thread.start();
        System.out.println("exiting");
        System.exit(0);
    }
}