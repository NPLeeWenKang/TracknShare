package sg.edu.np.tracknshare.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.tracknshare.R;

public class RunsViewHolder extends RecyclerView.ViewHolder {
    View view;
    public ImageView MapImage;
    public TextView Header_distance;
    public TextView Run_distance;
    public ImageView Header_Date;
    public TextView Run_Date;
    public TextView Header_timing;
    public TextView Run_timing;
    public TextView Header_pace;
    public TextView Run_pace;
    public TextView Header_calories;
    public TextView Run_calories;
    public TextView Run_steps;
    public TextView Header_steps;
    public ImageView Share_button;
    public ImageView post_button;
    public ConstraintLayout postConst;
    public ConstraintLayout shareConst;
    public RunsViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
        MapImage = itemView.findViewById(R.id.run_image);
        Header_distance = itemView.findViewById(R.id.header_distance);
        Run_distance = itemView.findViewById(R.id.run_distance);
        Header_Date = itemView.findViewById(R.id.date_icon);
        Run_Date= itemView.findViewById(R.id.run_date);
        Header_timing = itemView.findViewById(R.id.header_timing);
        Run_timing = itemView.findViewById(R.id.run_timing_post);
        Header_pace = itemView.findViewById(R.id.header_pace);
        Run_pace = itemView.findViewById(R.id.run_pace);
        Header_calories = itemView.findViewById(R.id.header_calories);
        Run_calories = itemView.findViewById(R.id.run_calories);
        Share_button = itemView.findViewById(R.id.share);
        post_button = itemView.findViewById(R.id.post_logo);
        Header_steps = itemView.findViewById(R.id.steps_header);
        Run_steps = itemView.findViewById(R.id.run_steps);
        postConst = itemView.findViewById(R.id.postConst);
        shareConst = itemView.findViewById(R.id.shareConst);
    }
}
