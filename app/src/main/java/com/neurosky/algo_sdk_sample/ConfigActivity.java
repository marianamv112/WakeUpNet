package com.neurosky.algo_sdk_sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class ConfigActivity extends AppCompatActivity {

    String breakTime = "";
    String workTime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        final Spinner brkSpin = (Spinner) findViewById(R.id.breakSpinner);
        final TextView welcomeText = (TextView) findViewById(R.id.welcomeText);
        final Button subButton = (Button) findViewById(R.id.submitButton);
        final Spinner wrkSpin = (Spinner) findViewById(R.id.workSpinner);

        Intent i = getIntent();
        final User user = (User)i.getSerializableExtra("user");

        final String userID = user.getUsername();

        welcomeText.setText("Welcome " + userID + "!");

        subButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                workTime = String.valueOf(wrkSpin.getSelectedItem());
                breakTime = String.valueOf(brkSpin.getSelectedItem());
                Intent mainIntent = new Intent(ConfigActivity.this, MainActivity.class);
                mainIntent.putExtra("user", user);
                mainIntent.putExtra("breakTime", breakTime);
                mainIntent.putExtra("workTime", workTime);
                startActivity(mainIntent);
            }

        });

    }
}
