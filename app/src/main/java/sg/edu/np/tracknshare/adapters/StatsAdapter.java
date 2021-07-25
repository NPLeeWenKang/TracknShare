package sg.edu.np.tracknshare.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import sg.edu.np.tracknshare.R;
import sg.edu.np.tracknshare.models.Statistics;
import sg.edu.np.tracknshare.viewholders.StatsViewHolder;

//this adapter is for the individual statistics viewholder not the statistics tab element
public class StatsAdapter extends RecyclerView.Adapter<StatsViewHolder> {
    Context context;
    ArrayList<Statistics> data;
    public StatsAdapter(Context c,ArrayList<Statistics>statisticsList) {
        context = c;
        data = statisticsList;
    }

    @NonNull
    @Override
    public StatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.vh_stats,parent,false);
        return new StatsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatsViewHolder holder, int position) {
        Statistics stat = data.get(position);
        holder.Title.setText(stat.Title);
        holder.stat1number.setText(""+stat.Stat1number);
        holder.stat2number.setText(""+stat.Stat2number);
        holder.stat1header.setText(stat.Stat1Header);
        holder.stat2header.setText(stat.Stat2Header);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
