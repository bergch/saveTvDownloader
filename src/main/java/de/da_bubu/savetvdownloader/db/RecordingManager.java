package de.da_bubu.savetvdownloader.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class RecordingManager {


    protected static Connection conn;

    protected static final Log LOG = LogFactory.getLog(RecordingManager.class);

    protected static final String DB = "data/savetv";


    /**
     * insert downloaded title to data base
     * 
     * @param title the title
     * @throws SQLException see {@link SQLException}
     */
    public void insertTitle(String title) throws SQLException {
        Statement st = conn.createStatement();
        String sql = "INSERT INTO titles VALUES('"+title+"')";
        LOG.debug(sql);

        // execute the insert statement
        st.executeUpdate(sql);
        conn.commit();

        st.close();
        st = null;
        
        LOG.info("added "+title +" as downloaded");
    }

    /**
     * check if title was already downloaded.
     * 
     * @param title the title
     * @return true if already downloaded
     * @throws SQLException see {@link SQLException} 
     */
    public boolean alreadyDownloaded(String title) throws SQLException {
        boolean found = false;
        try {
            Statement st = conn.createStatement();
            String query = "SELECT * FROM titles WHERE title = '" + title + "'";
            ResultSet res = st.executeQuery(query);
            if (res.next()) {
                found = true;
            }
            res.close();
            st.close();

        } catch (SQLException sqlex) {
            LOG.error("Java exception " + sqlex.getMessage() + " was thrown with with SQL message "
                    + sqlex.getSQLState());
        }

        return found;
    }
}
