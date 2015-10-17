package de.da_bubu.savetvdownloader.renamer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class NamePartMatcher {
    
    Repository repo;
    public NamePartMatcher(Repository repo) {
        this.repo = repo;
    }

    public String match(String fileName) throws Exception {
        List<String> serisRepo = getSeriesEpisodes(fileName);
        String seriesName = Levenshtein.extractSeriesName(fileName, repo.getEpisodeMap().keySet());
        if(serisRepo!=null) {
            List<String> buzzWords = getBuzzWords(fileName, seriesName);
        
        for (String candicate : serisRepo) {
            boolean matches = true;
            for (String buzzWord : buzzWords) {
                if(!candicate.contains(buzzWord)) {
                    matches = false;
                }
            } 
            
            if(matches){
                
                seriesName = seriesName.replace("_", " ");
                return seriesName + " - " + candicate + ".mp4";
            }
        }
        
        
        }
        
        return fileName;
    }

    private List<String> getSeriesEpisodes(String fileName) {
        Map<String, List<String>> map=null;
        try {
            map = repo.getEpisodeMap();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String seariesName = Levenshtein.extractSeriesName(fileName, map.keySet());
        return  map.get(seariesName);  
    }
    
    private List<String> getBuzzWords(String downloadFileName, String seriesName) {
        String[] candiates = downloadFileName.split(seriesName)[1].split("_");
//        String[] candiates = downloadFileName.substring(seriesName.length()).split("_");
        List<String> words = new ArrayList<String>();
        for (String string : candiates) {
            //TODO replace with an redex
            if(!string.startsWith("2013") && !string.startsWith("2014")  && !string.toLowerCase().contains("folge")) {
                if(!string.equals("")){
                    String trim = string.trim();
//                    trim.replace("ae", "a");
//                    trim.replace("oe", "o");
//                    trim.replace("ue", "u");
                    words.add(trim);
                }
            }else{
                return words;
            }
        }

        return words;
    }


}
