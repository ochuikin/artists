package com.obabichev.artists.model;


import java.io.Serializable;

/**
 * Created by olegchuikin on 13/04/16.
 */
public class Cover implements Serializable{
    private String small;
    private String big;

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getBig() {
        return big;
    }

    public void setBig(String big) {
        this.big = big;
    }
}
