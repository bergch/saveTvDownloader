package de.da_bubu.savetvdownloader.renamer;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;


public class RepoReader {

    public Map<String, String> getRepoMap(URL repoXML) {
        Map<String, String> repo = new HashMap<String, String>();

        SAXReader reader = new SAXReader();
        Document document;
        try {
            document = reader.read(repoXML);
        } catch (DocumentException e) {
            throw new IllegalArgumentException(e);
        }

        String xpathExpression = "//repository/entry";
        @SuppressWarnings("unchecked")
        List<Node> nodes = document.selectNodes(xpathExpression);

        for (Node node : nodes) {
            String title = node.selectSingleNode("title").getText();
            String url = node.selectSingleNode("url").getText();
            repo.put(title, url);
        }
        return repo;

    }


}
