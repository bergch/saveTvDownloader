package de.da_bubu.savetvdownloader.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import de.da_bubu.savetvdownloader.db.MapDBRecordingManager;
import de.da_bubu.savetvdownloader.renamer.NameMatcher;
import de.da_bubu.savetvdownloader.renamer.Repository;
import de.da_bubu.savetvdownloader.savetvparser.ParsedRecording;
import de.da_bubu.savetvdownloader.savetvparser.SaveTvEpisodeParser;

public class Main {

    public final static Log LOG = LogFactory.getLog(Main.class);

    private static final String numberOfRecordings = "300";

    private HttpClient client;

    private HttpEntity entity;

    private SaveTvEpisodeParser parser = new SaveTvEpisodeParser();

    private MapDBRecordingManager mysql = null;

    NameMatcher nameMatcher = new NameMatcher(new Repository());

    private String saveTvUser;

    private String saveTvPassword;

    public Main(String saveTvUser, String saveTvPassword) {
        this.saveTvUser = saveTvUser;
        this.saveTvPassword = saveTvPassword;
        mysql = new MapDBRecordingManager();
    }

    public static void main(String[] args) {

        Options options = new Options();
        options.addOption(CommandLineArguments.SAVETVPASSWORD, true, "save tv password");
        options.addOption(CommandLineArguments.SAVETVUSER, true, "save tv user");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e1) {
            throw new RuntimeException(e1);
        }

        String saveTvUser = cmd.getOptionValue(CommandLineArguments.SAVETVUSER);
        String saveTvPassword = cmd.getOptionValue(CommandLineArguments.SAVETVPASSWORD);

        Main main = new Main(saveTvUser, saveTvPassword);
        try {
            main.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void start() throws Exception {
        client = getNewHttpClient();
        try {

            loginToSaveTV(saveTvUser, saveTvPassword);
            String content = executeGet("https://www.save.tv/STV/M/obj/archive/JSON/VideoArchiveApi.cfm?iEntriesPerPage=" + numberOfRecordings
                    + "&iCurrentPage=1&iFilterType=1&sSearchString=&iTextSearchType=0&iChannelId=0&iTvCategoryId=0&iTvSubCategoryId=0&bShowNoFollower=false&iRecordingState=1&sSortOrder=StartDateDESC&iTvStationGroupId=0&iRecordAge=0&iDaytime=0");
            List<ParsedRecording> recordings = parser.parse(content);
            LOG.info("found " + recordings.size() + " adfree recordings");

            for (ParsedRecording recording : recordings) {
                recording.setDownloadURL(getDownloadURL(recording));
                NameMatcher matcher = new NameMatcher(new Repository());
                Recording match = matcher.searchMatch(recording);
                if (match != null) {
                    recording.setSearies(match.getSeries());
                    recording.setFileName(match.getName() + ".mp4");
                    LOG.info("matched to:" + recording.getFileName());
                } else {
                    recording.setFileName(getOrigName(recording));
                    LOG.info("no mapping found");
                }

                if (mysql.alreadyDownloaded(recording.getFileName())) {
                    LOG.info("recording skipped:" + recording.getFileName());
                    deleteRecording(recording);
                    continue;
                }

                startdownload(recording);
            }
        } catch (IOException | SQLException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            LOG.error("should not happen", e);
            e.printStackTrace();
            throw e;
        }
    }

    private String getOrigName(ParsedRecording recording) throws ClientProtocolException, IOException {
        HttpGet get = new HttpGet(recording.getDownloadURL());
        get.getParams().setParameter("http.socket.timeout", new Integer(30000));
        get.addHeader("Range", "bytes=0-");
        HttpResponse response = client.execute(get);

        return response.getHeaders("Content-Disposition")[0].getValue().split("=")[1];
    }

    private void startdownload(ParsedRecording recording) throws IOException, SQLException {
        // TODO add sub dir for series
        File f = new File("./downloads/.tmp/" + recording.getFileName());
        createParentDirs(f);

        LOG.info("Start download " + recording);
        try {
            FileUtils.copyURLToFile(new URL(recording.getDownloadURL()), f);
        } catch (MalformedURLException e) {
            LOG.error("brocken URL:" + recording.getDownloadURL());
            throw e;
        }

        LOG.info("added as already downloaded:" + recording.getFileName());

        mysql.insertTitle(recording.getFileName());

        finishFile(recording);
        deleteRecording(recording);
    }

    private void finishFile(ParsedRecording recording) {
        try {
            File src = new File("downloads/.tmp/" + recording.getFileName());
            File dest = new File("downloads/" + recording.getFileName());

            createParentDirs(dest);
            if (!src.renameTo(dest))
                LOG.debug("The file " + " was moved successfully to: ..");
        } catch (Exception e) {
            LOG.error("Could not move file " + recording.getFileName() + "from temporary directory to download directory. The error was:"
                    + e.getMessage());
        }

    }

    private void createParentDirs(File f) {
        File parentDir = f.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }
    }

    private void deleteRecording(ParsedRecording recording) throws IOException {
        LOG.info("recording deleted:" + recording);
        executeGet("https://www.save.tv/STV/M/obj/cRecordOrder/croDelete.cfm?TelecastID=" + recording.getITELECASTID());

    }

    /**
     * Makes a request to Save.TV to get the download URL for a recording that was previously found in the HTTP Website.
     * 
     * @param recording
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     * @throws InterruptedException
     */
    private String getDownloadURL(ParsedRecording recording) throws IOException, InterruptedException {
        LOG.info("get download URL for " + recording);
        boolean retry = true;
        String response = "";
        int count = 1;
        while (retry) {
            response = executeGet("https://www.save.tv/STV/M/obj/cRecordOrder/croGetDownloadUrl2.cfm?TelecastId=" + recording.getITELECASTID()
                    + "&iFormat=" + "5" + "&bAdFree=" + "1");
            if (response.contains("500 - Internal server error")) {
                if (count > 10) {
                    throw new RuntimeException("too many retries!");
                }
                Thread.sleep(1000L);
                LOG.error("internal server errror detected - sleep 1s and retry #" + count++);
            } else {
                retry = false;
            }
        }
        JsonParser jsonParser = new JsonParser();
        JsonElement root = jsonParser.parse(response);

        return root.getAsJsonObject().get("DOWNLOADURL").getAsString();

    }

    private String executeGet(String uri) throws IOException {
        LOG.info("exec:" + uri);
        StringBuffer sb = new StringBuffer();
        HttpGet get = new HttpGet(uri);
        this.entity = null;

        HttpResponse res = client.execute(get);
        this.entity = res.getEntity();

        if (this.entity != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), Charset.forName("UTF-8")));
            String inputLine;

            while ((inputLine = reader.readLine()) != null) {
                sb.append(inputLine);
            }
            reader.close();

        }
        return sb.toString();

    }

    private HttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
            HttpProtocolParams.setContentCharset(params, HTTP.CONTENT_ENCODING);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

    private boolean loginToSaveTV(String username, String password) throws ClientProtocolException, IOException, InterruptedException {

        boolean logon_succeeded = true;

        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("sUsername", username));
        formparams.add(new BasicNameValuePair("sPassword", password));
        UrlEncodedFormEntity ent = new UrlEncodedFormEntity(formparams, "UTF-8");
        HttpPost httppost = new HttpPost("https://www.save.tv/STV/M/Index.cfm?sk=PREMIUM");
        httppost.setEntity(ent);

        HttpResponse response = client.execute(httppost);
        entity = null;
        entity = response.getEntity();
        Header[] h = response.getHeaders("location");
        if (h != null) {
            String logon = h[0].getValue().toLowerCase();
            int pos = logon.indexOf("=errorcodeid_49");
            if (pos > 0)
                logon_succeeded = false;
        }

        if (entity != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent()));
            String line = reader.readLine();
            StringBuffer buff = new StringBuffer();
            while (line != null) {
                buff.append(line);
                line = reader.readLine();
            }
            reader.close();
            // LOG.info(buff.toString());
        }
        Thread.sleep(1000L);
        return logon_succeeded;

    }

}
