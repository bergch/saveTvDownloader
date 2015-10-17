package de.da_bubu.savetvdownloader.main;

public class Recording {

    private String name;

    private String series;

    private Integer daysToDeletion;

    private String URL;

    public String getName() {
        return name;
    }

    public Integer getDaysToDeletion() {
        return daysToDeletion;
    }

    public String getURL() {
        return URL;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDaysToDeletion(Integer daysToDeletion) {
        this.daysToDeletion = daysToDeletion;
    }

    public void setURL(String uRL) {
        URL = uRL;
    }

    public void setSeries(String stitle) {
        this.series = stitle;
    }

    public String getSeries() {
        return series;
    }
}
