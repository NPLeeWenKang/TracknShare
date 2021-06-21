package sg.edu.np.tracknshare.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import sg.edu.np.tracknshare.R;
import sg.edu.np.tracknshare.models.Post;
import sg.edu.np.tracknshare.adapters.PostsAdapter;
import sg.edu.np.tracknshare.viewholders.PostViewHolder;

public class PostFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    ImageView sendButton;
    ImageView likesIcon;
    TextView postCaption;
    ArrayList<Post> posts;

    public PostFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_post, container, false);
        return view;
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
}