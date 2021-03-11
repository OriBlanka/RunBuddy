package com.runbuddy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ActivityAdp extends RecyclerView.Adapter<ActivityAdp.ViewHolder> {

    ArrayList<String> arrayListActivity;

    public ActivityAdp(ArrayList<String> arrayListActivity){
        this.arrayListActivity = arrayListActivity;
    }

    @NonNull
    @Override
    public ActivityAdp.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.list_row_activity, parent, false);
        return new ActivityAdp.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityAdp.ViewHolder holder, int position) {
        holder.tvName.setText(arrayListActivity.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayListActivity.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }
}

