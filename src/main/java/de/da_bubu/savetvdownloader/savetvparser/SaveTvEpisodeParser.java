package de.da_bubu.savetvdownloader.savetvparser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;



public class SaveTvEpisodeParser {

    public List<ParsedRecording> parse(String toParse) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        JsonParser jsonParser = new JsonParser();
        JsonElement root = jsonParser.parse(toParse);
        
        JsonArray recordingsArray = root.getAsJsonObject().get("ARRVIDEOARCHIVEENTRIES").getAsJsonArray();
        
        
        List<ParsedRecording> parsedRecordings = new ArrayList<ParsedRecording>();
        for (JsonElement jsonRecording : recordingsArray) {
            jsonRecording = jsonRecording.getAsJsonObject().get("STRTELECASTENTRY");
            
            ParsedRecording recording = new ParsedRecording();
            for (String arrtibName : ParsedRecording.ATTRIBUTES) {
                String val = jsonRecording.getAsJsonObject().get(arrtibName).getAsString();
                val = val.replace("'", "");
                Class<? extends ParsedRecording> clazz = recording.getClass();
                Method setter = clazz.getMethod("set"+arrtibName, String.class);
                setter.invoke(recording, val);
            }
            
            // only add addfree recordings
            if("true".equals(recording.getBADFREEAVAILABLE())) {
                parsedRecordings.add(recording);
            }
        }
        
        
        return sort(parsedRecordings);
    }


    private List<ParsedRecording> sort(List<ParsedRecording> recordings) {
        recordings.sort((p1, p2) -> new BigDecimal(p1.getIDAYSLEFTBEFOREDELETE()).compareTo(new BigDecimal(p2.getIDAYSLEFTBEFOREDELETE())));
        return recordings;
    }

   

}
