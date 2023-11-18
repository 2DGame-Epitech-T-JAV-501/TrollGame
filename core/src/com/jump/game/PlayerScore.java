package com.jump.game;

public class PlayerScore {
    private String pseudo;
    private int score;

    public PlayerScore(String pseudo, int score) {
        this.pseudo = pseudo;
        this.score = score;
    }
    public String getPseudo() { return pseudo; }
    public int getScore() { return score; }
}
