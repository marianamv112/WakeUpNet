package com.neurosky.algo_sdk_sample;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends ActionBarActivity implements LoaderCallbacks<Cursor> {

    final String TAG = "LoginActivityTag";

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button enterButton = (Button)this.findViewById(R.id.sign_in_button);

        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                EditText editText = (EditText) findViewById(R.id.id);
                String userID = editText.getText().toString();
                mainIntent.putExtra("subject", userID);
                Log.d("Login", userID );
                startActivity(mainIntent);

            }
        });

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}


