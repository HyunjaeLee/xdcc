import com.hyunjae.xdcc.bot2.Bot;

import com.hyunjae.xdcc.bot2.BotBuilder;
import com.hyunjae.xdcc.bot2.Status;
import com.hyunjae.xdcc.parser.Doki;
import com.hyunjae.xdcc.parser.Nipponsei;
import com.hyunjae.xdcc.parser.Pack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {

        Pack[] packs = Nipponsei.all();
        for(Pack pack : packs) {
            logger.debug("#{} {} {} {}M", pack.getPackNumber(), pack.getBotName(), pack.getFileName(), pack.getFileSize());
        }

        Bot bot = BotBuilder.newBotBuilder()
                .setServer("irc.rizon.net")
                .setNick("bot")
                .setChannel("#nipponsei", "#doki")
                .build();

        while(true) {
            if(bot.getChannelStatus("#doki") == Status.OK
                    && bot.getChannelStatus("#nipponsei") == Status.OK)
                break;
            else
                Thread.sleep(100);
        }

        bot.xdccSend("Nippon|zongzing","3665");
    }
}
