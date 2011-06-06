        /* 
         * Class name: HighScores *
         * 
         * Version info: v4.0 *
         * 
         * Description: 
         * 
         * Copyright by Group4 from CmpE450 course *
         */

        package fi.tamk.anpro;

        import java.util.ArrayList;

        import android.app.Activity;
        import android.app.ListActivity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.view.KeyEvent;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.view.View.OnClickListener;
        import android.widget.ArrayAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;

        import fi.tamk.anpro.HighScore;
        import fi.tamk.anpro.R;
        import fi.tamk.anpro.Record;
        import fi.tamk.anpro.HighScoreType;

        public class Highscores extends ListActivity {

            //keeps the score of the user
            public static int score = 0;
            //keeps the user name and its default value is "noname"
            public static String user = "noname";
            //these keep the highest scores and their owners
            public static int[] highestScores = new int[11];
            public static String[] highestNames = new String[11];
            //true if the score is a high score. Otherwise false
            public static boolean newScore = false;
            public HighScore hs = new HighScore(this , HighScoreType.POINTS, 10);

            ArrayList<Record> records;

            //constructor
            public void HighScores() {

            }

            // The initialization of the high score screen 
            @Override
            public void onCreate(Bundle savedInstanceState) {
                super .onCreate(savedInstanceState);
                setContentView(R.layout.highscorelist);

                Record r;

                //if score deserve to enter the high score list, the records name and score will be set
                if (newScore) {
                    r = new Record();
                    r.setPlayerName(user);
                    r.setScore(score);
                    hs.insertRecord(r);
                    newScore = false;
                }

                records = hs.getRecords();
                setListAdapter(new ScoreAdapter(this ));

                ImageView home = (ImageView) findViewById(R.id.goHome);
                //click event handler for home button
                home.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        //class the game menu screen
                        Intent GotoMenuIntent = new Intent(Highscores.this ,
                                MainActivity.class);
                        startActivity(GotoMenuIntent);

                    }
                });

                ImageView reset = (ImageView) findViewById(R.id.resetHighScore);
                //click event handler for reset button
                reset.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        //deletes all high score saved so far
                        hs.DeleteAll();
                        records = hs.getRecords();
                        setListAdapter(new ScoreAdapter(Highscores.this ));
                    }
                });

            }

            //score adapter to handle score retrieval
            class ScoreAdapter extends ArrayAdapter<Record> {

                Activity context;

                ScoreAdapter(Activity context) {
                    super (context, R.layout.highscore_row, records);
                    this .context = context;
                }

                //returns all highest scores as a view which shows user and his or her score
                public View getView(int position, View convertView,
                        ViewGroup parent) {
                    LayoutInflater inflater = context.getLayoutInflater();
                    View row = inflater.inflate(R.layout.highscore_row, null);

                    TextView player = (TextView) row.findViewById(R.id.player);
                    player.setText(records.get(position).getPlayerName());
                    TextView score = (TextView) row.findViewById(R.id.score);
                    score.setText(String.valueOf(records.get(position)
                            .getScore()));
                    return (row);
                }
            }

            //overrideen method to handle keyboard events which hooks key down  event and disables back button
            @Override
            public boolean onKeyDown(int keyCode, KeyEvent event) {
                //Log.d(logtag, "onKeyDown: keycode=" + keyCode + ", event=" + event);
                switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:

                    break;

                }
                return true;
            }
        }
