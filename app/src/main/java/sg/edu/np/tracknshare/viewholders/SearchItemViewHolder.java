package sg.edu.np.tracknshare.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.tracknshare.R;

public class SearchItemViewHolder extends RecyclerView.ViewHolder {
    public View view;
    public TextView username;
    public TextView isFollowed;
    public ImageView image;
    public SearchItemViewHolder(@Nullable View itemView) {
        super(itemView);
        view = itemView;
        username = itemView.findViewById(R.id.username);
        image = itemView.findViewById(R.id.searchitem_image);
    }
}
