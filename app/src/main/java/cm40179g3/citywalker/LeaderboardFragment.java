package cm40179g3.citywalker;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class LeaderboardFragment extends Fragment {

    private final List<LeaderboardEntry> entries;
    private View view;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private LeaderboardEntryAdapter adapter;
    private TextView txtRank;

    public LeaderboardFragment() {
        entries = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        adapter = new LeaderboardEntryAdapter(view.getContext(), entries);
        txtRank = view.findViewById(R.id.leaderboard_txtRank);
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        ListView listView = view.findViewById(R.id.leaderboard_listView);
        listView.setAdapter(adapter);

        refreshLeaderboard();

        Button btnRefresh = view.findViewById(R.id.leaderboard_btnRefresh);
        btnRefresh.setOnClickListener(v -> {
            refreshLeaderboard();
        });

        return view;
    }

    private void refreshLeaderboard() {
        Toast toast = Toast.makeText(view.getContext(), "Loading leaderboard...", Toast.LENGTH_SHORT);
        toast.show();

        entries.clear();
        db.collection("Users").get().addOnCompleteListener(task -> {
            List<DocumentSnapshot> docs = task.getResult().getDocuments();
            for (DocumentSnapshot doc : docs) {
                String  uid         = doc.getId();
                String  displayName = doc.get("displayName", String.class);
                Integer steps       = doc.get("steps", Integer.class);

                if (steps == null) {
                    steps = 0;
                    Log.w("LeaderboardFragment", "Document has no 'steps' field");
                }
                entries.add(new LeaderboardEntry(uid, displayName, steps));
            }
            entries.sort(Comparator.comparingInt(LeaderboardEntry::getNumSteps));
            Collections.reverse(entries);
            for (int i = 0; i < entries.size(); i++) {
                LeaderboardEntry entry = entries.get(i);
                if (user.getUid().equals(entry.getUid())) {
                    txtRank.setText(String.format(Locale.getDefault(), "%d", 1 + i));
                    break;
                }
            }
            adapter.notifyDataSetChanged();
            toast.cancel();
            Toast.makeText(view.getContext(), "Leaderboard updated!", Toast.LENGTH_SHORT).show();
        });
    }

}
