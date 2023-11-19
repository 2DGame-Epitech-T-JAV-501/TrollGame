package com.jump.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ScoreUtils {

    public static void savePlayerScore(PlayerScore score) {
        FileHandle file = Gdx.files.local("scores.txt");
        try {
            file.writeString(score.getPseudo() + "," + score.getDistance() + "," + score.getMoney() + "\n", true); // 'true' pour append
        } catch (Exception e) {
            System.err.println("Erreur lors de l'écriture dans scores.txt : " + e.getMessage());
        }
    }

    public static List<PlayerScore> loadPlayerScores() {
        List<PlayerScore> scores = new ArrayList<>();
        FileHandle file = Gdx.files.local("scores.txt");

        if (!file.exists()) {
            return scores; // Le fichier n'existe pas, retourne une liste vide
        }

        try {
            String[] lines = file.readString().split("\n");
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String pseudo = parts[0];
                    float distance = Float.parseFloat(parts[1]);
                    int money = Integer.parseInt(parts[2]);
                    scores.add(new PlayerScore(pseudo, distance, money));
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la lecture de scores.txt : " + e.getMessage());
        }
        return scores;
    }
}

