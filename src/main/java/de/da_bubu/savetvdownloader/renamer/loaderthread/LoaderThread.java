package de.da_bubu.savetvdownloader.renamer.loaderthread;

import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import de.da_bubu.savetvdownloader.main.Main;

public class LoaderThread extends Thread {
    private  final Log LOG = LogFactory.getLog(Main.class);
    private Map<String, List<String>> repo;

    private String url;
    
    private String key;
    

    public LoaderThread(Map<String, List<String>> repo, String url, String key) {
        this.repo = repo;
        this.url = url;
        this.key = key;
    }

    @Override
    public void run() {
        super.run();
        Parser parser = new Parser();
        try {
            LOG.info("read episode repository for "+key);
            repo.put(key, parser.parse(url));
            LOG.info("done read episode repository for "+key);
        } catch (Exception e) {
            e.printStackTrace();
            
        }
    }

}
