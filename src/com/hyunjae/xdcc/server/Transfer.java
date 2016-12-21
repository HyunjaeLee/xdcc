package com.hyunjae.xdcc.server;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Transfer implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Transfer.class);

    private String filename;
    private String ip;
    private int port;
    private String fileSize;
    private Socket clientSocket;

    public Transfer(String filename, String ip, int port, String fileSize, Socket clientSocket) {

        this.filename = filename;
        this.ip = ip;
        this.port = port;
        this.fileSize = fileSize;
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {

        try {
            OutputStream outputStream = clientSocket.getOutputStream();
            outputStream.write(("HTTP/1.1 200 OK\r\n" +
                    "Accept-Ranges: bytes\r\n" +
                    "Content-Length: " + fileSize + "\r\n" +
                    "Content-Type: application/octet-stream\r\n" +
                    "Content-Disposition: attachment; filename=\"" + filename + "\"\r\n" +
                    "\r\n")
                    .getBytes());

            Socket socket = new Socket(ip, port);
            IOUtils.copy(socket.getInputStream(), outputStream); // TODO
            logger.debug("File upload complete");
            outputStream.flush();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
