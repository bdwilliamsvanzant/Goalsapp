package com.example.goals;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class UserStatsAdapter extends RecyclerView.Adapter<UserStatsAdapter.GoalHolder> {

    private List<Goal> list;
    private Context context;
    private LayoutInflater layoutInflater;
    private UserStatsAdapter.OnGoalItemClick onGoalItemClick;


    public UserStatsAdapter(List<Goal> list, Context context) {
        layoutInflater = LayoutInflater.from(context);
        this.list = list;
        this.context = context;
        this.onGoalItemClick = (UserStatsAdapter.OnGoalItemClick) context;
    }

    @Override
    public UserStatsAdapter.GoalHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.goal_list_item,parent,false);
        return new UserStatsAdapter.GoalHolder(view);
    }


    @Override
    public void onBindViewHolder(UserStatsAdapter.GoalHolder holder, int position) {
        Log.e("bind", "onBindViewHolder: "+ list.get(position));
        holder.textViewTitle.setText(list.get(position).getGoal_name());
        holder.textViewContent.setText(list.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class GoalHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewContent;
        TextView textViewTitle;
        TextView textViewDifficulty;
        TextView textViewStartTime;
        TextView textViewEndTime;
        TextView textViewPoints;

        public GoalHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textViewContent = itemView.findViewById(R.id.item_text);
            textViewTitle = itemView.findViewById(R.id.goal_name);
            textViewDifficulty = itemView.findViewById(R.id.tv_goal_difficulty);
            textViewStartTime = itemView.findViewById(R.id.tv_goal_startTime);
            textViewEndTime = itemView.findViewById(R.id.tv_goal_endTime);
            textViewPoints = itemView.findViewById(R.id.tv_goal_points);
        }

        @Override
        public void onClick(View view) {
            onGoalItemClick.onGoalClick(getAdapterPosition());
        }
    }

    public interface OnGoalItemClick{
        void onGoalClick(int pos);
    }

}
