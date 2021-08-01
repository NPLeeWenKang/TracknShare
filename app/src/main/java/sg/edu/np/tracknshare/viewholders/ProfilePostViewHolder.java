package sg.edu.np.tracknshare.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.tracknshare.R;

public class ProfilePostViewHolder extends RecyclerView.ViewHolder {
    public View View;
    public ImageView UserImg;
    public TextView Username;
    public TextView PostDate;
    public TextView Likes;
    public ImageView LikesIcon;
    public TextView PostCaption;
    public ImageView PostImg;
    public TextView IsLiked;
    public ProfilePostViewHolder(View itemView){
        super(itemView);
        View = itemView;
        Username = itemView.findViewById(R.id.username);
        PostDate = itemView.findViewById(R.id.workoutDate);
        Likes = itemView.findViewById(R.id.likes);
        LikesIcon = itemView.findViewById(R.id.likesImg);
        PostCaption = itemView.findViewById(R.id.postCaption);
        PostImg = itemView.findViewById(R.id.postImage);
        IsLiked = itemView.findViewById(R.id.isLiked);
    }
}
