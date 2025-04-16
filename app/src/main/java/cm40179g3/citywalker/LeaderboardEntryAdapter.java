package cm40179g3.citywalker;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class LeaderboardEntryAdapter extends BaseAdapter {

    private final Context context;
    private final List<LeaderboardEntry> entries;

    public LeaderboardEntryAdapter(Context context, List<LeaderboardEntry> entries) {
        this.context = context;
        this.entries = entries;
    }

    @Override
    public int getCount() {
        return entries.size();
    }

    @Override
    public Object getItem(int position) {
        return entries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_leaderboard_entry, parent, false);
        }

        LeaderboardEntry entry = entries.get(position);

        TextView txtRank     = convertView.findViewById(R.id.leaderboard_entry_txtRank);
        TextView txtName     = convertView.findViewById(R.id.leaderboard_entry_txtName);
        TextView txtNumSteps = convertView.findViewById(R.id.leaderboard_entry_txtNumSteps);

        txtRank.setText(String.format(Locale.getDefault(), "%d", 1 + position));
        txtName.setText(entry.getDisplayName());
        txtNumSteps.setText(String.format(Locale.getDefault(), "%d steps", entry.getNumSteps()));

        return convertView;
    }

}
