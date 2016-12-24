package com.hyunjae.xdcc.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    private static final int THREAD_CNT = 5;
    private static ExecutorService executorService = Executors.newFixedThreadPool(THREAD_CNT);

    public Server() {

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8080);

            while(true) {
                Socket clientSocket = serverSocket.accept();
                logger.debug("Connected to {}", clientSocket.getInetAddress());
                Wrapper wrapper = new Wrapper(clientSocket,
                        "irc.rizon.net",
                        "bot",
                        "#nipponsei",
                        "Nippon|zongzing",
                        "4668");
                executorService.execute(wrapper);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }
}
