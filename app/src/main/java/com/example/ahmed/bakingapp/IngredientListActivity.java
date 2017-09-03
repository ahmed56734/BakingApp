package com.example.ahmed.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.ahmed.bakingapp.models.Ingredient;

import java.util.ArrayList;

public class IngredientListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_list);

        ArrayList<Ingredient> ingredients = getIntent().getParcelableArrayListExtra("ingredients");

        IngredientsListFragment ingredientsListFragment = IngredientsListFragment.newInstance(ingredients);

        getSupportFragmentManager().beginTransaction().replace(R.id.ingerdient_list_container, ingredientsListFragment).commit();
    }
}
