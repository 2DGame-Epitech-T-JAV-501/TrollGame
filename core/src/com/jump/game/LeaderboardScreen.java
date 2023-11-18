package com.jump.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class LeaderboardScreen implements Screen {
    private Stage stage;
    private TextButton backButton;

    public LeaderboardScreen() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        // Initialisation de la liste des scores ou récupération depuis une source de données
        // Créez et affichez la liste des scores ici

        // Bouton pour revenir au menu principal
        backButton = new TextButton("Retour", new Skin(Gdx.files.internal("uiskin.json")));
        backButton.setSize(200, 50);
        backButton.setPosition(10, Gdx.graphics.getHeight() - 60);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Revenez au menu principal lorsque le bouton "Retour" est cliqué
                dispose();
                // Ajoutez ici la logique pour revenir au menu principal
            }
        });
        stage.addActor(backButton);
    }

    @Override
    public void show() {
        // Gestion de l'affichage de l'écran de classement
        // Appelez cette méthode lorsque le bouton "Classements" est cliqué
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        // Affichage de l'écran de classement
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        // Redimensionnez l'écran de classement si nécessaire
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        // Nettoyez les ressources de l'écran de classement
        stage.dispose();
    }
    public Stage getStage() {
        return stage;
    }
}

