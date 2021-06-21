package sg.edu.np.tracknshare.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sg.edu.np.tracknshare.R;
import sg.edu.np.tracknshare.models.Comment;
import sg.edu.np.tracknshare.viewholders.CommentViewHolder;

public class CommentsAdapter extends RecyclerView.Adapter<CommentViewHolder> {
    Context context;
    ArrayList<Comment> comments;
    public CommentsAdapter(Context c,ArrayList<Comment> data){
        context = c;
        comments = data;
    }
    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.vh_comments,parent,false);

        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment cmt = comments.get(position);
        holder.CommentUserImg.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_avatar));
        holder.CommentUsername.setText(cmt.getUserName());
        holder.CommentDate.setText(cmt.getDate());
        holder.CommentText.setText(cmt.getCommentText());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }
}
