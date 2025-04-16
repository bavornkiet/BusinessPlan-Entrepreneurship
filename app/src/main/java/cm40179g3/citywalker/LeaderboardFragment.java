package cm40179g3.citywalker;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardFragment extends Fragment {

    private final List<LeaderboardEntry> entries;
    private FirebaseFirestore db;

    public LeaderboardFragment() {
        entries = new ArrayList<>();
        entries.add(new LeaderboardEntry("John Doe", 69));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        db = FirebaseFirestore.getInstance();

        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        ListView listView = view.findViewById(R.id.leaderboard_listView);
        LeaderboardEntryAdapter adapter = new LeaderboardEntryAdapter(view.getContext(), entries);
        listView.setAdapter(adapter);

        Button btnRefresh = view.findViewById(R.id.leaderboard_btnRefresh);
        btnRefresh.setOnClickListener(v -> {

        });

        return view;
    }
}
