package com.example.sound4you.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.sound4you.R;
import com.example.sound4you.data.model.HomeSection;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;


import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.SectionViewHolder> {
    private final Context context;
    private final List<HomeSection> homeSections;
    private final HomeChildAdapter.OnTrackClickListener listener;

    public HomeAdapter(Context context, List<HomeSection> homeSections, HomeChildAdapter.OnTrackClickListener listener) {
        this.context = context;
        this.homeSections = homeSections;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_section, parent, false);
        return new SectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder (@NonNull SectionViewHolder holder, int position) {
        HomeSection section = homeSections.get(position);
        holder.tvTitle.setText(section.getTitle());

        HomeChildAdapter childAdapter = new HomeChildAdapter(context, section.getTracks(), listener);
        if (section.getLayoutType() == 0) {
            holder.rvTracks.setLayoutManager(new GridLayoutManager(context, 2));
        }
        else {
            holder.rvTracks.setLayoutManager(
                    new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            );
        }
        holder.rvTracks.setAdapter(childAdapter);
    }

    @Override
    public int getItemCount() {
        return homeSections.size();
    }

    static class SectionViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        RecyclerView rvTracks;

        SectionViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvSectionTitle);
            rvTracks = itemView.findViewById(R.id.rvSectionTracks);
        }
    }
}
