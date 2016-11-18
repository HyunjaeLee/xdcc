import org.pircbotx.*;
import org.pircbotx.exception.IrcException;

import java.io.IOException;

public class Bot {

    private PircBotX bot;

    public Bot() {
        bot = createBot();
        startBot(bot);
    }

    public PircBotX getBot() {
        return bot;
    }

    public void sendXdcc(String botName, String packNumber) {
        bot.send().message(botName, "xdcc send #" + packNumber);
    }

    public boolean isJoined() {
        return bot.getUserChannelDao().containsChannel("#nipponsei");
    }

    public static void startBot(PircBotX bot) {

        new Thread(() -> {
            try {
                bot.startBot();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IrcException e) {
                e.printStackTrace();
            }
        }).start();

    }

    public static PircBotX createBot() {

        Configuration configuration = new Configuration.Builder()
                .setName("PircBotX")
                .setAutoNickChange(true)
                .setAutoReconnect(true)
                .addListener(new BotListener())
                .addServer("irc.rizon.net")
                .addAutoJoinChannel("#nipponsei")
                //.addAutoJoinChannel("#doki")
                .buildConfiguration();

        return new PircBotX(configuration);

    }
}
