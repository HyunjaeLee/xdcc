package com.hyunjae.xdcc.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Nipponsei {

    public static Pack[] all() throws Exception {

        final Document doc = Jsoup.connect("https://nipponsei.minglong.org/packlist/archive/").get();

        Elements rows = doc.select("#main table tr");
        int size = rows.size();
        Pack[] packs = new Pack[size-9];
        for (int i = 5, j = size-4, k = 0; i < j; i++, k++){
            Element row = rows.get(i);
            Elements columns = row.select("td");
            packs[k] = new Pack("Nippon|zongzing", parseInt(columns.get(0).text()), columns.get(3).text(), parseInt(columns.get(2).text()));
        }

        return packs;
    }

    // TODO : ADD search(String str)

    private static String parseInt(String s) {
        return s.replaceAll("[^0-9]", "");
    }

}
