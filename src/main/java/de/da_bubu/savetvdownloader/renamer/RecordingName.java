package de.da_bubu.savetvdownloader.renamer;

public class RecordingName {
    private final String name;

    public RecordingName(String name) {
        this.name = name;
    }

    public String chopDate() {
        return name.split("\\d{4}-\\d{2}-\\d{2}")[0];
    }
}
