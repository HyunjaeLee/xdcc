import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

public class Bot2 {

    private static final Logger logger = LoggerFactory.getLogger(Bot2.class);

    private BufferedWriter writer;
    private BufferedReader reader;

    private String server;
    private String nick;
    private String channel;

    public Bot2(String server, String nick, String channel) throws IOException {

        this.server = server;
        this.nick = nick;
        this.channel = channel;

        Socket socket = new Socket(server, 6667);
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        sendRaw("USER " + nick + " 8 * : bot");
        sendRaw("NICK " + nick);

        new Thread(() -> {
            try {
                listen();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }).start();
    }

    private void listen() throws IOException {

        String line;
        while((line = reader.readLine()) != null) {
            logger.debug(line);

            if(line.split(" ")[1].equals("001")) { // Connected to server
                sendRaw("JOIN " + channel);
            } else if(line.startsWith("PING")) {
                sendRaw("PONG " + line.substring(5));
            } else if(line.contains("433")) { // Nickname is already in use
                sendRaw("NICK " + getRandomNick(nick));
            } else if(line.contains("DCC SEND")) {
                String[] str = line.split("DCC SEND ")[1].split(" ");
                String filename = str[0];
                String ip = parseIp(str[1]);
                int port = Integer.parseInt(str[2]);
                String fileSize = "";
                if(str.length == 4)
                    fileSize = str[3];

                logger.debug("filename: {}, ip: {}, port: {}, fileSize: {}", filename, ip, port, fileSize);

                String file = System.getProperty("user.home") + "/Downloads/" + filename;
                new Thread(() -> {
                    try {
                        transfer2(ip, port, file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        }
    }

    private void sendRaw(String str) {

        logger.debug(str);

        try {
            writer.write(str + "\r\n");
            writer.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void sendXdcc(String botName, String packNumber) {

        sendRaw("PRIVMSG " + botName + " :xdcc send #" + packNumber);
    }

    private static void transfer2(String ip, int port, String file) throws IOException {

        Socket socket = new Socket(ip, port);
        InputStream is = socket.getInputStream();
        FileOutputStream fos = new FileOutputStream(file);
        IOUtils.copy(is, fos);
        is.close();
        fos.close();

        logger.debug("Download finished: {}", file);
    }

    private void transfer(String ip, int port, String file) throws IOException {

        Socket socket = new Socket(ip, port);
        BufferedInputStream socketInput = new BufferedInputStream(socket.getInputStream());
        BufferedOutputStream socketOutput = new BufferedOutputStream(socket.getOutputStream());
        BufferedOutputStream fileOutput = new BufferedOutputStream(new FileOutputStream(new File(file)));

        byte[] inBuffer = new byte[1024];
        byte[] outBuffer = new byte[4];
        int bytesRead;
        long bytesTransferred = 0;
        while((bytesRead = socketInput.read(inBuffer, 0, inBuffer.length)) != -1) {

            fileOutput.write(inBuffer, 0, bytesRead);
            bytesTransferred += bytesRead;

            outBuffer[0] = (byte) ((bytesTransferred >> 24) & 0xff);
            outBuffer[1] = (byte) ((bytesTransferred >> 16) & 0xff);
            outBuffer[2] = (byte) ((bytesTransferred >> 8) & 0xff);
            outBuffer[3] = (byte) (bytesTransferred & 0xff);

            socketOutput.write(outBuffer);
            socketOutput.flush();
        }

        socketInput.close();
        socketOutput.close();
        fileOutput.close();
    }

    private static String parseIp(String ip) {

        if(ip.contains(":")) // IPv6
            return ip;
        else
            return longToIp(Long.parseLong(ip));
    }

    private static String longToIp(long ip) {

        return ((ip >> 24) & 0xFF) + "."
                + ((ip >> 16) & 0xFF) + "."
                + ((ip >> 8) & 0xFF) + "."
                + (ip & 0xFF);
    }

    private static String getRandomNick(String nick) {
        return nick + System.currentTimeMillis();
    }
}
