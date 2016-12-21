package com.hyunjae.xdcc.bot2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

public class Bot implements Observer {

    private static final Logger logger = LoggerFactory.getLogger(Bot.class);

    private BufferedWriter writer;
    private String nick;
    private String channel;

    public Bot(String server, String nick, String channel) throws IOException {

        this.nick = nick;
        this.channel = channel;

        Socket socket = new Socket(server, 6667);
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        sendRaw("USER " + nick + " 8 * : bot");
        sendRaw("NICK " + nick);

        final Reader reader = new Reader(socket);
        reader.addObserver(this);
        run(reader);
    }

    @Override
    public void update(Observable o, Object arg) {

        String str = (String) arg;
        if(str.split(" ")[1].equals("001")) { // Connected to server
            joinChannel();
        } else if(str.startsWith("PING")) {
            pong(str);
        } else if(str.contains("433")) { // Nickname is already in use
            changeNick();
        } else if(str.contains("DCC SEND")) {
            xdcc(str);
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

    public void sendXdcc(String botName, String packNumber) {
        sendRaw("PRIVMSG " + botName + " :xdcc send #" + packNumber);
    }

    private void joinChannel() {
        sendRaw("JOIN " + channel);
    }

    private void pong(String s) {
        sendRaw("PONG " + s.substring(5));
    }

    private void changeNick() {
        sendRaw("NICK " + getRandomNick(nick));
    }

    private void xdcc(String s) {

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
        return ip.contains(":") ? ip : longToIp(Long.parseLong(ip)); // IPv6 check
    }

    private static String longToIp(long ip) {

        return ((ip >> 24) & 0xFF) + "."
                + ((ip >> 16) & 0xFF) + "."
                + ((ip >> 8) & 0xFF) + "."
                + (ip & 0xFF);
    }

    private static void run(Runnable o) {
        new Thread(o).start();
    }
}
