package com.example.ahmed.bakingapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ahmed.bakingapp.R;
import com.example.ahmed.bakingapp.models.Recipe;

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


    public interface OnRecipeClickListener{
        void onRecipeClick(int position);
    }

    public RecipesListAdapter(Context context, OnRecipeClickListener onRecipeClickListener){
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



        holder.recipeNameTextView.setText(recipeName);

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
