package com.winningticketproject.in.Shamble_flow;

public class Shamble_player_info {

    public Shamble_player_info(String name, String score,String selected) {
        this.name = name;
        this.score = score;
        this.selected = selected;
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


}
