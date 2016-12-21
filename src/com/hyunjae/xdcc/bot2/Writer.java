package com.hyunjae.xdcc.bot2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Writer implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Writer.class);

    private Socket socket;
    private BufferedWriter writer;

    public static Writer newWriter(Socket socket) {

        Writer writer = new Writer(socket);
        new Thread(writer).start();
        return writer;
    }

    private Writer(Socket socket) {

        this.socket = socket;
    }

    @Override
    public void run() {

        try {
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void sendRaw(String str) {

        logger.debug(str);

        try {
            writer.write(str + "\r\n");
            writer.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
