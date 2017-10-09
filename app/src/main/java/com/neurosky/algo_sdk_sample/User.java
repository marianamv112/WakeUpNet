package com.neurosky.algo_sdk_sample;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mariana on 22/08/17.
 */

public final class User implements Serializable{
    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        User.user = user;
    }

    private static User user;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    String username=null;

    protected ArrayList<Session> sessions = null;

    public ArrayList<Session> getSessions() {
        return sessions;
    }

    public void setSessions(ArrayList<Session> listOfSessions) {
        this.sessions=listOfSessions;
    }



    public User(String u){
        this.username=u;
        this.sessions=new ArrayList<Session>();
    }

}
