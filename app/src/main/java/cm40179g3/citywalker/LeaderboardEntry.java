package cm40179g3.citywalker;

public class LeaderboardEntry {

    private String displayName;
    private int numSteps;

    public LeaderboardEntry(String displayName, int numSteps) {
        this.displayName = displayName;
        this.numSteps = numSteps;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getNumSteps() {
        return numSteps;
    }

}
