package com.example.ahmed.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.example.ahmed.bakingapp.R;
import com.example.ahmed.bakingapp.models.Ingredient;
import com.example.ahmed.bakingapp.models.Recipe;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;

import java.util.List;
import java.util.concurrent.ExecutionException;

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
    private String mRecipeName;

    IngredientsListRemoteViewsFactory(Context context){
        mContext = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        final int recipeId = mContext.getSharedPreferences("recipes", Context.MODE_PRIVATE).getInt("recipe_id", 1);

        final TaskCompletionSource<List<Ingredient>> taskCompletionSource = new TaskCompletionSource<>();
        Task<List<Ingredient>> task = taskCompletionSource.getTask();

        AndroidNetworking.get("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json")
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsObjectList(Recipe.class, new ParsedRequestListener<List<Recipe>>() {
                    @Override
                    public void onResponse(List<Recipe> recipes) {
                        for(Recipe recipe : recipes){
                            if(recipe.getId() == recipeId){
                                mRecipeName = recipe.getName();
                                taskCompletionSource.setResult(recipe.getIngredients());
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        Log.d("network", "error", anError);
                    }
                });

        try {
            mIngredientsList = Tasks.await(task);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }


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
            text = mRecipeName + " Ingredients\n";
        }
        else {
            String name = mIngredientsList.get(i-1).getIngredient();
            String measure = mIngredientsList.get(i-1).getMeasure();
            String quantity = String.valueOf(mIngredientsList.get(i-1).getQuantity());
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
