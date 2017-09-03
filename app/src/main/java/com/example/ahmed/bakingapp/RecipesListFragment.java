package com.example.ahmed.bakingapp;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ahmed.bakingapp.adapters.RecipesListAdapter;
import com.example.ahmed.bakingapp.models.Recipe;

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
    private static Parcelable mListState;


    public RecipesListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRecipeListAdapter = new RecipesListAdapter(getContext(), this);

        try {
            mRecipesList = Recipe.parseJson(getContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
        mRecipeListAdapter.setRecipesList(mRecipesList);


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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(LIST_STATE_KEY, mRecyclerView.getLayoutManager().onSaveInstanceState());


    }

    @Override
    public void onResume() {
        super.onResume();
        if (mListState != null) {
            mRecyclerView.getLayoutManager().onRestoreInstanceState(mListState);
        }

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mListState = savedInstanceState.getParcelable(LIST_STATE_KEY);

        }


    }


}
