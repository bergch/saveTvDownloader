package de.da_bubu.savetvdownloader.renamer;

import static org.junit.Assert.*;
import java.io.IOException;
import org.junit.Test;
import de.da_bubu.savetvdownloader.main.Recording;
import de.da_bubu.savetvdownloader.savetvparser.ParsedRecording;


public class NameMatcherTest {

    @Test
    public void test() throws Exception {
        NameMatcher nameMatcher = new NameMatcher(new Repository());
        ParsedRecording parsedRecording = new ParsedRecording();
        parsedRecording.setFileName("Castle_Die_Maske_des_Moerders_FolgeS07E23_2016-02-09_0145_507698.mp4");
        Recording match = nameMatcher.searchMatch(parsedRecording);
        assertEquals("dasd", match.getName());
    }

}
