package com.example.wise_memory_optimizer.ui.adapter;

import android.app.ActivityManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wise_memory_optimizer.R;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class ListAppsRunningAdapter extends RecyclerView.Adapter<ListAppsRunningAdapter.ViewHolder> {

    private Context context;
    private List<ActivityManager.RunningAppProcessInfo>  listAppRuning;

    public ListAppsRunningAdapter(Context context, List<ActivityManager.RunningAppProcessInfo>  listAppRuning) {
        this.context = context;
        this.listAppRuning = listAppRuning;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context)
                .load(listAppRuning.get(position))
                .into(holder.ic_app);
    }

    @Override
    public int getItemCount() {
        return listAppRuning.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ic_app;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ic_app = itemView.findViewById(R.id.ic_app);
        }
    }
}
