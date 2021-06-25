package sg.edu.np.tracknshare.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sg.edu.np.tracknshare.R;
import sg.edu.np.tracknshare.models.Runs;
import sg.edu.np.tracknshare.viewholders.RunsViewHolder;

public class RunsAdapter extends RecyclerView.Adapter<RunsViewHolder> {
    Context context;
    ArrayList<Runs> runs;
    public RunsAdapter(Context c,ArrayList<Runs> runsArrayList){
        context = c;
        runs = runsArrayList;
    }
    @NonNull
    @Override
    public RunsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.vh_runs,parent,false);
        return new RunsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RunsViewHolder holder, int position) {
//        Drawable post_icon = context.getResources().getDrawable(R.drawable.writing);//post icon for every run
//        Runs r = runs.get(position);
//        holder.MapImage.setImageResource(R.drawable.running_splash);
//        holder.Run_Date.setText(r.getRunDate());
//        holder.Run_distance.setText(""+r.getRunDistance());
//        holder.Run_calories.setText(""+r.getRunCalories());
//        holder.Run_timing.setText(""+r.getRunDuration());
//        holder.Run_pace.setText(String.format("%,.2f",r.getRunPace()));
//        holder.Post_Button.setImageDrawable(post_icon);
    }

    @Override
    public int getItemCount() {
        return runs.size();
    }
}
