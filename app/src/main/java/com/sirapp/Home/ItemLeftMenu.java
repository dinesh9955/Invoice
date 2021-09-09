package com.sirapp.Home;

import java.io.Serializable;

/**
 * Created by AnaadIT on 10/5/2017.
 */

public class ItemLeftMenu implements Serializable {

    String name = "";
    int icon = 0;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
