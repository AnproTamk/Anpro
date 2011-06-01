package fi.tamk.anpro;

public class Record {
    private String playerName;
    private int score;

    public Record() {

    }

    public Record(String name, int newscore) {
        this .playerName = name;
        this .score = newscore;
    }

    public String getPlayerName() {
        return this .playerName;
    }

    public void setPlayerName(String name) {
        this .playerName = name;
    }

    public int getScore() {
        return this .score;
    }

    public void setScore(int newscore) {
        this.score = newscore;
    }
}
