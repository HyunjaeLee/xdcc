package com.hyunjae.xdcc.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

public class Wrapper implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Wrapper.class);

    private Socket clientSocket;
    private String server;
    private String nick;
    private String channel;
    private String botName;
    private String packNumber;

    public Wrapper(Socket clientSocket, String server, String nick, String channel, String botName, String packNumber) {

        this.clientSocket = clientSocket;
        this.server = server;
        this.nick = nick;
        this.channel = channel;
        this.botName = botName;
        this.packNumber = packNumber;
    }

    @Override
    public void run() {

        try {
            Socket ircSocket = new Socket(server, 6667);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ircSocket.getOutputStream()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(ircSocket.getInputStream()));

            writer.write("USER " + nick + " 8 * : bot" + "\r\n");
            writer.write("NICK " + nick + "\r\n");
            writer.flush();

            String line;
            while((line = reader.readLine()) != null) {

                logger.debug(line);

                if(getNumeric(line).equals("001")) { // Connected to server

                    writer.write("JOIN " + channel + "\r\n");
                    writer.flush();

                } else if(line.startsWith("PING")) {

                    writer.write("PONG " + line.substring(5) + "\r\n");
                    writer.flush();

                } else if(getNumeric(line).equals("433")) { // Nickname is already in use

                    String randomNick = nick + System.currentTimeMillis();
                    writer.write("NICK " + randomNick + "\r\n");
                    writer.flush();

                } else if(getNumeric(line).equals("332") || getNumeric(line).equals("331")) {

                    writer.write("PRIVMSG " + botName + " :xdcc send #" + packNumber + "\r\n");
                    writer.flush();

                } else if(line.contains("DCC SEND")) {

                    String[] str = line.split("DCC SEND ")[1].split(" ");
                    String filename = str[0];
                    String ip = parseIp(str[1]);
                    int port = Integer.parseInt(str[2]);
                    String fileSize = (str.length == 4) ? str[3] : "";

                    Transfer transfer = new Transfer(filename, ip, port, fileSize, clientSocket);
                    new Thread(transfer).start();
                }
            }

            ircSocket.close();

        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }

    private static String getNumeric(String str) {
        return (str.contains(" ")) ? str.split(" ")[1] : "";
    }

    private static String parseIp(String ip) {
        return ip.contains(":") ? ip : longToIp(Long.parseLong(ip)); // IPv6 check
    }

    private static String longToIp(long ip) {

        return ((ip >> 24) & 0xFF) + "."
                + ((ip >> 16) & 0xFF) + "."
                + ((ip >> 8) & 0xFF) + "."
                + (ip & 0xFF);
    }
}
