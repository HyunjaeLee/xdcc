import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class Main {

    public static void main(String[] args) throws Exception {


        for (Pack pack : Doki("girlish 1080")) {
            System.out.println(pack.getFileName() + " | " + pack.getMessage());
        }

        for(Pack pack : Nipponsei()) {
            System.out.println(pack.getFileName() + " | " + pack.getMessage());
        }

    }

    public static Pack[] Doki(String keyword) throws Exception {

        Document doc = Jsoup.connect("http://xdcc.anidex.moe/search.php")
                .data("t", keyword)
                .get(); // Do post() to get all lists

        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        engine.eval("function p(text){return p.k=new Array,eval(text),JSON.stringify(p.k)}");

        Invocable invocable = (Invocable) engine;

        String json = (String) invocable.invokeFunction("p", doc.text());

        return new Gson().fromJson(json, Pack[].class);

    }

    public static Pack[] Nipponsei() throws Exception {

        Document doc = Jsoup.connect("https://nipponsei.minglong.org/packlist/archive/").get();

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

    private static String parseInt(String s) {
        return s.replaceAll("[^0-9]", "");
    }

}
