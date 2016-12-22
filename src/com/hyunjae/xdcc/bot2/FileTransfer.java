package com.hyunjae.xdcc.bot2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

public class FileTransfer implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(FileTransfer.class);

    private String ip;
    private int port;
    private String file;

    public static FileTransfer newFileTransfer(String ip, int port, String file) {

        FileTransfer fileTransfer = new FileTransfer(ip, port, file);
        new Thread(fileTransfer).start();
        return fileTransfer;
    }

    private FileTransfer(String ip, int port, String file) {

        this.ip = ip;
        this.port = port;
        this.file = file;
    }

    @Override
    public void run() {

        try {
            Socket socket = new Socket(ip, port);
            BufferedInputStream socketInput = new BufferedInputStream(socket.getInputStream());
            BufferedOutputStream socketOutput = new BufferedOutputStream(socket.getOutputStream());
            BufferedOutputStream fileOutput = new BufferedOutputStream(new FileOutputStream(new File(file)));

            byte[] inBuffer = new byte[1024];
            byte[] outBuffer = new byte[4];
            int bytesRead;
            long bytesTransferred = 0;
            //Read next part of incomming file
            while((bytesRead = socketInput.read(inBuffer, 0, inBuffer.length)) != -1) {

                //Write to file
                fileOutput.write(inBuffer, 0, bytesRead);
                bytesTransferred += bytesRead;

                //Send back an acknowledgement of how many bytes we have got so far.
                //Convert bytesTransfered to an "unsigned, 4 byte integer in network byte order", per DCC specification
                outBuffer[0] = (byte) ((bytesTransferred >> 24) & 0xff);
                outBuffer[1] = (byte) ((bytesTransferred >> 16) & 0xff);
                outBuffer[2] = (byte) ((bytesTransferred >> 8) & 0xff);
                outBuffer[3] = (byte) (bytesTransferred & 0xff);

                socketOutput.write(outBuffer);
                socketOutput.flush();
            }

            socketInput.close();
            socketOutput.close();
            fileOutput.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        logger.debug("File transfer complete : {}", file);
    }
}
