package sg.edu.np.tracknshare.viewholders;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import sg.edu.np.tracknshare.R;

public class RunsViewHolder extends RecyclerView.ViewHolder {
    View view;
    public ImageView MapImage;
    public TextView Header_distance;
    public TextView Run_distance;
    public TextView Header_Date;
    public TextView Run_Date;
    public TextView Header_timing;
    public TextView Run_timing;
    public TextView Header_pace;
    public TextView Run_pace;
    public TextView Header_calories;
    public TextView Run_calories;
    public ImageView Post_Button;
    public RunsViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
        MapImage = itemView.findViewById(R.id.run_image);
        Header_distance = itemView.findViewById(R.id.header_distance);
        Run_distance = itemView.findViewById(R.id.run_distance);
        Header_Date = itemView.findViewById(R.id.header_date);
        Run_Date= itemView.findViewById(R.id.run_date);
        Header_timing = itemView.findViewById(R.id.header_timing);
        Run_timing = itemView.findViewById(R.id.run_timing);
        Header_pace = itemView.findViewById(R.id.header_pace);
        Run_pace = itemView.findViewById(R.id.run_pace);
        Header_calories = itemView.findViewById(R.id.header_calories);
        Run_calories = itemView.findViewById(R.id.run_calories);
        Post_Button = itemView.findViewById(R.id.post);
    }
}
