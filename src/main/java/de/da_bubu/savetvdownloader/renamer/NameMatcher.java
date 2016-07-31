package de.da_bubu.savetvdownloader.renamer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.sound.midi.Receiver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import de.da_bubu.savetvdownloader.main.Main;
import de.da_bubu.savetvdownloader.main.Recording;
import de.da_bubu.savetvdownloader.savetvparser.ParsedRecording;

public class NameMatcher {

    Repository repository;
    private final Log LOG = LogFactory.getLog(Main.class);

    public NameMatcher(Repository repository) {
        this.repository = repository;
    }

    public Recording searchMatch(ParsedRecording recording) throws IOException {
        Map<String, List<String>> map = repository.getEpisodeMap(); 

        Recording rec = new Recording();
        if(recording.getSFOLGE() != null && !("".equals(recording.getSFOLGE()))) {
            rec.setSeries(recording.getSTITLE());
            rec.setName(recording.getSTITLE() +" - " + recording.getSFOLGE() + " - " + recording.getSSUBTITLE());
            return rec;
        }
        List<String> epsFronRepo = null;
        epsFronRepo = map.get(recording.getSTITLE());

        List<String> candidates = null;
        if (epsFronRepo != null) {
            candidates = Levenshtein.getStringsWithMinimalDist(recording.getSSUBTITLE(), epsFronRepo);
        }

        // if found return the substituted filename
        if (candidates != null) {
            String newName = recording.getSTITLE() + " - " + candidates.get(0);

                List<String> sigWords = getSignificantWords(recording.getSSUBTITLE(), recording.getSTITLE());

                for (String string : sigWords) {
                    if (!newName.toLowerCase().contains(string.toLowerCase())) {
                        LOG.info(recording+" sigword "+string+" not found in "+newName);
                        rec.setName(recording.getSTITLE() + "-"+recording.getSSUBTITLE());
                        rec.setSeries(recording.getSTITLE());
                    }
                }
                LOG.info("mapped "+recording+" to "+newName);
                 rec.setName(newName);
                 rec.setSeries(recording.getSTITLE());
                 return rec;
        }
        // if non found return the original
        LOG.info("no mapping found for "+recording);
        return null;

    }
   
    private List<String> getSignificantWords(String downloadFileName, String series) {

        String[] candiates = downloadFileName.split(" ");
        List<String> words = new ArrayList<String>();
        for (String string : candiates) {
            // TODO replace with an redex
            if (!string.startsWith("2013") && !string.startsWith("2014") && !string.startsWith("2015") && !string.toLowerCase().contains("folge")
                    && !string.toLowerCase().contains("ue") && !string.toLowerCase().contains("oe")
                    && !string.toLowerCase().contains("ae") && !string.toLowerCase().contains(".") && !string.toLowerCase().contains("-")) {
                if (!string.equals("")) {
                    words.add(string.trim().toLowerCase());
                }
            } else {
                return words;
            }
        }

        return words;
    }

}
