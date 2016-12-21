package com.hyunjae.xdcc.bot;

import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.IncomingFileTransferEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class BotListener extends ListenerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(BotListener.class);

    @Override
    public void onIncomingFileTransfer(IncomingFileTransferEvent event) throws Exception {

        logger.info("Download started - filename: {}, filesize: {}", event.getSafeFilename(), event.getFilesize());

        File file = new File(System.getProperty("user.home") + "/Downloads/" + event.getSafeFilename());
        event.accept(file).transfer();

        logger.info("Download finished - filename: {}, filesize: {}", event.getSafeFilename(), event.getFilesize());

    }

}
