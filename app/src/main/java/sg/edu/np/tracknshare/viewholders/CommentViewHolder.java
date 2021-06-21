package sg.edu.np.tracknshare.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import sg.edu.np.tracknshare.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CommentViewHolder extends RecyclerView.ViewHolder{
    public View view;
    public ImageView CommentUserImg;
    public TextView CommentUsername;
    public TextView CommentDate;
    public TextView CommentText;
    public CommentViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
        CommentUserImg = itemView.findViewById(R.id.comment_user_image);
        CommentUsername = itemView.findViewById(R.id.comment_user);
        CommentDate = itemView.findViewById(R.id.comment_date);
        CommentText = itemView.findViewById(R.id.comment_text);
    }
}
