package de.da_bubu.savetvdownloader.db;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MySQLRecordingManager extends RecordingManager {
	
	public  static  String DB = null;
	public static  String DBHOST =  null;
	public static  String DBPORT= null;
	public static  String DBUSER = null;
	public static  String DBPASSWORD = null;
	
	private static final Log LOG = LogFactory.getLog(MySQLRecordingManager.class);
	
	
    /**
     * The class for using MySQL as a storage for all recorded software is not yet implemented
     * right now the download tool only supports HSQLDB as an internal database that only
     * one process at the time can connect to.
     */
	public MySQLRecordingManager() {
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
		} catch (ClassNotFoundException ex){
			LOG.error("JDBC driver is not in class path!");
		}
		
		if(isNotNull(DB, DBHOST, DBPASSWORD, DBPORT, DBUSER)) {
		    LOG.error("Not all DB Parameters are set");
		}
		
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + DBHOST + ":" + DBPORT + "/" + DB, DBUSER, DBPASSWORD);
			conn.setAutoCommit(false);
	        
		} catch (SQLException sqlx){
			LOG.fatal("Fatal error when trying to read local data. The message is " + sqlx.getMessage());
			System.exit(-1);
		}
		
	}
	
	private boolean isNotNull(String... strings) {
        for (String string : strings) {
            if(string == null ) {
                return true;
            }
        }
        return false;
    }

    public boolean initialize(){
	    try {
	    	LOG.info("Inititializing the database");
	    	Statement st = conn.createStatement();
	    	st.executeUpdate("CREATE TABLE titles ( title VARCHAR(255), PRIMARY KEY(title))");
	    	st.close();
	    	st = null;

	    	conn.commit(); 
	    } catch (SQLException sqlx){
	    	sqlx.printStackTrace();
	        return false;
	    }
	    LOG.trace("Initialization of the database is complete");
	    return true;
	}
	
    @Override
    public void insertTitle(String title) throws SQLException {
        Statement st = conn.createStatement();
        String sql="INSERT INTO titles VALUES('"+title+"');";
        
        // execute the insert statement
        st.executeUpdate(sql);
        st.close();
        st = null;
        
        conn.commit();
        
    }

    @Override
    public boolean alreadyDownloaded(String title) throws SQLException {
        boolean ex = false;
        try {
            Statement st = conn.createStatement();
            String query = "SELECT * FROM titles WHERE title = '" + title + "';";
            ResultSet res = st.executeQuery(query);
            if(res.next()){
                ex= true;
            } else {
                ex=false;
            }
            res.close();
            st.close();
            res = null;
            st = null;

            return ex;   
        } catch (SQLException sqlex){
            LOG.error("Java exception " + sqlex.getMessage() + " was thrown with with SQL message " + sqlex.getSQLState());
            return false;
        }
    }
	
}