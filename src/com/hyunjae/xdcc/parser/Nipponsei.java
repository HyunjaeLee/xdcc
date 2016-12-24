package com.hyunjae.xdcc.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Nipponsei {

    public static Pack[] all() {

        Document doc = null;
        try {
            doc = Jsoup.connect("https://nipponsei.minglong.org/packlist/archive/").get();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    public static Pack[] search(String keyword) {

        String _keyword = keyword.trim().toLowerCase();
        List<Pack> packList = new ArrayList<>();
        Pack[] all = all();
        for(Pack pack : all) {
            String _filename = pack.getFileName().toLowerCase();
            if(_filename.contains(_keyword)) {
                packList.add(pack);
            }
        }

        return packList.toArray(new Pack[packList.size()]);
    }

    private static String parseInt(String s) {
        return s.replaceAll("[^0-9]", "");
    }

}
