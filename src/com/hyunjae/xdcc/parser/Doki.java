package com.hyunjae.xdcc.parser;

import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Doki {

    public static Pack[] all() throws Exception {

        final Document doc = Jsoup.connect("http://xdcc.anidex.moe/search.php")
                .maxBodySize(Integer.MAX_VALUE) // Get complete document
                .userAgent("Mozilla/5.0")
                .get();

        return regexDocument(doc);
    }

    public static Pack[] search(String keyword) throws Exception {

        final Document doc = Jsoup.connect("http://xdcc.anidex.moe/search.php")
                .userAgent("Mozilla/5.0")
                .data("t", keyword)
                .get();

        return regexDocument(doc);
    }

    private static Pack[] regexDocument(Document doc) {

        Pattern pattern = Pattern.compile("p\\.k\\[.*?] = \\{b:\"(.*?)\", n:(.*?), s:(.*?), f:\"(.*?)\"};");
        Matcher matcher = pattern.matcher(doc.text());

        List<Pack> packList = new ArrayList<>();

        while(matcher.find()) {

            String botName = matcher.group(1);
            String packNumber = matcher.group(2);
            String fileSize = matcher.group(3);
            String fileName = matcher.group(4);

            Pack pack = new Pack();
            pack.setBotName(botName);
            pack.setFileName(fileName);
            pack.setFileSize(fileSize);
            pack.setPackNumber(packNumber);

            packList.add(pack);
        }

        return packList.toArray(new Pack[packList.size()]);
    }

    @Deprecated
    private static Pack[] parseDocument(Document doc) throws Exception {

        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.eval("function p(text){return p.k=new Array,eval(text),JSON.stringify(p.k)}");

        Invocable invocable = (Invocable) engine;

        String json = (String) invocable.invokeFunction("p", doc.text());

        return new Gson().fromJson(json, Pack[].class);
    }
}
