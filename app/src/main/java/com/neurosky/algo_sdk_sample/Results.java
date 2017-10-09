package com.neurosky.algo_sdk_sample;

import java.io.Serializable;

/**
 * Created by mariana on 10/09/17.
 */

public class Results implements Serializable {
    public static Results getResults() {
        return results;
    }

    public static void setResults(Results results) {
        Results.results = results;
    }

    private static Results results;

    public User getResUser() {
        return resUser;
    }

    public void setResUser(User resUser) {
        this.resUser = resUser;
    }

    User resUser=null;


    public Integer getResSessionID() {
        return resSessionID;
    }

    public void setResSessionID(Integer resSessionID) {
        this.resSessionID = resSessionID;
    }

    Integer resSessionID=0;

    public Integer getResMaxWorkDuration() {
        return resMaxWorkDur;
    }

    public void setResMaxWorkDuration(Integer resMaxWorkDuraxWorkDur) {
        this.resMaxWorkDur = resMaxWorkDur;
    }

    Integer resMaxWorkDur=null;

    public Integer getResNumOfBreaks() {
        return resNumOfBreaks;
    }

    public void resSetNumOfBreaks(Integer resNumOfBreaks) {
        this.resNumOfBreaks = resNumOfBreaks;
    }

    Integer resNumOfBreaks=null;

    public Results(User resUser, Integer resSessionID, Integer resMaxWork, Integer resnBreaks){
        this.resUser = resUser;
        this.resSessionID=resSessionID;
        this.resMaxWorkDur=resMaxWork;
        this.resNumOfBreaks=resnBreaks;
    }

}
