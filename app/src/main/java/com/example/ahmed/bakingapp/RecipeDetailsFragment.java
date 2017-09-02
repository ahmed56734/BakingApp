package com.example.ahmed.bakingapp;


import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeDetailsFragment extends Fragment  {

    @BindView(R.id.rv_steps_list) RecyclerView mRecyclerView;
    @BindView(R.id.btn_recipe_ingredients) Button ingredientsButton;
    private Recipe mRecipe;
    private StepsListAdapter mStepListAdapter;
    private StepsListAdapter.OnStepClickListener mOnStepClickListener;
    private static final String BUNDLE_RECYCLER_LAYOUT = "classname.recycler.layout";


    public RecipeDetailsFragment() {
        // Required empty public constructor
    }

    public static RecipeDetailsFragment newInstance(Recipe recipe) {

        Bundle args = new Bundle();
        args.putParcelable("recipe", recipe);

        RecipeDetailsFragment fragment = new RecipeDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRecipe = getArguments().getParcelable("recipe");
        List<Step> steps = mRecipe.getSteps();

        mStepListAdapter = new StepsListAdapter(mOnStepClickListener);
        mStepListAdapter.setStepsList(steps);



    }




    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_recipe_details, container, false);
        ButterKnife.bind(this, view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), ((LinearLayoutManager)mRecyclerView.getLayoutManager()).getOrientation());
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.list_item_divider));
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setAdapter(mStepListAdapter);

        ingredientsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), IngredientListActivity.class);
                intent.putParcelableArrayListExtra("ingredients", (ArrayList<Ingredient>) mRecipe.getIngredients());
                startActivity(intent);
            }
        });



        return view;
    }


    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, mRecyclerView.getLayoutManager().onSaveInstanceState());


    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null)
        {
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            mRecyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }


    }

    @Override
    public void onStop() {
        super.onStop();

        String recipeId = mRecipe.getId();
        SharedPreferences.Editor editor = getContext().getSharedPreferences("recipes", Context.MODE_PRIVATE).edit();
        editor.putString("recipe_id", recipeId);
        editor.commit();

        UpdateAppWidgetService.startActionUpdateIngredientsWidget(getContext());




    }

    public  void setOnStepClickListener(StepsListAdapter.OnStepClickListener onStepClickListener){
        mOnStepClickListener = onStepClickListener;
    }
}
