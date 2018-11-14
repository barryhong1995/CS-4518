package wpi.hphong.databasetutorial;

import android.content.ContentValues;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private LogEntryDbHelper mDBHelper;
    private SQLiteDatabase writeableDB;
    private SQLiteDatabase readableDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDBHelper = new LogEntryDbHelper(this);
    }

    public void addToDB(View v) {
        // Initialize variables
        writeableDB = mDBHelper.getWritableDatabase();
        ContentValues colVal = new ContentValues();
        TextView editText = (TextView) findViewById(R.id.inputText);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String timestamp  = dateFormat.format(new Date());

        // Put values into columns
        colVal.put("entry", editText.getText().toString());
        colVal.put("timeStamp", timestamp);
        writeableDB.insert("logEntries", null, colVal);

        // Clear text entry for next submit
        editText.setText("");

        // Show log count
        updateLogCount();
    }

    public void updateLogCount() {
        // Initialize variables
        readableDB = mDBHelper.getReadableDatabase();
        long entryCount = DatabaseUtils.queryNumEntries(readableDB, "logEntries");
        TextView outputText = (TextView) findViewById(R.id.outputText);
        outputText.setText("Number of Entries: " + entryCount);
    }
}
