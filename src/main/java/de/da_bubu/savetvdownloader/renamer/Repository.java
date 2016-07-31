package de.da_bubu.savetvdownloader.renamer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import de.da_bubu.savetvdownloader.main.Main;
import de.da_bubu.savetvdownloader.renamer.loaderthread.LoaderThread;

public class Repository {
    public  Map<String, String> repo = null;
    private  final Log LOG = LogFactory.getLog(Main.class);

    public Repository() {
    }

    public Map<String, String> getRepo() {
        RepoReader repoReader = new RepoReader();
        return repoReader.getRepoMap(this.getClass().getResource("repo.xml"));
    }

    private static  Map<String, List<String>> res = null; 

    public  Map<String, List<String>> getEpisodeMap() throws IOException {
        if (res == null) {
            LOG.info("start reading of episode repository");
            res = new ConcurrentHashMap<String, List<String>>();
            List<Thread> loaders = new ArrayList<Thread>();
            for (String key : getRepo().keySet()) {

                loaders.add( new LoaderThread(res, getRepo().get(key), key));
                
            }
            
            for (Thread thread : loaders) {
                thread.start();
            }
            
            for (Thread thread : loaders) {
                try {
                    thread.join(0);
                } catch (InterruptedException e) {
                    throw new IllegalArgumentException(e);
                }
            }
            LOG.info("finished reading of episode repository");
        }

        return res;

    }

}
