package sg.edu.np.tracknshare.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import sg.edu.np.tracknshare.handlers.StorageHandler;
import sg.edu.np.tracknshare.models.Post;
import sg.edu.np.tracknshare.models.Run;
import sg.edu.np.tracknshare.models.User;
import sg.edu.np.tracknshare.models.UserPostViewModel;
import sg.edu.np.tracknshare.viewholders.PostViewHolder;

public class PostsAdapter extends RecyclerView.Adapter<PostViewHolder> {
    Context context ;
    ArrayList<UserPostViewModel> data;
    boolean isSelected;//for toggling of likes upon click eventHandler
    /*    int likes; //to store likes of each viewHolder post
        int tapCount; //separate eventHandler scenario for tapping on img post*/
//    Post p;
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

        isSelected = false;
        int red = Color.RED;
        int white = Color.WHITE;
        holder.LikesIcon.setOnClickListener(new View.OnClickListener(){
            int likes = p.getLikes();

            int clickCount = 0;
            @Override
            public void onClick(View v) {
                int l = likes;
                clickCount += 1;
                if(clickCount > 0){//to prevent unnecessary minusing of like as initial isClicked is false
                    if(!isSelected){ // unselected to selected:turns red,likes + 1,
                        likes = addOneLike(holder,red,l);
                        isSelected = true;
                    }

                    else{   // selected to unselected:turns white,likes - 1,
                        likes = removeOneLike(holder,white,l);
                        isSelected = false;
                    }

                    p.setLikes(likes);
                    holder.Likes.setText(""+likes);
                }

            }


        });
/*
        holder.PostImg.setOnClickListener(new View.OnClickListener() {
            int tapCount = 0;
            @Override
            public void onClick(View v) {
                tapCount += 1;
                if (tapCount == 2){
                    if(!isSelected){ // unselected to selected:turns red,likes + 1,
                        int likes = addOneLike(holder,red,p.getLikes()); //double tap on image only likes post,cannot unlike post
                        //hence one method,unlike can only be done via likeIcon onclick
                        p.setLikes(likes);
                        holder.Likes.setText(""+likes);
                        tapCount = 0; //reset the double tap method
                    }
                }
            }
        });
*/
        holder.ViewComments.setOnClickListener(new View.OnClickListener() {
            Run r = new Run();// to be modified with DB handlers
            @Override
            public void onClick(View v) {
//                EnterCommentView(p,r);
            }
        });
    }
    public int addOneLike(PostViewHolder holder,int red,int l){
        isSelected = true;
        l +=1;
        holder.LikesIcon.setBackgroundColor(red);
        return l;
    }
    public int removeOneLike(PostViewHolder holder, int white, int l){
        isSelected = false;
        l -= 1;
        holder.LikesIcon.setBackgroundColor(white);
        return l;
    }
    @Override
    public int getItemCount() {
        return data.size();
    }

//    public void EnterCommentView(Post p, Run r){
//        Intent intent = new Intent(context, CommentsActivity.class);
//        intent.putExtra("Username", );
//        intent.putExtra("Postdate",p.getPostDate());
//        intent.putExtra("PostLikes",p.getLikes());
//        intent.putExtra("PostCaption",p.getCaption());
//        intent.putExtra("RunDistance",r.getRunDistance());
//        intent.putExtra("RunDuration",r.getRunDuration());
//        intent.putExtra("RunCalories",r.getRunCalories());
//        intent.putExtra("RunPace",r.getRunPace());
//        intent.putExtra("RunDate",r.getRunDate());
//        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //to allow activity transition from adapter class
//        context.startActivity(intent);
//
//    }


}
