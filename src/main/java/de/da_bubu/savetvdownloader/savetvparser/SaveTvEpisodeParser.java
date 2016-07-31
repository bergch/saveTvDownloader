package de.da_bubu.savetvdownloader.savetvparser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import de.da_bubu.savetvdownloader.main.Main;

public class SaveTvEpisodeParser {

    static DecimalFormat format = getDFormat();

    static private DecimalFormat getDFormat() {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(0);
        df.setMinimumFractionDigits(0);
        df.setGroupingUsed(false);
        df.setMaximumIntegerDigits(309);
        return df;
    }

    public List<ParsedRecording> parse(String toParse)
            throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        JsonParser jsonParser = new JsonParser();
        JsonElement root = null;
        try {
            root = jsonParser.parse(toParse);
        } catch (JsonSyntaxException e) {
            Main.LOG.error("json error:" + e.getLocalizedMessage());
            Main.LOG.info(toParse);
            throw e;
        }

        JsonArray recordingsArray = root.getAsJsonObject().get("ARRVIDEOARCHIVEENTRIES").getAsJsonArray();

        List<ParsedRecording> parsedRecordings = new ArrayList<ParsedRecording>();
        for (JsonElement jsonRecording : recordingsArray) {
            jsonRecording = jsonRecording.getAsJsonObject().get("STRTELECASTENTRY");

            ParsedRecording recording = new ParsedRecording();
            for (String arrtibName : ParsedRecording.ATTRIBUTES) {
                String val = "";
                if (arrtibName.equals("ITELECASTID")) {
                    val = format.format(new BigDecimal(jsonRecording.getAsJsonObject().get(arrtibName).getAsString()));
                } else {
                    val = jsonRecording.getAsJsonObject().get(arrtibName).getAsString();
                    val = val.replace("'", "");
                }
                Class<? extends ParsedRecording> clazz = recording.getClass();
                Method setter = clazz.getMethod("set" + arrtibName, String.class);
                setter.invoke(recording, val);
            }

            // only add addfree recordings
            if ("true".equals(recording.getBADFREEAVAILABLE()) || "1".equals(recording.getBADFREEAVAILABLE())) {
                parsedRecordings.add(recording);
            }
        }

        return sort(parsedRecordings);
    }

    private List<ParsedRecording> sort(List<ParsedRecording> recordings) {
        // recordings.sort((p1, p2) -> new BigDecimal(p1.getIDAYSLEFTBEFOREDELETE()).compareTo(new
        // BigDecimal(p2.getIDAYSLEFTBEFOREDELETE())));
        java.util.Collections.sort(recordings, new Comparator<ParsedRecording>() {

            @Override
            public int compare(ParsedRecording p1, ParsedRecording p2) {
                return new BigDecimal(p1.getIDAYSLEFTBEFOREDELETE()).compareTo(new BigDecimal(p2.getIDAYSLEFTBEFOREDELETE()));
            }
        });
        return recordings;
    }

}
