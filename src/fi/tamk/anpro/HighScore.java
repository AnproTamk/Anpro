package fi.tamk.anpro;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;

public class HighScore {

    HighscoreDBAdapter mydb;
    HighScoreType type;
    int numOfRecords;

    public HighScoreType GetHighScoreType() {
        return this .type;
    }

    public void SetHighScoreType(HighScoreType t) {
        this .type = t;
    }

    public int GetNumOfRecords() {
        return this .numOfRecords;
    }

    public void SetNumOfRecords(int number) {
        this .numOfRecords = number;
    }

    public HighScore(Context ctx, int numberOfRecords) {
        mydb = new HighscoreDBAdapter(ctx);
        this .type = HighScoreType.POINTS;
        this .numOfRecords = numberOfRecords;
    }

    public HighScore(Context ctx, HighScoreType t, int numberOfRecords) {
        mydb = new HighscoreDBAdapter(ctx);
        this .type = t;
        this .numOfRecords = numberOfRecords;
    }

    public long insertRecord(Record r) {
        try {
            long returnvalue;
            if (r.getPlayerName().equals("")
                    || r.getPlayerName() == null) {
                return -1;
            } else {
                mydb.open();
                Cursor c = mydb.getAllRecords(type);
                if (c.getCount() < numOfRecords) {
                    returnvalue = mydb.insertRecord(r);
                } else {
                    Cursor cw = mydb.getWorstRecord(type);
                    if (cw.moveToFirst()) {
                        int worstId = Integer.parseInt(cw.getString(0));
                        int worstScore = Integer.parseInt(cw
                                .getString(2));
                        if (this .type == HighScoreType.POINTS) {
                            if (worstScore < r.getScore()) {
                                mydb.deleteRecord(worstId);
                                returnvalue = mydb.insertRecord(r);
                            } else
                                returnvalue = -1; //score is not high enough
                        } else {
                            if (worstScore > r.getScore()) {
                                mydb.deleteRecord(worstId);
                                returnvalue = mydb.insertRecord(r);
                            } else
                                returnvalue = -1; //score is not high enough
                        }

                    } else
                        returnvalue = -1;//an error occurred
                    mydb.close();
                }
            }
            return returnvalue;
        } catch (Exception obj) {
            return -1;
        }
    }

    public ArrayList<Record> getRecords() {

        ArrayList<Record> records;
        mydb.open();
        Cursor c = mydb.getAllRecords(type);
        records = new ArrayList<Record>();
        while (c.moveToNext()) {
            Record r = new Record();
            r.setPlayerName(c.getString(1));
            r.setScore(Integer.parseInt(c.getString(2)));
            records.add(r);
        }
        mydb.close();
        return records;
    }

    //deletes all records
    public boolean DeleteAll() {
        boolean returnvalue = true;
        try {
            mydb.open();
            Cursor c = mydb.getAllRecords(type);
            while (c.moveToNext() && returnvalue == true) {
                returnvalue = mydb.deleteRecord(Integer.parseInt(c
                        .getString(0)));
            }
            mydb.close();
            return returnvalue;
        } catch (Exception obj) {
            return false;
        }
    }

    //returns true if score is better than the worst score in the highscore table
    //returns false otherwise
    public boolean isBetterScore(int score) {

        boolean returnvalue;
        if (score <= 0)
            return false;
        else {
            mydb.open();
            Cursor cw = mydb.getWorstRecord(type);

            if (cw.moveToFirst()) {
                int worstScore = Integer.parseInt(cw.getString(2));
                if (score < worstScore) {
                    if (this .type == HighScoreType.TIME)
                        returnvalue = true;
                    else
                        returnvalue = false;
                } else {
                    if (this .type == HighScoreType.TIME)
                        returnvalue = false;
                    else
                        returnvalue = true;
                }
            } else
                returnvalue = true;
            mydb.close();
        }
        return returnvalue;
    }
}
