import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.IncomingFileTransferEvent;

import java.io.File;

public class BotListener extends ListenerAdapter {

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
