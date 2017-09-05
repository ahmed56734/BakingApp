package com.example.ahmed.bakingapp;

import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.example.ahmed.bakingapp.IdlingResource.SimpleIdlingResource;
import com.example.ahmed.bakingapp.adapters.RecipesListAdapter;
import com.example.ahmed.bakingapp.models.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RecipesListAdapter.OnRecipeClickListener {


    @BindView(R.id.rv_recipes_list)
    RecyclerView mRecyclerView;

    RecipesListAdapter mRecipeListAdapter;

    private static final String LIST_STATE_KEY = "keey";
    private Parcelable mListState;



    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mRecipeListAdapter = new RecipesListAdapter(this, this);
        mIdlingResource = (SimpleIdlingResource) this.getIdlingResource();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mRecipeListAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.list_item_divider));
        mRecyclerView.addItemDecoration(dividerItemDecoration);


    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mIdlingResource != null) {
            mIdlingResource.setIdleState(false);
            Log.d("onStart", mIdlingResource.isIdleNow() +"");
        }



        AndroidNetworking.get("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json")
                .setTag(this)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObjectList(Recipe.class, new ParsedRequestListener<List<Recipe>>() {
                    @Override
                    public void onResponse(List<Recipe> recipes) {
                        mRecipeListAdapter.setRecipesList(recipes);
                        if (mListState != null) {
                            mRecyclerView.getLayoutManager().onRestoreInstanceState(mListState);
                        }

                        if(mIdlingResource != null)
                            mIdlingResource.setIdleState(true);
                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        Log.d("network", "error", anError);
                    }
                });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(LIST_STATE_KEY, mRecyclerView.getLayoutManager().onSaveInstanceState());

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            mListState = savedInstanceState.getParcelable(LIST_STATE_KEY);

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mListState = mRecyclerView.getLayoutManager().onSaveInstanceState();
    }

    @Override
    public void onRecipeClick(int position) {
        Recipe recipe = mRecipeListAdapter.getRecipe(position);
        Intent intent = new Intent(this, RecipeDetailsActivity.class);
        intent.putExtra("recipe", recipe);
        startActivity(intent);
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }
}
