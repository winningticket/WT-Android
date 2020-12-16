package com.winningticketproject.in.AppInfo;

/**
 * Created by carmatec on 10/10/17.
 */

public class parent_getter_setter {
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTotal_score() {
        return total_score;
    }

    public String getPar_score() {
        return par_score;
    }

    public parent_getter_setter(String user_postion,String id, String name, String total_score, String par_score,String enebled,String individula_player,String thru) {
        this.id = id;
        this.name = name;
        this.total_score = total_score;
        this.par_score = par_score;
        this.user_postion = user_postion;
        this.enebled = enebled;
        this.individula_player = individula_player;
        this.THRU = thru;
    }

    String id;
    String name;
    String total_score;
    String par_score;

    public String getEnebled() {
        return enebled;
    }

    String enebled;

    public String getUser_postion() {
        return user_postion;
    }

    String user_postion;

    public String getIndividula_player() {
        return individula_player;
    }

    String individula_player;

   public String THRU;

}
