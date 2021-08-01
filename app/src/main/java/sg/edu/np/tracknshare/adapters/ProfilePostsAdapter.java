package sg.edu.np.tracknshare.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
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
import sg.edu.np.tracknshare.handlers.AuthHandler;
import sg.edu.np.tracknshare.handlers.PostDBHandler;
import sg.edu.np.tracknshare.handlers.StorageHandler;
import sg.edu.np.tracknshare.handlers.UserDBHandler;
import sg.edu.np.tracknshare.models.Post;
import sg.edu.np.tracknshare.models.User;
import sg.edu.np.tracknshare.models.UserPostViewModel;
import sg.edu.np.tracknshare.viewholders.PostViewHolder;
import sg.edu.np.tracknshare.viewholders.ProfilePostViewHolder;

public class ProfilePostsAdapter extends RecyclerView.Adapter<ProfilePostViewHolder> {
    Context context ;
    ArrayList<UserPostViewModel> data;
    boolean isSelected;//for toggling of likes upon click eventHandler
    /*    int likes; //to store likes of each viewHolder post
        int tapCount; //separate eventHandler scenario for tapping on img post*/
//    Post p;
    public ProfilePostsAdapter(Context c,ArrayList<UserPostViewModel> up ){
        context = c;
        data = up;
    }

    @NonNull
    @Override
    public ProfilePostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vh_profile_post,parent,false);

        return new ProfilePostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfilePostViewHolder holder, int position) {
        Post p = data.get(position).getPost();
        User u = data.get(position).getUser();
        DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy h:mma");
        holder.Username.setText(u.getUserName());
        holder.PostDate.setText(dateFormat.format(p.getPostDate()));

        holder.PostCaption.setText(p.getCaption());


        StorageHandler storageHandler = new StorageHandler();
        storageHandler.loadFileToApp(p.getRunId(), context, holder.PostImg);

        AuthHandler auth = new AuthHandler(context);

        PostDBHandler postDBHandler = new PostDBHandler(context);
        postDBHandler.isLiked(holder.LikesIcon, holder.IsLiked, p.getPostId()); // determines if current user should be able to like

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
        if (!u.getId().equals(auth.getCurrentUser().getUid())){
            holder.Likes.setText(""+p.getLikes());
            holder.LikesIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_heart));
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


    }
    public void addOneLike(ProfilePostViewHolder holder, String postId){
        // adds a like to the database and changes like icon color to red
        holder.Likes.setText(""+(Integer.parseInt(holder.Likes.getText().toString())+1));
        holder.LikesIcon.setColorFilter(((Activity) context).getResources().getColor(R.color.red)); // Add tint color
        PostDBHandler postDBHandler = new PostDBHandler(context);
        postDBHandler.addLike(postId);
        UserDBHandler userDBHandler = new UserDBHandler(context);
        AuthHandler authHandler = new AuthHandler(context);
        userDBHandler.addLike(authHandler.getCurrentUser().getUid(), postId);
        holder.IsLiked.setText("1");
    }
    public void removeOneLike(ProfilePostViewHolder holder, String postId){
        // removes a like from the database and changes like icon color to normal
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
