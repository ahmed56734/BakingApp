package com.example.ahmed.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Recipe> recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        RecipesListFragment recipesListFragment = new RecipesListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.recipe_fragment_container, recipesListFragment).commit();



    }
}
