package sg.edu.np.tracknshare.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.tracknshare.R;

public class StatsViewHolder extends RecyclerView.ViewHolder {
    public View view;
    public CardView cardView;
    public ImageView cardIcon;
    public TextView Title;
    public TextView stat1number;
    public TextView stat2number;
    public TextView stat1header;
    public TextView stat2header;

    public StatsViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
        cardView = view.findViewById(R.id.cardView);
        cardIcon = view.findViewById(R.id.cardIcon);
        Title = view.findViewById(R.id.Title);
        stat1header = view.findViewById(R.id.stat1_header);
        stat2header = view.findViewById(R.id.stat2_header);
        stat1number = view.findViewById(R.id.stat1_number);
        stat2number = view.findViewById(R.id.stat2_number);
    }

}
