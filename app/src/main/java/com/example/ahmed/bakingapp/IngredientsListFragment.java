package com.example.ahmed.bakingapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ahmed.bakingapp.adapters.IngredientListAdapter;
import com.example.ahmed.bakingapp.models.Ingredient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class IngredientsListFragment extends Fragment {

    @BindView(R.id.rv_ingredients_list) RecyclerView mRecyclerView;
    IngredientListAdapter mIngredientListAdapter;

    public IngredientsListFragment() {
        // Required empty public constructor
    }

     public static IngredientsListFragment newInstance(ArrayList<Ingredient> ingredients) {

        Bundle args = new Bundle();
        args.putParcelableArrayList("ingredients", ingredients);
        IngredientsListFragment fragment = new IngredientsListFragment();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<Ingredient> ingredients = getArguments().getParcelableArrayList("ingredients");
        mIngredientListAdapter = new IngredientListAdapter();
        mIngredientListAdapter.setIngredients(ingredients);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_ingredients_list, container, false);
        ButterKnife.bind(this, view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mIngredientListAdapter);

        return view;
    }

}
