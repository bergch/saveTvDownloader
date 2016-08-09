package de.da_bubu.savetvdownloader.db;

import java.sql.Connection;
import java.sql.SQLException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class RecordingManager {


    protected static Connection conn;

    protected static final Log LOG = LogFactory.getLog(RecordingManager.class);

    /**
     * insert downloaded title to data base
     * 
     * @param title the title
     * @throws SQLException see {@link SQLException}
     */
    public abstract void insertTitle(String title) throws SQLException ;

    /**
     * check if title was already downloaded.
     * 
     * @param title the title
     * @return true if already downloaded
     * @throws SQLException see {@link SQLException} 
     */
    public abstract boolean alreadyDownloaded(String title) throws SQLException;
}
