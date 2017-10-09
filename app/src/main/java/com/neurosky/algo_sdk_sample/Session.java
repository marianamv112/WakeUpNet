package com.neurosky.algo_sdk_sample;

import java.io.Serializable;

/**
 * Created by mariana on 22/08/17.
 */

public final class Session implements Serializable {
    public static Session getSession() {
        return session;
    }

    public static void setSession(Session session) {
        Session.session = session;
    }

    private static Session session;


    public Integer getSessionID() {
        return sessionID;
    }

    public void setSessionID(Integer sessionID) {
        this.sessionID = sessionID;
    }

    Integer sessionID=0;

    public Integer getMaxWorkDuration() {
        return maxWorkDur;
    }

    public void setMaxWorkDuration(Integer maxWorkDur) {
        this.maxWorkDur = maxWorkDur;
    }

    Integer maxWorkDur=null;

    public Integer getNumOfBreaks() {
        return numOfBreaks;
    }

    public void setNumOfBreaks(Integer numOfBreaks) {
        this.numOfBreaks = numOfBreaks;
    }

    Integer numOfBreaks=null;


    public Session(Integer sessionID, Integer maxWork, Integer nBreaks){
        this.sessionID=sessionID;
        this.maxWorkDur=maxWork;
        this.numOfBreaks=nBreaks;
    }
}
