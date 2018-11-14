package wpi.hphong.databasetutorial;

import android.provider.BaseColumns;

public class LogContract {
    // Constructor
    private LogContract() {}

    protected static class LogEntry implements BaseColumns {
        public static final String TABLE_NAME = "logEntries";
        public static final String COLUMN_NAME_ENTRY = "entry";
        public static final String COLUMN_NAME_TIMESTAMP = "timeStamp";
    }

    protected static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + LogEntry.TABLE_NAME + " (" +
                    LogEntry._ID + " INTEGER PRIMARY KEY," +
                    LogEntry.COLUMN_NAME_ENTRY + " TEXT," +
                    LogEntry.COLUMN_NAME_TIMESTAMP + " TEXT)";

    protected static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + LogEntry.TABLE_NAME;
}
