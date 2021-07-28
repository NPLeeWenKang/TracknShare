package sg.edu.np.tracknshare.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import sg.edu.np.tracknshare.R;

public class PostViewHolder extends RecyclerView.ViewHolder {
    public View View;
    public ImageView UserImg;
    public TextView Username;
    public TextView PostDate;
    public TextView IsLiked;
    public TextView Likes;
    public ImageView LikesIcon;
    public TextView PostCaption;
    public ImageView PostImg;
    public TextView ViewComments;
    public ImageView CommentsIcon;
    public ImageView ShareIcon;
    public PostViewHolder(View itemView){
        super(itemView);
        View = itemView;
        UserImg = itemView.findViewById(R.id.avatarIMG);
        Username = itemView.findViewById(R.id.username);
        PostDate = itemView.findViewById(R.id.workoutDate);
        Likes = itemView.findViewById(R.id.likes);
        LikesIcon = itemView.findViewById(R.id.likesImg);
        PostCaption = itemView.findViewById(R.id.postCaption);
        PostImg = itemView.findViewById(R.id.postImage);
        ViewComments = itemView.findViewById(R.id.viewComments);
        CommentsIcon = itemView.findViewById(R.id.commentsIcon);
        ShareIcon = itemView.findViewById(R.id.sharePost);
        IsLiked = itemView.findViewById(R.id.isLiked);
    }
}
