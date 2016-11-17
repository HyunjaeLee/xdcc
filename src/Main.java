import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.UtilSSLSocketFactory;
import org.pircbotx.cap.TLSCapHandler;
import org.pircbotx.exception.IrcException;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.IncomingFileTransferEvent;
import org.pircbotx.hooks.events.JoinEvent;

import java.io.File;
import java.io.IOException;

public class Main extends ListenerAdapter {

    private static PircBotX bot;

    public static void main(String[] args) throws Exception {

        Configuration configuration = new Configuration.Builder()
                .setName("PircBotX")
                .setAutoNickChange(true)
                .setCapEnabled(true)
                .addCapHandler(new TLSCapHandler(new UtilSSLSocketFactory().trustAllCertificates(), true))
                .addListener(new Main())
                .addServer("irc.rizon.net")
                .addAutoJoinChannel("#nipponsei")
                //.addAutoJoinChannel("#doki")
                .buildConfiguration();

        bot = new PircBotX(configuration);

        new Thread(() -> {
            try {
                bot.startBot();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IrcException e) {
                e.printStackTrace();
            }
        }).start();

        //System.out.println(bot.getUserChannelDao().containsChannel("#nipponsei"));

    }

    @Override
    public void onJoin(JoinEvent event) throws Exception {
        //event.getBot().send().message("Nippon|zongzing", "xdcc send #4698");
    }

    @Override
    public void onIncomingFileTransfer(IncomingFileTransferEvent event) throws Exception {

        File file = new File(System.getProperty("user.home") + "/Downloads/" + event.getSafeFilename());
        event.accept(file).transfer();
        /*
        Socket socket = new Socket(event.getAddress(), event.getPort(), getRealDccLocalAddress(event.getAddress()), 0);
        ReadableByteChannel rbc = Channels.newChannel(socket.getInputStream());
        FileOutputStream fos = new FileOutputStream(file);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
        rbc.close();
        */

    }
    /*
    private InetAddress getRealDccLocalAddress(InetAddress destAddress) {

        InetAddress address = bot.getConfiguration().getDccLocalAddress();
        address = (address != null && destAddress.getClass().equals(address.getClass())) ? address : bot.getConfiguration().getLocalAddress();
        address = (address != null && destAddress.getClass().equals(address.getClass())) ? address : bot.getLocalAddress();
        address = (address != null && destAddress.getClass().equals(address.getClass())) ? address : null;
        return address;

    }
    */





}
