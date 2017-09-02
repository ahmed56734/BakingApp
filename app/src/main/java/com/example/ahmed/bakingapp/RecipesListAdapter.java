package com.example.ahmed.bakingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ahmed on 6/27/17.
 */

public class RecipesListAdapter extends RecyclerView.Adapter<RecipesListAdapter.RecipeViewHolder> {

    private List<Recipe> mRecipesList;
    private Context mContext;
    private OnRecipeClickListener mOnRecipeClickListener;


    interface OnRecipeClickListener{
        void onRecipeClick(int position);
    }

    RecipesListAdapter(Context context, OnRecipeClickListener onRecipeClickListener){
        mContext = context;
        mOnRecipeClickListener = onRecipeClickListener;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_item, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        String recipeName = mRecipesList.get(position).getName();

        String imageName = mRecipesList.get(position).getImageName();
        int id = mContext.getResources().getIdentifier(imageName, "drawable", mContext.getPackageName());

        holder.recipeNameTextView.setText(recipeName);
        holder.imageView.setImageResource(id);
    }

    @Override
    public int getItemCount() {
        if(mRecipesList == null)
            return 0;
        else
            return mRecipesList.size();
    }

    public void setRecipesList(List<Recipe> recipesList){
        mRecipesList = recipesList;
        notifyDataSetChanged();

    }

    public Recipe getRecipe(int position){
        return mRecipesList.get(position);
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_recipe_name) TextView recipeNameTextView;
        @BindView(R.id.iv_recipe_image) ImageView imageView;
        public RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mOnRecipeClickListener.onRecipeClick(getAdapterPosition());
        }
    }
}
