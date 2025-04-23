package cm40179g3.citywalker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class LeaderboardEntryAdapter extends RecyclerView.Adapter<LeaderboardEntryAdapter.MyViewHolder> {
    Context context;
    ArrayList<LeaderboardEntry> leaderboardEntries;

    public LeaderboardEntryAdapter(Context context, ArrayList<LeaderboardEntry> leaderboardEntries) {
        this.context = context;
        this.leaderboardEntries = leaderboardEntries;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.leaderboard_entry, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.ranking.setText(String.valueOf(leaderboardEntries.get(position).getUid()));
        holder.name.setText(leaderboardEntries.get(position).getDisplayName());
        holder.numSteps.setText(String.valueOf(leaderboardEntries.get(position).getNumSteps()));
    }

    @Override
    public int getItemCount() {
        return leaderboardEntries.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView ranking, name, numSteps;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ranking = itemView.findViewById(R.id.ranking);
            name = itemView.findViewById(R.id.name);
            numSteps = itemView.findViewById(R.id.steps);
        }
    }
}