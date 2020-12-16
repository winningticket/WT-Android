package com.winningticketproject.in.BestBallFlow;

public class BestBall_player_info {

    public BestBall_player_info(String name, String score, String selected,String net_score) {
        this.name = name;
        this.score = score;
        this.selected = selected;
        this.net_score = net_score;
    }

    public String getName() {
        return name;
    }

    public String getScore() {
        return score;
    }

    String name;
    String score;

    public String getSelected() {
        return selected;
    }

    String selected;

    public String getNet_score() {
        return net_score;
    }

    String net_score;


}
