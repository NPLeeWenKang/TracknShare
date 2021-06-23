package sg.edu.np.tracknshare.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import sg.edu.np.tracknshare.R;
import sg.edu.np.tracknshare.adapters.PostsAdapter;
import sg.edu.np.tracknshare.models.Post;

public class fragment_follow extends Fragment {
    ImageView sendButton;
    ImageView likesIcon;
    TextView postCaption;
    ArrayList<Post> posts;

    public fragment_follow() {
        //Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_follow,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sendButton = view.findViewById(R.id.sharePost);
        postCaption = view.findViewById(R.id.postCaption);
        likesIcon = view.findViewById(R.id.likesImg);
        posts = new ArrayList<Post>();
        generatePosts();
        RecyclerView rv = view.findViewById(R.id.rvPost);
        PostsAdapter adapter = new PostsAdapter(view.getContext(),posts);
        LinearLayoutManager lm = new LinearLayoutManager(view.getContext());
        rv.setLayoutManager(lm);
        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }
    public void generatePosts(){
        //Post date formatting constructor and methods
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy") ;
        String date = sdf.format(calendar.getTime());

        String captionText = "This is the message of the post! Lorem ipsum dolor sit amet, consectetur adipiscing elit." +
                "Sed sagittis ante quis convallis rutrum. Integer vitae convallis nulla, non pharetra quam. " +
                "Fusce in sodales arcu, a iaculis felis.";
        for(int i = 1;i<=10;i++){
            Post p = new Post();
            p.setPostUsername("#MAD CATS"+i);
            p.setPostId(i);
            p.setCaption(captionText);
            p.setLikes(i*2);
            p.setPostDate(date);
            posts.add(p);
        }
    }
    public void SharePost(String caption, Uri imageUri){
        /*this function calls the sharesheet API to share the posts to
        other social messaging apps*/
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_STREAM,imageUri);
        sendIntent.setType("image/jpeg");

        Intent sharePost = Intent.createChooser(sendIntent,"hello");
        startActivity(sharePost);
    }

}
