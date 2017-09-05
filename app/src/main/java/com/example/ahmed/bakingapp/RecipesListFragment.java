package com.example.ahmed.bakingapp;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.example.ahmed.bakingapp.adapters.RecipesListAdapter;
import com.example.ahmed.bakingapp.models.Ingredient;
import com.example.ahmed.bakingapp.models.Recipe;
import com.example.ahmed.bakingapp.models.Step;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipesListFragment extends Fragment implements RecipesListAdapter.OnRecipeClickListener {

    @BindView(R.id.rv_recipes_list)
    RecyclerView mRecyclerView;

    RecipesListAdapter mRecipeListAdapter;
    List<Recipe> mRecipesList;

    private static final String LIST_STATE_KEY = "keey";
    private  Parcelable mListState;
    private static int position;


    public RecipesListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRecipeListAdapter = new RecipesListAdapter(getContext(), this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_recipes_list, container, false);
        ButterKnife.bind(this, root);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mRecipeListAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.list_item_divider));
        mRecyclerView.addItemDecoration(dividerItemDecoration);


        return root;
    }

    @Override
    public void onRecipeClick(int position) {
        Recipe recipe = mRecipeListAdapter.getRecipe(position);
        Intent intent = new Intent(getContext(), RecipeDetailsActivity.class);
        intent.putExtra("recipe", recipe);
        startActivity(intent);
    }


    @Override
    public void onStart() {
        super.onStart();

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
                    }

                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        Log.d("network", "error", anError);
                    }
                });
    }

    @Override
    public void onStop() {
        super.onStop();
        mListState = mRecyclerView.getLayoutManager().onSaveInstanceState();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(LIST_STATE_KEY, mRecyclerView.getLayoutManager().onSaveInstanceState());

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mListState = savedInstanceState.getParcelable(LIST_STATE_KEY);

        }


    }


}
