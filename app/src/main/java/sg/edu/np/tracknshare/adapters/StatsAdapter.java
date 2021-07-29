package sg.edu.np.tracknshare.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull StatsViewHolder holder, int position) {
        Statistics stat = data.get(position);
        holder.Title.setText(stat.Title);
        holder.stat1number.setText(""+stat.Stat1number);
        holder.stat2number.setText(""+stat.Stat2number);
        holder.stat1header.setText(stat.Stat1Header);
        holder.stat2header.setText(stat.Stat2Header);
        int color; //initialise switch case variables
        Drawable icon;
        switch(position){ //generate 3 random colors for cardViews
            // and set the respective icons
            case 0:
                color = context.getResources().getColor(R.color.fossil);
                holder.cardView.setBackgroundColor(color);
                icon = context.getResources().getDrawable(R.drawable.ic_foot);//steps avatar
                holder.cardIcon.setImageDrawable(icon);
                break;
            case 1:
                color = context.getResources().getColor(R.color.lime);
                holder.cardView.setBackgroundColor(color);
                icon = context.getResources().getDrawable(R.drawable.ic_calories);//kcal symbol
                holder.cardIcon.setImageDrawable(icon);

                break;
            case 2:
                color = context.getResources().getColor(R.color.primaryLightColor);
                holder.cardView.setBackgroundColor(color);
                icon = context.getResources().getDrawable(R.drawable.ic_outline_pin_drop_24);//pin symbol
                holder.cardIcon.setImageDrawable(icon);

                break;


        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
