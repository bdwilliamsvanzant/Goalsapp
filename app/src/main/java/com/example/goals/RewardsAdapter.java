package com.example.goals;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class RewardsAdapter extends RecyclerView.Adapter<RewardsAdapter.RewardHolder> {

    private List<Reward> list;
    private Context context;
    private LayoutInflater layoutInflater;
    private OnRewardItemClick onRewardItemClick;


    public RewardsAdapter(List<Reward> list, Context context) {
        layoutInflater = LayoutInflater.from(context);
        this.list = list;
        this.context = context;
        this.onRewardItemClick = (OnRewardItemClick) context;
    }

    @Override
    public RewardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.reward_list_item,parent,false);
        return new RewardHolder(view);
    }


    @Override
    public void onBindViewHolder(RewardHolder holder, int position) {
        Log.e("bind", "onBindViewHolder: "+ list.get(position));
        holder.textViewTitle.setText(list.get(position).getReward_name());
        holder.textViewDescription.setText(list.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class RewardHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewDescription;
        TextView textViewTitle;
        TextView textViewPoints;

        public RewardHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textViewDescription = itemView.findViewById(R.id.item_text);
            textViewTitle = itemView.findViewById(R.id.reward_name);
            //textViewPoints = itemView.findViewById(R.id.tv_Reward_points);
        }

        @Override
        public void onClick(View view) {
            onRewardItemClick.onRewardClick(getAdapterPosition());
        }
    }

    public interface OnRewardItemClick{
        void onRewardClick(int pos);
    }
}