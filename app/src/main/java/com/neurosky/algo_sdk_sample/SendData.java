package com.neurosky.algo_sdk_sample;

import android.os.AsyncTask;

/**
 * Created by mariana on 22/08/17.
 */

public class SendData extends AsyncTask<Void, Void, Boolean> {

    private final Integer msessionID;
    private final Integer mmaxWorkDur;
    private final Integer mnumOfBreaks;


    public SendData(Integer sessionID, Integer maxWorkDur, Integer numOfBreaks) {
        msessionID = sessionID;
        mmaxWorkDur = maxWorkDur;
        mnumOfBreaks = numOfBreaks;
    }

    SendData connectServer;

    @Override
    protected Boolean doInBackground(Void... params) {
        RestClient client = new RestClient("http://192.168.43.246:3000/sendData");
        client.AddParam("sessionID", Integer.toString(msessionID) );
        client.AddParam("maxWorkDur", Integer.toString(mmaxWorkDur) );
        client.AddParam("numOfBreaks", Integer.toString(mnumOfBreaks) );
        try {
            client.Execute(RequestMethod.POST);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String response = client.getResponse();
        if(!response.contains("OK")){
            Session.getSession().setMaxWorkDuration(mmaxWorkDur);
            Session.getSession().setNumOfBreaks(mnumOfBreaks);
            return true;
        }else {
            return false;
        }
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        connectServer = null;

        if (success) {

            System.out.println("Dados enviados para o servidor");
        } else {
            System.out.println("Falha no envio dos dados para o servidor");
        }
    }

    @Override
    protected void onCancelled() {
        connectServer = null;
    }
}
