package com.winningticketproject.in.BestBallFlow;

public class Bestball_Player_list {

    public String getPlayer_name() {
        return player_name;
    }

    public String getScore() {
        return score;
    }

    public String getSelect_unselect() {
        return select_unselect;
    }

    public Bestball_Player_list(String player_name, String score, String select_unselect,String id,String net_Score) {
        this.player_name = player_name;
        this.score = score;
        this.select_unselect = select_unselect;
        this.id = id;
        this.net_Score = net_Score;
    }

    String player_name;
    String score;
    String select_unselect;

    public String getNet_Score() {
        return net_Score;
    }

    String net_Score;
    public String getId() {
        return id;
    }

    String id;
}
