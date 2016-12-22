import com.hyunjae.xdcc.bot2.Bot;

import com.hyunjae.xdcc.bot2.BotBuilder;
import com.hyunjae.xdcc.bot2.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {

        Bot bot = BotBuilder.newBotBuilder()
                .setServer("irc.rizon.net")
                .setNick("bot")
                .setChannel("#nipponsei", "#doki")
                .build();

        while(true) {
            if(bot.getChannelStatus("#doki") == Status.OK)
                break;
            else
                Thread.sleep(100);
        }

        bot.sendXdcc("Doki|Homura","2374");
    }
}
