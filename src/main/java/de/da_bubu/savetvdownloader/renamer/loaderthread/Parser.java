package de.da_bubu.savetvdownloader.renamer.loaderthread;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Parser {

    public List<String> parse(String URL) throws IOException {

        try {

            Document doc = Jsoup.connect(URL).timeout(10 * 1000).get();
            Elements tables = doc.select("table[class]");

            List<String> res = new ArrayList<String>();

            for (Element tab : tables) {
                if (tab.attr("class").equals("episodenliste")) {
                    for (Element tr : tab.select("tr")) {
                        boolean first = true;
                        boolean second = false;
                        String staffel = "";
                        String episode = "";
                        String name = "";
                        if (tr.attr("class").contains("ep-hover tr")) {
                            for (Element td : tr.select("td")) {
                                if (td.toString().contains("808080")) {
                                    if (first) {
                                        staffel = td.text().replace(".", "");
                                        if (staffel.length() == 1) {
                                            staffel = "0" + staffel;
                                        }
                                        first = false;
                                        second = true;
                                    } else if (second) {
                                        episode = td.text();
                                        second = false;
                                    }
                                }

                                if (td.attr("class").equals("episodenliste-titel")) {
                                    name = td.childNode(0).childNode(0).toString();
                                }
                            }
                            String fullName = "S" + staffel + "E" + episode + " - " + name;
                            res.add(StringEscapeUtils.unescapeHtml(fullName));
                        }
                    }
                }
            }
            return res;
        } finally {

        }
    }
}
