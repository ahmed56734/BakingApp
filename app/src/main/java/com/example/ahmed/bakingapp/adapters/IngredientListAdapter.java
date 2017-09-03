package com.example.ahmed.bakingapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ahmed.bakingapp.models.Ingredient;

import java.util.List;

/**
 * Created by ahmed on 6/27/17.
 */

public class IngredientListAdapter extends RecyclerView.Adapter<IngredientListAdapter.IngredientViewHolder> {

    List<Ingredient> mIngredients;

    @Override
    public IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientViewHolder holder, int position) {
        String quantity = mIngredients.get(position).getQuantity();
        String measure = mIngredients.get(position).getMeasure();
        String name = mIngredients.get(position).getIngredientName();
        String text = quantity + " " + measure + "  " + name;

        ((TextView)holder.itemView).setText(text);



    }

    @Override
    public int getItemCount() {
        if(mIngredients == null)
            return 0;
        return mIngredients.size();
    }

    public void setIngredients(List<Ingredient> ingredients){
        mIngredients = ingredients;
        notifyDataSetChanged();
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {
        public IngredientViewHolder(View itemView) {
            super(itemView);
        }
    }
}
