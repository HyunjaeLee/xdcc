package com.hyunjae.xdcc.bot2;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class FileTransfer implements Runnable{

    private static final Logger logger = LoggerFactory.getLogger(Writer.class);

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
            InputStream is = socket.getInputStream();
            FileOutputStream fos = new FileOutputStream(file);
            IOUtils.copy(is, fos);
            is.close();
            fos.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        logger.debug("File transfer complete : {}", file);
    }
}
