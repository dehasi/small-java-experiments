package jcip.ch7;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

class ReaderThread extends Thread {

    private static final int BUFSZ = 42;

    private final Socket socket;
    private final InputStream in;

    ReaderThread(Socket socket, InputStream in) {
        this.socket = socket;
        this.in = in;
    }

    @Override public void interrupt() {
        try {
            socket.close();
        } catch (IOException ignored) {
        } finally {
            super.interrupt();
        }
    }

    @Override public void run() {
        try {
            byte[] buf = new byte[BUFSZ];
            while (true) {
                int count = in.read(buf);
                if (count < 0)
                    break;
                else
                    processBuffer(buf, count);
            }
        } catch (IOException e) { /* Allow thread to exit */ }
    }

    private void processBuffer(byte[] buf, int count) { }
}
