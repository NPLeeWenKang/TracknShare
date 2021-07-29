package sg.edu.np.tracknshare.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import sg.edu.np.tracknshare.CommentsActivity;
import sg.edu.np.tracknshare.R;
import sg.edu.np.tracknshare.UserDetailActivity;
import sg.edu.np.tracknshare.handlers.AuthHandler;
import sg.edu.np.tracknshare.handlers.PostDBHandler;
import sg.edu.np.tracknshare.handlers.StorageHandler;
import sg.edu.np.tracknshare.handlers.UserDBHandler;
import sg.edu.np.tracknshare.models.Post;
import sg.edu.np.tracknshare.models.User;
import sg.edu.np.tracknshare.models.UserPostViewModel;
import sg.edu.np.tracknshare.viewholders.PostViewHolder;

public class PostsAdapter extends RecyclerView.Adapter<PostViewHolder> {
    Context context ;
    ArrayList<UserPostViewModel> data;
    public PostsAdapter(Context c,ArrayList<UserPostViewModel> up ){
        context = c;
        data = up;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.vh_posts,parent,false);

        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post p = data.get(position).getPost();
        User u = data.get(position).getUser();
        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy h:mma");
        holder.Username.setText(u.getUserName());
        holder.PostDate.setText(dateFormat.format(p.getPostDate()));
        holder.Likes.setText(""+p.getLikes());
        holder.PostCaption.setText(p.getCaption());
        holder.UserImg.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_avatar));
        holder.CommentsIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_comment));
        holder.LikesIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_heart));

        StorageHandler storageHandler = new StorageHandler();
        storageHandler.LoadFileToApp(p.getRunId(), context, holder.PostImg);
        storageHandler.LoadProfileImageToApp(u.getId(), context, holder.UserImg);

        PostDBHandler postDBHandler = new PostDBHandler(context);
        postDBHandler.isLiked(holder.LikesIcon, holder.IsLiked, p.getPostId());

        holder.View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentsActivity.class);
                intent.putExtra("postId", p.getPostId());
                intent.putExtra("runId", p.getRunId());
                intent.putExtra("userId", p.getUserId());
                ((Activity) context).startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.start_enter, R.anim.start_exit);
            }
        });
        holder.UserImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), UserDetailActivity.class);
                intent.putExtra("id", ""+u.getId());
                ((Activity) view.getContext()).startActivity(intent);
                ((Activity) view.getContext()).overridePendingTransition(R.anim.start_enter, R.anim.start_exit);
            }
        });
        holder.Username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), UserDetailActivity.class);
                intent.putExtra("id", ""+u.getId());
                ((Activity) view.getContext()).startActivity(intent);
                ((Activity) view.getContext()).overridePendingTransition(R.anim.start_enter, R.anim.start_exit);
            }
        });
        holder.PostDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), UserDetailActivity.class);
                intent.putExtra("id", ""+u.getId());
                ((Activity) view.getContext()).startActivity(intent);
                ((Activity) view.getContext()).overridePendingTransition(R.anim.start_enter, R.anim.start_exit);
            }
        });
        int dBLikes = p.getLikes();
        holder.LikesIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (holder.IsLiked.getText().toString().equals("0"))
                {
                    addOneLike(holder, p.getPostId());
                }else{
                    removeOneLike(holder, p.getPostId());
                }
            }


        });
    }
    public void addOneLike(PostViewHolder holder, String postId){
        holder.Likes.setText(""+(Integer.parseInt(holder.Likes.getText().toString())+1));
        holder.LikesIcon.setColorFilter(((Activity) context).getResources().getColor(R.color.red)); // Add tint color
        PostDBHandler postDBHandler = new PostDBHandler(context);
        postDBHandler.addLike(postId);
        UserDBHandler userDBHandler = new UserDBHandler(context);
        AuthHandler authHandler = new AuthHandler(context);
        userDBHandler.addLike(authHandler.getCurrentUser().getUid(), postId);
        holder.IsLiked.setText("1");
    }
    public void removeOneLike(PostViewHolder holder, String postId){
        holder.Likes.setText(""+(Integer.parseInt(holder.Likes.getText().toString())-1));
        holder.LikesIcon.setColorFilter(null); // remove tint color
        PostDBHandler postDBHandler = new PostDBHandler(context);
        postDBHandler.removeLike(postId);
        UserDBHandler userDBHandler = new UserDBHandler(context);
        AuthHandler authHandler = new AuthHandler(context);
        userDBHandler.removeLike(authHandler.getCurrentUser().getUid(), postId);
        holder.IsLiked.setText("0");
    }
    @Override
    public int getItemCount() {
        return data.size();
    }

}
