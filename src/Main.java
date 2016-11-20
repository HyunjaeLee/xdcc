import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {

        for(Pack pack : Nipponsei.all()) {
            System.out.printf("pack: %s filename: %s size: %sM\n", pack.getPackNumber(), pack.getFileName(), pack.getFileSize());
        }

        Bot bot = new Bot();

        while(!bot.isJoined()) {
            Thread.sleep(100); // Wait for the bot to join
        }

        Scanner scanner = new Scanner(System.in);
        while(true) {
            String s = scanner.nextLine();
            bot.sendXdcc("Nippon|zongzing", s);
        }

    }
}
