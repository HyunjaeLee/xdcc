import com.hyunjae.xdcc.bot2.Bot;
import com.hyunjae.xdcc.bot2.BotBuilder;
import com.hyunjae.xdcc.parser.Nipponsei;
import com.hyunjae.xdcc.parser.Pack;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        /*
        for(Pack pack : Nipponsei.all()) {
            System.out.printf("pack: %s filename: %s size: %sM\n", pack.getPackNumber(), pack.getFileName(), pack.getFileSize());
        }
        */
        Bot bot = BotBuilder.newBotBuilder()
                .setServer("irc.rizon.net")
                .setNick("bot")
                .setChannel("#nipponsei")
                .build();

        Scanner scanner = new Scanner(System.in);
        while(true) {
            String s = scanner.nextLine();
            bot.sendXdcc("Nippon|zongzing", s);
        }
    }
}
