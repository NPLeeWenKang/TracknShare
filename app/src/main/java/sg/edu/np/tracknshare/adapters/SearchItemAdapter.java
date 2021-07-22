package sg.edu.np.tracknshare.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import sg.edu.np.tracknshare.FriendListActivity;
import sg.edu.np.tracknshare.R;
import sg.edu.np.tracknshare.UserDetailActivity;
import sg.edu.np.tracknshare.handlers.StorageHandler;
import sg.edu.np.tracknshare.models.User;
import sg.edu.np.tracknshare.viewholders.SearchItemViewHolder;

public class SearchItemAdapter extends RecyclerView.Adapter<SearchItemViewHolder> {
    private Context context;
    private ArrayList<User> data;
    public SearchItemAdapter(Context c, ArrayList<User> d){
        context = c;
        data = d;
    }
    @NonNull
    @Override
    public SearchItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item;
        item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search, parent, false);
        return new SearchItemViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchItemViewHolder holder, int position) {
        User u = data.get(position);
        holder.username.setText(u.getUserName());

        StorageHandler storageHandler = new StorageHandler();
        storageHandler.LoadProfileImageToApp(u.getId(),context,holder.image);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), UserDetailActivity.class);
                intent.putExtra("id", ""+u.getId());
                ((Activity) view.getContext()).startActivity(intent);
                ((Activity) view.getContext()).overridePendingTransition(R.anim.start_enter, R.anim.start_exit);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
