package com.hyunjae.xdcc.bot2;

import java.io.*;
import java.net.Socket;

public class Bot {

    private Writer writer;

    public Bot(BotData botData) throws IOException {

        Socket socket = new Socket(botData.getServer(), 6667);
        writer = Writer.newWriter(socket);
        Listener listener = Listener.newListener(botData, writer);
        Reader.newReader(socket, listener);

        writer.sendRaw("USER " + botData.getNick() + " 8 * : bot");
        writer.sendRaw("NICK " + botData.getNick());
    }

    public void sendXdcc(String botName, String packNumber) {

        writer.sendRaw("PRIVMSG " + botName + " :xdcc send #" + packNumber);
    }
}
