package com.hyunjae.xdcc.bot2;

import java.io.IOException;

public class BotBuilder {

    private String server;
    private String nick;
    private String[] channels;

    public static BotBuilder newBotBuilder() {
        return new BotBuilder();
    }

    public BotBuilder setServer(String server) {

        this.server = server;
        return this;
    }

    public BotBuilder setNick(String nick) {

        this.nick = nick;
        return this;
    }

    public BotBuilder setChannel(String... channel) {

        this.channels = channel;
        return this;
    }

    public Bot build() throws IOException {
        return new Bot(this.server, this.nick, this.channels);
    }
}
