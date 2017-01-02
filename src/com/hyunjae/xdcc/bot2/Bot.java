package com.hyunjae.xdcc.bot2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class Bot implements Observer {

    private static final Logger logger = LoggerFactory.getLogger(Bot.class);

    private BufferedWriter writer;
    private String nick;
    private String[] channels;
    private Map<String, Status> channelStatus;

    public Bot(String server, String nick, String channels[]) throws IOException {

        this.nick = nick;
        this.channels = channels;
        this.channelStatus = new HashMap<>();
        for(String channel : channels) {
            channelStatus.put(channel, Status.WAITING);
        }

        Socket socket = new Socket(server, 6667);
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        sendRaw("USER " + nick + " 8 * : bot");
        sendRaw("NICK " + nick);

        final Reader reader = new Reader(socket);
        reader.addObserver(this);
        new Thread(reader).start();
    }

    @Override
    public void update(Observable o, Object arg) {

        String str = (String) arg;
        if(getNumeric(str).equals("001")) { // Connected to server

            for(String channel : channels) {
                sendRaw("JOIN " + channel);
            }

        } else if(str.startsWith("PING")) {

            sendRaw("PONG " + str.substring(5));

        } else if(getNumeric(str).equals("433")) { // Nickname is already in use

            sendRaw("NICK " + nick + System.currentTimeMillis());

        } else if(getNumeric(str).equals("332") || getNumeric(str).equals("331")) { // Joined to channel

            String channel = str.split(" ")[3];
            channelStatus.put(channel, Status.OK);

        } else if(str.contains("DCC SEND")) {

            String[] s = str.split("DCC SEND ")[1].split(" ");

            if(s.length != 4)
                return;

            String filename = s[0];
            String ip = parseIp(s[1]);
            int port = Integer.parseInt(s[2]);
            long fileSize = Long.parseLong(s[3].replaceAll("\\u0001", "")); // Fix NumberFormatException

            logger.debug("filename: {}, ip: {}, port: {}, fileSize: {}", filename, ip, port, fileSize);

            String file = System.getProperty("user.home") + "/Downloads/" + filename;
            FileTransfer.newAsynchronousFileTransfer(ip, port, file);
        }
    }

    private void sendRaw(String str) {

        logger.debug(str);

        try {
            writer.write(str + "\r\n");
            writer.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void xdccSend(String botName, String packNumber) {
        sendRaw("PRIVMSG " + botName + " :xdcc send #" + packNumber);
    }

    public void quit() {
        sendRaw("QUIT");
    }

    public Status getChannelStatus(String channel) {
        return channelStatus.get(channel);
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
