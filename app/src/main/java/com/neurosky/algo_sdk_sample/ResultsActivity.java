package com.neurosky.algo_sdk_sample;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ResultsActivity extends ActionBarActivity {

    final String TAG = "MainActivityTag";


    File outputFile;
    String gnuPlotInput = "";
    int gnuPlotXXAxis = 0;

    private static final String URL = "194.210.234.246:3000"; //ver este url...
    ProgressDialog progress;
    Integer sessDur;
    Integer maxSess;
    Integer numBreaks;
    Integer sessId;
    User user;
    private DataBaseHandler db;

    private SendData sendDataTask = null;

    //List to store all the results
    private List<Results> results;
    private Results result;

    //1 means data is synced and 0 means data is not synced
    public static final int SYNCED_WITH_SERVER = 1;
    public static final int NOT_SYNCED_WITH_SERVER = 0;

    //a broadcast to know weather the data is synced or not
    public static final String DATA_SAVED_BROADCAST = "net.simplifiedcoding.datasaved";

    //Broadcast receiver to know the sync status
    private BroadcastReceiver broadcastReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        TextView sessionDurText = (TextView) findViewById(R.id.sessionDurText);
        TextView maxSessionDurText = (TextView) findViewById(R.id.maxSessionDurText);
        TextView numBreaksText = (TextView) findViewById(R.id.numBreaksText);
        TextView breaksInstText = (TextView) findViewById(R.id.breaksInstText);
        TextView lowerBondAtt = (TextView) findViewById(R.id.lowerBond);
        TextView upperBondAtt = (TextView) findViewById(R.id.upperBond);
        Button sendButton = (Button) findViewById(R.id.sendButton);
        Button sendButton2 = (Button) findViewById(R.id.sendButton2);
        Button suggButton = (Button) findViewById(R.id.suggButton);


        Intent b;
        b = getIntent();
        user = (User)b.getSerializableExtra("user");
        final Session session = (Session)b.getSerializableExtra("session");

        sessDur = b.getExtras().getInt("session duration");
        maxSess = session.getMaxWorkDuration();
        numBreaks = session.getNumOfBreaks();
        sessId = session.getSessionID();
        Integer lowerBond = b.getExtras().getInt("lower Bond");
        Integer upperBond = b.getExtras().getInt("upper Bond");

        result.setResUser(user);
        result.setResSessionID(sessId);
        result.setResMaxWorkDuration(numBreaks);
        result.setResMaxWorkDuration(maxSess);

        ArrayList<Integer> breakInsts = b.getExtras().getIntegerArrayList("break instants");
        /*ArrayList<String> rawValues = b.getExtras().getStringArrayList("raw values");

        for (String s : rawValues) {
            if (s.equals("begin"))
                gnuPlotInput += Integer.toString(gnuPlotXXAxis) + "\t";
            else if (s.equals("end")) {
                gnuPlotInput += "\n";
                gnuPlotXXAxis++;
            }
            else
                gnuPlotInput += s + "\t";
        }


        try {
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString();
            OutputStream out = null;
            outputFile = new File(path, "gnuPlotInput.dat");
            out = new FileOutputStream(outputFile);
            PrintWriter pw = new PrintWriter(out);
            pw.println(gnuPlotInput);
            pw.flush();
            pw.close();
        } catch (FileNotFoundException e) {
            Log.d("", "File not found Exception");
        } catch (IOException i) {
            Log.d("", "IO EXCEPTION");
        }*/

        String arr = "";



        for (int i=0; i < breakInsts.size() - 1 ; i++) {
            arr += breakInsts.get(i).toString() + "\t";
        }

        sessionDurText.setText(sessDur.toString());
        maxSessionDurText.setText(maxSess.toString());
        numBreaksText.setText(numBreaks.toString());
        lowerBondAtt.setText(lowerBond.toString());
        upperBondAtt.setText(upperBond.toString());

        if(breakInsts.size() > 0)
            breaksInstText.setText(arr);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //saveToServer();
                 sendDataToDB(session.getSessionID(), maxSess, numBreaks );
            }
        });

        sendButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail(outputFile);
            }
        });

        suggButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://play.google.com/store/search?q=neurofeedback&c=apps"));
                startActivity(intent);
            }
        });
    }

    protected void sendDataToDB(Integer a, Integer b, Integer c) {
        sendDataTask = new SendData(a, b, c);
        sendDataTask.execute((Void) null);
    }


    protected void sendEmail(File filelocation) {

        TextView sessionDurText = (TextView) findViewById(R.id.sessionDurText);
        TextView maxSessionDurText = (TextView) findViewById(R.id.maxSessionDurText);
        TextView numBreaksText = (TextView) findViewById(R.id.numBreaksText);
        TextView breaksInstText = (TextView) findViewById(R.id.breaksInstText);
        TextView lowerBondAtt = (TextView) findViewById(R.id.lowerBond);
        TextView upperBondAtt = (TextView) findViewById(R.id.upperBond);


        Uri path = Uri.fromFile(filelocation);
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        // set the type to 'email'
        emailIntent .setType("vnd.android.cursor.dir/email");
        String to[] = {"marianamv112@gmail.com"};
        emailIntent .putExtra(Intent.EXTRA_EMAIL, to);
        // the attachment
        emailIntent .putExtra(Intent.EXTRA_STREAM, path);
        // the mail subject
        String subject = user.getUsername();
        //Log.d("Results", "" + subject );

        // the email content
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Session Duration: " + sessionDurText.getText() + "\n" + "Max coutinuous work: " + maxSessionDurText.getText() + "\n" + "Nr of breaks: " + numBreaksText.getText() + "\n" + "Period of max attention: " + lowerBondAtt.getText() + " to " + upperBondAtt.getText() + "\n" + "Instants of breaks: " + breaksInstText.getText() + "\n");

        //Log.d(TAG, "The received message is: " + subject);
        emailIntent .putExtra(Intent.EXTRA_SUBJECT, subject);
        startActivity(Intent.createChooser(emailIntent , "Send email..."));
    }


    /*
    * this method is saving the name to ther server
    * */
   /* private void saveToServer() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving...");
        progressDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //if there is a success
                                //storing the name to sqlite with status synced
                                db.updateResults(result, SYNCED_WITH_SERVER);
                            } else {
                                //if there is some error
                                //saving the name to sqlite with status unsynced
                                db.updateResults(result, NOT_SYNCED_WITH_SERVER);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        //on error storing the name to sqlite with status unsynced
                        db.updateResults(result, NOT_SYNCED_WITH_SERVER);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                sendDataToDB(sessId, maxSess, numBreaks );
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }*/



    }



