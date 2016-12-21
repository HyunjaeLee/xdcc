package com.hyunjae.xdcc.bot2;

import java.io.IOException;

public class BotBuilder {

    private BotData botData;

    public static BotBuilder newBotBuilder() {
        return new BotBuilder();
    }

    private BotBuilder() {
        botData = new BotData();
    }

    public BotBuilder setServer(String server) {
        botData.setServer(server);
        return this;
    }

    public BotBuilder setNick(String nick) {
        botData.setNick(nick);
        return this;
    }

    public BotBuilder setChannel(String channel) {
        botData.setChannel(channel);
        return this;
    }

    public Bot build() throws IOException {
        return new Bot(botData);
    }
}
