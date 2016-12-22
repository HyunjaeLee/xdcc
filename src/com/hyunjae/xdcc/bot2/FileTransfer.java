package com.hyunjae.xdcc.bot2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class FileTransfer implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(FileTransfer.class);

    private String ip;
    private int port;
    private long fileSize;
    private String file;

    public static FileTransfer newFileTransfer(String ip, int port, long fileSize, String file) {

        FileTransfer fileTransfer = new FileTransfer(ip, port, fileSize, file);
        new Thread(fileTransfer).start();
        return fileTransfer;
    }

    private FileTransfer(String ip, int port, long fileSize , String file) {

        this.ip = ip;
        this.port = port;
        this.fileSize = fileSize;
        this.file = file;
    }

    @Override
    public void run() {

        try {
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress(ip, port));
            FileChannel fileChannel = new FileOutputStream(file).getChannel();
            fileChannel.transferFrom(socketChannel, 0, fileSize);
            socketChannel.close();
            fileChannel.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }

        logger.debug("File transfer complete : {}", file);
    }
}
