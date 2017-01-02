package com.hyunjae.xdcc.bot2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class FileTransfer implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(FileTransfer.class);

    private String ip;
    private int port;
    private String file;

    public static void newAsynchronousFileTransfer(String ip, int port, String file) {

        FileTransfer fileTransfer = new FileTransfer(ip, port, file);
        new Thread(fileTransfer).start();
    }

    private FileTransfer(String ip, int port, String file) {

        this.ip = ip;
        this.port = port;
        this.file = file;
    }

    @Override
    public void run() {

        try(SocketChannel socketChannel = SocketChannel.open();
        FileChannel fileChannel = new FileOutputStream(file).getChannel()) {

            socketChannel.connect(new InetSocketAddress(ip, port));
            ByteBuffer inBuffer = ByteBuffer.allocate(1024);
            ByteBuffer outBuffer = ByteBuffer.allocate(4);
            outBuffer.order(ByteOrder.BIG_ENDIAN);

            int bytesRead;
            int bytesTransferred = 0;
            while((bytesRead = socketChannel.read(inBuffer)) != -1) {

                inBuffer.flip();
                fileChannel.write(inBuffer);
                inBuffer.clear();

                //Convert bytesTransfered to an "unsigned, 4 byte integer in network byte order", per DCC specification
                bytesTransferred += bytesRead;
                outBuffer.putInt(bytesTransferred); // TODO : Use unsigned-int
                outBuffer.flip();
                socketChannel.write(outBuffer);
                outBuffer.clear();
            }
        } catch(IOException e) {
            logger.error(e.getMessage());
        }

        logger.debug("File transfer complete : {}", file);
    }
}
