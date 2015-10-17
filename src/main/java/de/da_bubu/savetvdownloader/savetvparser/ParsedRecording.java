package de.da_bubu.savetvdownloader.savetvparser;

public class ParsedRecording {

    public static final String[] ATTRIBUTES = { "DENDDATE", "IRECORDINGFORMATID", "SSTATUS", "DSTARTDATE",
            "IDAYSLEFTBEFOREDELETE", "BADFREEALOWED", "DSTARTDATEBUFFER", "SSUBTITLE", "ITVSTATIONID", "ITELECASTID",
            "SHDIMGURL", "DENDDATEBUFFER", "BISHIGHLIGHT", "STVSTATIONNAME", "BADFREEAVAILABLE", "SFOLGE",
            "ICOLLECTIVEROID", "STVSTATIONIMGURL", "BDOWNLOADADFREE", "STELECASTIMGURL", "DDELETEDATE", "STITLE",
            "SDETAILSURL" };

    String DENDDATE;

    String IRECORDINGFORMATID;

    String SSTATUS;

    String DSTARTDATE;

    String IDAYSLEFTBEFOREDELETE;

    String BADFREEALOWED;

    String DSTARTDATEBUFFER;

    String SSUBTITLE;

    String ITVSTATIONID;

    String ITELECASTID;

    String SHDIMGURL;

    String DENDDATEBUFFER;

    String BISHIGHLIGHT;

    String STVSTATIONNAME;

    String BADFREEAVAILABLE;

    String SFOLGE;

    String ICOLLECTIVEROID;

    String STVSTATIONIMGURL;

    String BDOWNLOADADFREE;

    String STELECASTIMGURL;

    String DDELETEDATE;

    String STITLE;

    String SDETAILSURL;

    private String downloadURL;

    private String series;

    private String fileName;

    public String getDENDDATE() {
        return DENDDATE;
    }

    public void setDENDDATE(String dENDDATE) {
        DENDDATE = dENDDATE;
    }

    public String getIRECORDINGFORMATID() {
        return IRECORDINGFORMATID;
    }

    public void setIRECORDINGFORMATID(String iRECORDINGFORMATID) {
        IRECORDINGFORMATID = iRECORDINGFORMATID;
    }

    public String getSSTATUS() {
        return SSTATUS;
    }

    public void setSSTATUS(String sSTATUS) {
        SSTATUS = sSTATUS;
    }

    public String getDSTARTDATE() {
        return DSTARTDATE;
    }

    public void setDSTARTDATE(String dSTARTDATE) {
        DSTARTDATE = dSTARTDATE;
    }

    public String getIDAYSLEFTBEFOREDELETE() {
        return IDAYSLEFTBEFOREDELETE;
    }

    public void setIDAYSLEFTBEFOREDELETE(String iDAYSLEFTBEFOREDELETE) {
        IDAYSLEFTBEFOREDELETE = iDAYSLEFTBEFOREDELETE;
    }

    public String getBADFREEALOWED() {
        return BADFREEALOWED;
    }

    public void setBADFREEALOWED(String bADFREEALOWED) {
        BADFREEALOWED = bADFREEALOWED;
    }

    public String getDSTARTDATEBUFFER() {
        return DSTARTDATEBUFFER;
    }

    public void setDSTARTDATEBUFFER(String dSTARTDATEBUFFER) {
        DSTARTDATEBUFFER = dSTARTDATEBUFFER;
    }

    public String getSSUBTITLE() {
        return SSUBTITLE;
    }

    public void setSSUBTITLE(String sSUBTITLE) {
        SSUBTITLE = sSUBTITLE;
    }

    public String getITVSTATIONID() {
        return ITVSTATIONID;
    }

    public void setITVSTATIONID(String iTVSTATIONID) {
        ITVSTATIONID = iTVSTATIONID;
    }

    public String getITELECASTID() {
        return ITELECASTID;
    }

    public void setITELECASTID(String iTELECASTID) {
        ITELECASTID = iTELECASTID;
    }

    public String getSHDIMGURL() {
        return SHDIMGURL;
    }

    public void setSHDIMGURL(String sHDIMGURL) {
        SHDIMGURL = sHDIMGURL;
    }

    public String getDENDDATEBUFFER() {
        return DENDDATEBUFFER;
    }

    public void setDENDDATEBUFFER(String dENDDATEBUFFER) {
        DENDDATEBUFFER = dENDDATEBUFFER;
    }

    public String getBISHIGHLIGHT() {
        return BISHIGHLIGHT;
    }

    public void setBISHIGHLIGHT(String bISHIGHLIGHT) {
        BISHIGHLIGHT = bISHIGHLIGHT;
    }

    public String getSTVSTATIONNAME() {
        return STVSTATIONNAME;
    }

    public void setSTVSTATIONNAME(String sTVSTATIONNAME) {
        STVSTATIONNAME = sTVSTATIONNAME;
    }

    public String getBADFREEAVAILABLE() {
        return BADFREEAVAILABLE;
    }

    public void setBADFREEAVAILABLE(String bADFREEAVAILABLE) {
        BADFREEAVAILABLE = bADFREEAVAILABLE;
    }

    public String getSFOLGE() {
        return SFOLGE;
    }

    public void setSFOLGE(String sFOLGE) {
        SFOLGE = sFOLGE;
    }

    public String getICOLLECTIVEROID() {
        return ICOLLECTIVEROID;
    }

    public void setICOLLECTIVEROID(String iCOLLECTIVEROID) {
        ICOLLECTIVEROID = iCOLLECTIVEROID;
    }

    public String getSTVSTATIONIMGURL() {
        return STVSTATIONIMGURL;
    }

    public void setSTVSTATIONIMGURL(String sTVSTATIONIMGURL) {
        STVSTATIONIMGURL = sTVSTATIONIMGURL;
    }

    public String getBDOWNLOADADFREE() {
        return BDOWNLOADADFREE;
    }

    public void setBDOWNLOADADFREE(String bDOWNLOADADFREE) {
        BDOWNLOADADFREE = bDOWNLOADADFREE;
    }

    public String getSTELECASTIMGURL() {
        return STELECASTIMGURL;
    }

    public void setSTELECASTIMGURL(String sTELECASTIMGURL) {
        STELECASTIMGURL = sTELECASTIMGURL;
    }

    public String getDDELETEDATE() {
        return DDELETEDATE;
    }

    public void setDDELETEDATE(String dDELETEDATE) {
        DDELETEDATE = dDELETEDATE;
    }

    public String getSTITLE() {
        return STITLE;
    }

    public void setSTITLE(String sTITLE) {
        STITLE = sTITLE;
    }

    public String getSDETAILSURL() {
        return SDETAILSURL;
    }

    public void setSDETAILSURL(String sDETAILSURL) {
        SDETAILSURL = sDETAILSURL;
    }

    @Override
    public String toString() {
        return "TelecastId:" + getITELECASTID() + "Title:" + getSTITLE() + "-" + getSSUBTITLE()  + " daystodel:" + getIDAYSLEFTBEFOREDELETE();
    }

    public void setDownloadURL(String downloadURL) {
        this.downloadURL = downloadURL;
    }

    public String getDownloadURL() {
        return downloadURL;
    }

    public void setSearies(String series) {
        this.series = series;

    }

    public String getSeries() {
        return series;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
    
   
}
