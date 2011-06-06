package fi.tamk.anpro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HighscoreDBAdapter {

    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "Name";
    public static final String KEY_SCORE = "Score";
    public static final String KEY_SUBMISSIONDATE = "SubmissionDate";

    private static final String DATABASE_NAME = "gamespacecorehighscore.db";
    private static final String DATABASE_TABLE = "highscore";
    private static final int DATABASE_VERSION = 1;

    private static String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + DATABASE_TABLE
            + " ("
            + KEY_ID
            + " integer primary key autoincrement, "
            + KEY_NAME
            + " text not null, "
            + KEY_SCORE
            + " integer not null, "
            + KEY_SUBMISSIONDATE + " integer not null);";

    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public HighscoreDBAdapter(Context ctx) {
        this .context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super (context, DATABASE_NAME, null, DATABASE_VERSION);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }
    }

    //opens the database
    public HighscoreDBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this ;
    }

    //closes the database   
    public void close() {
        DBHelper.close();
    }

    //insert a record into the database
    public long insertRecord(Record r) {
        Long now = Long.valueOf(System.currentTimeMillis());
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, r.getPlayerName());
        initialValues.put(KEY_SCORE, r.getScore());
        initialValues.put(KEY_SUBMISSIONDATE, now);
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //deletes a particular record
    public boolean deleteRecord(int id) {
        return db.delete(DATABASE_TABLE, KEY_ID + "=" + id, null) > 0;
    }

    //retrieves all records
    public Cursor getAllRecords(HighScoreType type) {
        String orderby = "";
        switch (type) {
        case TIME:
            orderby = KEY_SCORE + " ASC";
            break;
        case POINTS:
            orderby = KEY_SCORE + " DESC";
            break;
        }

        Cursor c = db.query(DATABASE_TABLE, new String[] { KEY_ID,
                KEY_NAME, KEY_SCORE, KEY_SUBMISSIONDATE }, null, null,
                null, null, orderby);

        return c;
    }

    public Cursor getWorstRecord(HighScoreType type) {
        String orderby = "";
        switch (type) {
        case TIME:
            orderby = KEY_SCORE + " DESC";
            break;
        case POINTS:
            orderby = KEY_SCORE + " ASC";
            break;
        }
        Cursor c = db.query(DATABASE_TABLE, new String[] { KEY_ID,
                KEY_NAME, KEY_SCORE, KEY_SUBMISSIONDATE }, null, null,
                null, null, orderby);

        return c;
    }
}

