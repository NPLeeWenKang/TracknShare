package sg.edu.np.tracknshare.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import sg.edu.np.tracknshare.R;
import sg.edu.np.tracknshare.SplashLoginActivity;
import sg.edu.np.tracknshare.viewholders.ViewPagerViewHolder;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerViewHolder> {
    Context context;
    ViewPager2 viewPager2;
    public ViewPagerAdapter(Context c, ViewPager2 vp2){
        context = c;
        viewPager2 = vp2;
    }
    @NonNull
    @Override
    public ViewPagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item;
        if (viewType == 0){
            item = LayoutInflater.from(parent.getContext()).inflate(R.layout.description_1, parent, false);
        }else if(viewType == 1){
            item = LayoutInflater.from(parent.getContext()).inflate(R.layout.description_2, parent, false);
        }else if(viewType == 2){
            item = LayoutInflater.from(parent.getContext()).inflate(R.layout.description_3, parent, false);
        } else{
            item = null;
        }

        return new ViewPagerViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPagerViewHolder holder, int position) {
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position == 2){
                    Intent intent = new Intent(context, SplashLoginActivity.class);
                    context.startActivity(intent);
                }else{
                    viewPager2.setCurrentItem(viewPager2.getCurrentItem()+1,true);
                }
            }
        });
        holder.view.findViewById(R.id.nextButton).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (position == 2){
                    Intent intent = new Intent(context, SplashLoginActivity.class);
                    context.startActivity(intent);
                }else{
                    viewPager2.setCurrentItem(viewPager2.getCurrentItem()+1,true);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
