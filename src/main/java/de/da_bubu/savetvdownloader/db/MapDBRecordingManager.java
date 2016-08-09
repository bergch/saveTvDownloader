package de.da_bubu.savetvdownloader.db;

import java.sql.SQLException;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.IndexTreeList;

public class MapDBRecordingManager extends RecordingManager {

    IndexTreeList<Object> recordings = null;
    
    public MapDBRecordingManager() {
        DB db = DBMaker.fileDB("file.db").make();
        recordings = db.indexTreeList("recordings").make();
    }
    
    @Override
    public void insertTitle(String title) throws SQLException {
       recordings.add(title);
    }

    @Override
    public boolean alreadyDownloaded(String title) throws SQLException {
        return recordings.contains(title);
    }

}
