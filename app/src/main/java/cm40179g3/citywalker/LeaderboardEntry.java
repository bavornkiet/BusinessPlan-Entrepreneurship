package cm40179g3.citywalker;

public class LeaderboardEntry {

    private String uid;
    private String displayName;
    private int numSteps;

    public LeaderboardEntry(String uid, String displayName, int numSteps) {
        this.uid = uid;
        this.displayName = displayName;
        this.numSteps = numSteps;
    }

    public String getUid() {
        return uid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getNumSteps() {
        return numSteps;
    }

}
