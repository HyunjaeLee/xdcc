import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        /*
        for(Pack pack : Nipponsei.all()) {
            System.out.printf("pack: %s filename: %s size: %sM\n", pack.getPackNumber(), pack.getFileName(), pack.getFileSize());
        }
        */

        Bot2 bot = new Bot2("irc.rizon.net", "bot", "#nipponsei");

        Scanner scanner = new Scanner(System.in);
        while(true) {
            String s = scanner.nextLine();
            bot.sendXdcc("Nippon|zongzing", s);
        }
    }
}
