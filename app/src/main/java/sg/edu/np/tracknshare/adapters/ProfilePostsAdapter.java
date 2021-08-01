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
import sg.edu.np.tracknshare.handlers.StorageHandler;
import sg.edu.np.tracknshare.models.Post;
import sg.edu.np.tracknshare.models.User;
import sg.edu.np.tracknshare.models.UserPostViewModel;
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
                int likes = p.getLikes();

                int clickCount = 0;
                @Override
                public void onClick(View v) {
                    int l = likes;
                    clickCount += 1;
                    if(clickCount > 0){//to prevent unnecessary minusing of like as initial isClicked is false
                        if(!isSelected){ // unselected to selected:turns red,likes + 1,
                        }

                        else{   // selected to unselected:turns white,likes - 1,
                        }

                        p.setLikes(likes);
                        holder.Likes.setText(""+likes);
                    }

                }


            });
        }


    }
    @Override
    public int getItemCount() {
        return data.size();
    }

}
