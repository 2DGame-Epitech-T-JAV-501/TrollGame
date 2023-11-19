package com.jump.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.List;

public class LeaderboardScreen {
    private List<PlayerScore> scores;
    private BitmapFont font;
    private SpriteBatch batch;
    private GlyphLayout layout = new GlyphLayout();

    public LeaderboardScreen() {
        this.scores = ScoreUtils.loadPlayerScores(); // Charge les scores depuis le fichier
        this.font = new BitmapFont();
        this.batch = new SpriteBatch();
    }

    public void render(float deltaTime) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        int y = Gdx.graphics.getHeight() - 50;
        for (PlayerScore score : scores) {
            String text = "Pseudo: " + score.getPseudo() + ", Distance: " + String.format("%.2f", score.getDistance()) + " m, Argent: " + score.getMoney() + " pièces";
            layout.setText(font, text); // Calcule la largeur du texte
            float x = (Gdx.graphics.getWidth() - layout.width) / 2; // Calcule la position x pour centrer le texte
            font.draw(batch, text, x, y);
            y -= 30; // Décale chaque ligne vers le bas
        }
        batch.end();
    }

    public void dispose() {
        font.dispose();
        batch.dispose();
    }
}

