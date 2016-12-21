package com.hyunjae.xdcc.bot2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Listener implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Listener.class);

    private String server;
    private String nick;
    private String channel;
    private Writer writer;

    public static Listener newListener(BotData botData, Writer writer) {

        Listener listener = new Listener(botData, writer);
        new Thread(listener).start();
        return listener;
    }

    public Listener(BotData botData, Writer writer) {

        this.server = botData.getServer();
        this.nick = botData.getNick();
        this.channel = botData.getChannel();
        this.writer = writer;
    }

    @Override
    public void run() {

    }

    public void joinChannel() {
        writer.sendRaw("JOIN " + channel);
    }

    public void pong(String s) {
        writer.sendRaw("PONG " + s.substring(5));
    }

    public void changeNick() {
        writer.sendRaw("NICK " + getRandomNick(nick));
    }

    public void xdcc(String s) {

        String[] str = s.split("DCC SEND ")[1].split(" ");
        String filename = str[0];
        String ip = parseIp(str[1]);
        int port = Integer.parseInt(str[2]);
        String fileSize = "";
        if(str.length == 4)
            fileSize = str[3];

        logger.debug("filename: {}, ip: {}, port: {}, fileSize: {}", filename, ip, port, fileSize);

        String file = System.getProperty("user.home") + "/Downloads/" + filename;
        FileTransfer.newFileTransfer(ip, port, file);
    }

    private static String getRandomNick(String nick) {
        return nick + System.currentTimeMillis();
    }

    private static String parseIp(String ip) {

        if(ip.contains(":")) // IPv6
            return ip;
        else
            return longToIp(Long.parseLong(ip));
    }

    private static String longToIp(long ip) {

        return ((ip >> 24) & 0xFF) + "."
                + ((ip >> 16) & 0xFF) + "."
                + ((ip >> 8) & 0xFF) + "."
                + (ip & 0xFF);
    }
}
