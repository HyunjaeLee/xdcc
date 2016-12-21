package com.hyunjae.xdcc.bot2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Observable;

public class Reader extends Observable implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Reader.class);

    private Socket socket;

    public Reader(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String line;
            while((line = reader.readLine()) != null) {
                logger.debug(line);
                setChanged();
                notifyObservers(line);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
