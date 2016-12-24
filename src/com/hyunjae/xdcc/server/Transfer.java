package com.hyunjae.xdcc.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
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
            BufferedOutputStream clientOutputStream = new BufferedOutputStream(clientSocket.getOutputStream());
            clientOutputStream.write(("HTTP/1.1 200 OK\r\n" +
                    "Accept-Ranges: bytes\r\n" +
                    "Content-Length: " + fileSize + "\r\n" +
                    "Content-Type: application/octet-stream\r\n" +
                    "Content-Disposition: attachment; filename=\"" + filename + "\"\r\n" +
                    "\r\n")
                    .getBytes());
            clientOutputStream.flush();

            Socket socket = new Socket(ip, port);

            BufferedInputStream socketInput = new BufferedInputStream(socket.getInputStream());
            OutputStream socketOutput = socket.getOutputStream();

            byte[] inBuffer = new byte[1024];
            byte[] outBuffer = new byte[4];
            int bytesRead;
            long bytesTransferred = 0;
            //Read next part of incomming file
            while((bytesRead = socketInput.read(inBuffer, 0, inBuffer.length)) != -1) {

                //Write to file
                clientOutputStream.write(inBuffer, 0, bytesRead);
                bytesTransferred += bytesRead;

                //Send back an acknowledgement of how many bytes we have got so far.
                //Convert bytesTransfered to an "unsigned, 4 byte integer in network byte order", per DCC specification
                outBuffer[0] = (byte) ((bytesTransferred >> 24) & 0xff);
                outBuffer[1] = (byte) ((bytesTransferred >> 16) & 0xff);
                outBuffer[2] = (byte) ((bytesTransferred >> 8) & 0xff);
                outBuffer[3] = (byte) (bytesTransferred & 0xff);

                socketOutput.write(outBuffer);
            }

            socketInput.close();
            socketOutput.close();
            clientOutputStream.flush();
            clientOutputStream.close();
            socket.close();

            logger.debug("File upload complete");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
