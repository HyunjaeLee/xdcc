package com.hyunjae.xdcc.bot2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Reader implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Reader.class);

    private Socket socket;
    private Listener listener;
    private BufferedReader reader;

    public static Reader newReader(Socket socket, Listener listener) {

        Reader reader = new Reader(socket, listener);
        new Thread(reader).start();
        return reader;
    }

    private Reader(Socket socket, Listener listener) {

        this.socket = socket;
        this.listener = listener;
    }

    @Override
    public void run() {

        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            read();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void read() throws IOException {

        String line;
        while((line = reader.readLine()) != null) {
            logger.debug(line);

            if(line.split(" ")[1].equals("001")) { // Connected to server
                listener.joinChannel();
            } else if(line.startsWith("PING")) {
                listener.pong(line);
            } else if(line.contains("433")) { // Nickname is already in use
                listener.changeNick();
            } else if(line.contains("DCC SEND")) {
                listener.xdcc(line);
            }
        }
    }
}
