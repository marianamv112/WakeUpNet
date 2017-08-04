package com.neurosky.algo_sdk_sample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ResultsActivity extends ActionBarActivity {

    File outputFile;
    String gnuPlotInput = "";
    int gnuPlotXXAxis = 0;

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

        Bundle b;
        b = getIntent().getExtras();
        Integer sessDur = b.getInt("session duration");
        Integer maxSess = b.getInt("max duration time");
        Integer numBreaks = b.getInt("num of breaks");
        Integer lowerBond = b.getInt("lower Bond");
        Integer upperBond = b.getInt("upper Bond");

        ArrayList<Integer> breakInsts = b.getIntegerArrayList("break instants");
        ArrayList<String> rawValues = b.getStringArrayList("raw values");

        Log.d("", rawValues.toString());

        for (String s : rawValues) {
            if (s.equals("begin"))
                gnuPlotInput += Integer.toString(gnuPlotXXAxis) + "\t";
            else if (s.equals("end")) {
                gnuPlotInput += "\n";
                gnuPlotXXAxis++;
            }
            else
                gnuPlotInput += s + "\t";
            //Log.d("", gnuPlotInput);
        }
        /*for(int i = 0; i < rawValues.size(); i++) {
            if (rawValues.get(i).equals("begin"))
                gnuPlotInput += Integer.toString(gnuPlotXXAxis) + "\t";
            else if (s=="end") {
                gnuPlotInput += "\n";
                gnuPlotXXAxis++;
            }
            else
                gnuPlotInput += s + "\t";
            Log.d("", gnuPlotInput);
        }*/

        try {
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).toString();

            OutputStream out = null;

            outputFile = new File(path, "gnuPlotInput.dat");

            //if (outputFile.exists())
            //    Log.d(TAG, "File created");


            out = new FileOutputStream(outputFile);

            PrintWriter pw = new PrintWriter(out);
            pw.println(gnuPlotInput);
            pw.flush();
            pw.close();

            //sendEmail(outputFile);

        } catch (FileNotFoundException e) {
            Log.d("", "File not found Exception");
        } catch (IOException i) {
            Log.d("", "IO EXCEPTION");
        }

        String arr = "";

        //Log.d("...", "BreakInstants3: " + breakInsts.toString());

        for (int i=0; i < breakInsts.size() - 1 ; i++) {
            arr += breakInsts.get(i).toString() + "\t";
            //Log.d("...", "Array of Breaks: " + breakInsts.get(i).toString());
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
                sendEmail(outputFile);
                //sendEmail();
            }
        });
        }

        //protected void sendEmail(File filelocation) {

    protected void sendEmail(File filelocation) {

        TextView sessionDurText = (TextView) findViewById(R.id.sessionDurText);
        TextView maxSessionDurText = (TextView) findViewById(R.id.maxSessionDurText);
        TextView numBreaksText = (TextView) findViewById(R.id.numBreaksText);
        TextView breaksInstText = (TextView) findViewById(R.id.breaksInstText);
        TextView lowerBondAtt = (TextView) findViewById(R.id.lowerBond);
        TextView upperBondAtt = (TextView) findViewById(R.id.upperBond);
        //Button sendButton = (Button) findViewById(R.id.sendButton);

        Uri path = Uri.fromFile(filelocation);
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        // set the type to 'email'
        emailIntent .setType("vnd.android.cursor.dir/email");
        String to[] = {"marianamv112@gmail.com"};
        emailIntent .putExtra(Intent.EXTRA_EMAIL, to);
        // the attachment
        emailIntent .putExtra(Intent.EXTRA_STREAM, path);
        // the mail subject
        String subject = getIntent().getStringExtra("subject");
        //Log.d("Results", "" + subject );

        // the email content
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Session Duration: " + sessionDurText.getText() + "\n" + "Max coutinuous work: " + maxSessionDurText.getText() + "\n" + "Nr of breaks: " + numBreaksText.getText() + "\n" + "Period of max attention: " + lowerBondAtt.getText() + " to " + upperBondAtt.getText() + "\n" + "Instants of breaks: " + breaksInstText.getText() + "\n");

        //Log.d(TAG, "The received message is: " + subject);
        emailIntent .putExtra(Intent.EXTRA_SUBJECT, subject);
        startActivity(Intent.createChooser(emailIntent , "Send email..."));
    }
 }


