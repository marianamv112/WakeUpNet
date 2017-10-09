package com.neurosky.algo_sdk_sample;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A login screen that offers login via email/password.
 */



public class LoginActivity extends ActionBarActivity implements LoaderCallbacks<Cursor> {


    private UserLoginTask mAuthTask = null;

    final String TAG = "LoginActivityTag";

    private User user;

    private Session auxSession;

    private ArrayList<Session> sessions = new ArrayList<Session>();

    private Toast toast;

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

        Button enterButton = (Button) this.findViewById(R.id.sign_in_button);

        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent configIntent = new Intent(LoginActivity.this, ConfigActivity.class);
                EditText editText = (EditText) findViewById(R.id.regUserId);
                String inputUsername = editText.getText().toString();
                //DataBaseHandler db = new DataBaseHandler(LoginActivity.this);
                /*String answer = db.userExist(inputUsername);


                if( answer != null) {
                    user = new User(answer);
                    Log.d(TAG, "EXISTENT USER NAMED: " + answer);
                    sessions = db.getSessions(answer);
                    user.setSessions(sessions);
                } else {
                    user = new User(inputUsername);
                    Log.d(TAG, "NEW USER NAMED: " + inputUsername);
                    auxSession = new Session(0, 0, 0);
                    ArrayList <Session> auxListSession = new ArrayList<Session>();
                    auxListSession.add(auxSession);
                    user.setSessions(auxListSession);
                    db.addResults(user, auxSession);
                }*/
                //user = new User(userID);
                //session = new Session(0,0,0);

                /* Todo este codigo esta correcto e testado*/

                //se houver ligacao, pode aceder a base de dados remota
                ConnectivityManager cm = (ConnectivityManager)LoginActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

                if (isConnected) {
                    Log.d(TAG, "Connected");
                    mAuthTask = new UserLoginTask(inputUsername);
                    mAuthTask.execute((Void) null);
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please turn on wi-fi", Toast.LENGTH_SHORT);
                    toast.show();

                    //se nao houver ligacao, tera de usar apenas a base de dados local
                    //DataBaseHandler db = new DataBaseHandler(LoginActivity.this);
                    //Log.d("Insert: ", "Inserting ..");

                    //db.addResults(user, session); //FALTA VER COMO E QUE VEMOS QUAL E O SESSIONID!!!
                }

                user = new User(inputUsername);
                configIntent.putExtra("user", user);
                //configIntent.putExtra("session", auxSession);
                startActivity(configIntent);
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

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */

    public class UserLoginTask extends AsyncTask<Void, Void, Void> {


        private final String mUsername;

        UserLoginTask(String username) {
            mUsername = username;
        }

        @Override
        protected Void doInBackground(Void... params) {

            RestClient client = new RestClient("http://192.168.43.246:3000/user");
            client.AddParam("username", mUsername);
            try {
                client.Execute(RequestMethod.GET);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String response = client.getResponse();
            Log.d("RESPONSE: ", response);

            //new User
            if (!response.contains(mUsername)) {

                auxSession = new Session(1, 0, 0);
                sessions.add(auxSession);
                user.setSessions(sessions);

                client = new RestClient("http://192.168.43.246:3000/newUser");
                client.AddParam("username", user.getUsername());
                client.AddParam("sessionNr", user.getSessions().get(0).getSessionID().toString());
                client.AddParam("maxWorkDuration", user.getSessions().get(0).getMaxWorkDuration().toString());
                client.AddParam("nrOfBreaks", user.getSessions().get(0).getNumOfBreaks().toString());
                try {
                    client.Execute(RequestMethod.POST);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                //already registered user
                client = new RestClient("http://192.168.43.246:3000/getSessions");
                client.AddParam("username", mUsername);
                try {
                    client.Execute(RequestMethod.GET);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                response = client.getResponse();

                populate(user, response);
            }
            return null;
        }
    }

    public void populate(User user, String input) {

        int newsessionr;

        String[] array = input.split("\\}");
        newsessionr = array.length;
        ArrayList<Integer> popValues = new ArrayList<Integer>();

        for (int i = 0; i < newsessionr; i++) {
            String[] array2 = array[i].split(",");

            for (int j = 0; j < array2.length; j++) {
                String[] array3 = array2[j].split(":");

                if (popValues.size() < 3) {
                    popValues.add(Integer.parseInt(array3[1]));
                } else {
                    Session ses = new Session(popValues.get(0), popValues.get(1), popValues.get(2));
                    //user.addSessions(ses);
                    popValues.clear();
                }
            }
        }


    }
}


