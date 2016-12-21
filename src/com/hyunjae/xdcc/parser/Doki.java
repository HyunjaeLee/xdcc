package com.hyunjae.xdcc.parser;

import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class Doki {

    public static Pack[] search(String keyword) throws Exception {

        Document doc = Jsoup.connect("http://xdcc.anidex.moe/search.php")
                .data("t", keyword)
                .get(); // Do post() to get all lists

        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.eval("function p(text){return p.k=new Array,eval(text),JSON.stringify(p.k)}");

        Invocable invocable = (Invocable) engine;

        String json = (String) invocable.invokeFunction("p", doc.text());

        return new Gson().fromJson(json, Pack[].class);

    }

}
