package com.example.ahmed.bakingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RecipeDetailsActivity extends AppCompatActivity implements StepsListAdapter.OnStepClickListener {

    private boolean mIsTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mIsTwoPane = findViewById(R.id.step_details_container) != null;


        //start recipe details in the two cases
        Recipe recipe = getIntent().getExtras().getParcelable("recipe");
        RecipeDetailsFragment recipeDetailsFragment = RecipeDetailsFragment.newInstance(recipe);
        recipeDetailsFragment.setOnStepClickListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.recipe_details_fragment_container, recipeDetailsFragment).commit();

        //start the StepDetailsFragment in case of two pane with first step of recipe as default
        if(mIsTwoPane){
            Step defaultFirstStep = recipe.getSteps().get(0);
            startDetailsFragment(defaultFirstStep);
        }
    }

    @Override
    public void onStepClick(Step step) {

        if(mIsTwoPane){
            startDetailsFragment(step);
        }

        else {
            Intent intent = new Intent(this, StepDetailsActivity.class);
            intent.putExtra("step", step);
            startActivity(intent);
        }

    }

    private void startDetailsFragment(Step step){
        StepDetailsFragment stepDetailsFragment =  StepDetailsFragment.newInstance(step);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.step_details_container, stepDetailsFragment).commit();
    }
}
