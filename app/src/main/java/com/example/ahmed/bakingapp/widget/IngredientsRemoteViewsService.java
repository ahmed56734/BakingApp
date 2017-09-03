package com.example.ahmed.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.ahmed.bakingapp.models.Ingredient;
import com.example.ahmed.bakingapp.R;
import com.example.ahmed.bakingapp.models.Recipe;

import java.io.IOException;
import java.util.List;

/**
 * Created by ahmed on 7/2/17.
 */

public class IngredientsRemoteViewsService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientsListRemoteViewsFactory(getApplicationContext());
    }
}

class IngredientsListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{

    private Context mContext;
    private List<Ingredient> mIngredientsList;
    private String recipeName;

    IngredientsListRemoteViewsFactory(Context context){
        mContext = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        String recipeId = mContext.getSharedPreferences("recipes", Context.MODE_PRIVATE).getString("recipe_id", "2");
        Recipe recipe = null;
        try {
            recipe = Recipe.getRecipeById(mContext, recipeId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mIngredientsList = recipe.getIngredients();
        recipeName = recipe.getName();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if(mIngredientsList == null || mIngredientsList.isEmpty())
            return 0;
        return mIngredientsList.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {

        RemoteViews itemRemoteViews = new RemoteViews(mContext.getPackageName(), R.layout.ingredients_list_item_widget);
        String text;

        if(i == 0){
            text = recipeName + " Ingredients\n";
        }
        else {
            String name = mIngredientsList.get(i-1).getIngredientName();
            String measure = mIngredientsList.get(i-1).getMeasure();
            String quantity = mIngredientsList.get(i-1).getQuantity();
            text =  quantity + " " + measure + "  " + name;
        }
        itemRemoteViews.setTextViewText(R.id.tv_ingredient_item_widget, text);
        return itemRemoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
