package com.example.ahmed.bakingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.ahmed.bakingapp.models.Step;

public class StepDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);

        Bundle extras = getIntent().getExtras();
        Step step = extras.getParcelable("step");

        StepDetailsFragment stepDetailsFragment = StepDetailsFragment.newInstance(step);


        getSupportFragmentManager().beginTransaction().replace(R.id.step_details_container, stepDetailsFragment).commit();
    }


}
