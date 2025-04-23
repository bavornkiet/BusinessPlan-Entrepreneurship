package cm40179g3.citywalker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LeaderboardFragment extends Fragment {

    ArrayList<LeaderboardEntry> leaderboardEntries = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setUpLeaderboard();
        View rootView = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        RecyclerView recyclerView = rootView.findViewById(R.id.leaderboard_recycler_view);

        LeaderboardEntryAdapter adapter = new LeaderboardEntryAdapter(requireContext(), leaderboardEntries);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    private void setUpLeaderboard() {
        leaderboardEntries.add(new LeaderboardEntry("1", "John Doe", 1000));
        leaderboardEntries.add(new LeaderboardEntry("2", "Jane Smith", 900));
        leaderboardEntries.add(new LeaderboardEntry("3", "Bob Johnson", 800));
        leaderboardEntries.add(new LeaderboardEntry("4", "Alice Brown", 700));
        leaderboardEntries.add(new LeaderboardEntry("5", "Charlie Davis", 600));
        leaderboardEntries.add(new LeaderboardEntry("6", "Eve Wilson", 500));
        leaderboardEntries.add(new LeaderboardEntry("7", "Frank Miller", 400));
        leaderboardEntries.add(new LeaderboardEntry("8", "Grace Lee", 300));
        leaderboardEntries.add(new LeaderboardEntry("9", "Hank Taylor", 200));
        leaderboardEntries.add(new LeaderboardEntry("10", "Ivy Anderson", 100));
    }
}