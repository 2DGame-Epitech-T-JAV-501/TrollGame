package com.jump.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.List;

public class LeaderboardScreen {
    private List<PlayerScore> scores;
    private BitmapFont font;
    private SpriteBatch batch;
    private GlyphLayout layout = new GlyphLayout();
    private Stage stage;
    private Skin skin;
    private JumpGame game;

    public LeaderboardScreen(JumpGame game) {
        this.scores = ScoreUtils.loadPlayerScores(); // Charge les scores depuis le fichier
        this.font = new BitmapFont();
        this.batch = new SpriteBatch();
        stage = new Stage();
        skin = new Skin(Gdx.files.internal("uiskin.json")); // Assurez-vous d'avoir un skin
        createBackButton();
        this.game = game;
    }
    private void createBackButton() {
        TextButton backButton = new TextButton("Retour", skin);
        backButton.setSize(200, 50);
        backButton.setPosition((float) Gdx.graphics.getWidth() / 2 - 100, 50);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.changeState(JumpGame.GameState.MENU);
            }
        });

        stage.addActor(backButton);
    }

    public void render(float deltaTime) {
        if (game.getCurrentState() == JumpGame.GameState.LEADERBOARD) {
            Gdx.input.setInputProcessor(stage); // Définissez le processeur d'entrée
        }
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
        stage.act();
        stage.draw();

        batch.end();
    }

    public void dispose() {
        font.dispose();
        batch.dispose();
        stage.dispose();
        skin.dispose();
    }
}

