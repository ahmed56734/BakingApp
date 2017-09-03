package com.example.ahmed.bakingapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ahmed.bakingapp.R;
import com.example.ahmed.bakingapp.models.Step;

import java.util.List;

/**
 * Created by ahmed on 6/27/17.
 */

public class StepsListAdapter extends RecyclerView.Adapter<StepsListAdapter.StepViewHolder> {

    List<Step> mSteps;
    OnStepClickListener mOnStepClickListener;


    public interface OnStepClickListener{
        void onStepClick(Step step);
    }

    public StepsListAdapter(OnStepClickListener onStepClickListener){
        mOnStepClickListener = onStepClickListener;
    }
    @Override
    public StepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.step_list_item, parent, false);
        return new StepViewHolder(view);
    }

    public void setStepsList(List<Step> steps){
        mSteps = steps;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(StepViewHolder holder, int position) {
        String shortDescription = mSteps.get(position).getShortDescription();

        if(position != 0)
            shortDescription = String.valueOf(position) + "- " + shortDescription;

        ((TextView)holder.itemView).setText(shortDescription);
    }

    @Override
    public int getItemCount() {
        if(mSteps == null)
            return 0;
        return mSteps.size();
    }

    public Step getStep(int position){
        return mSteps.get(position);
    }

    public class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public StepViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Step clickedStep = mSteps.get(position);
            mOnStepClickListener.onStepClick(clickedStep);
        }
    }
}
