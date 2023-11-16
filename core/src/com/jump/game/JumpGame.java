package com.jump.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;


import java.util.ArrayList;

public class JumpGame extends ApplicationAdapter {
	SpriteBatch batch;
	Player player;
	Enemy enemy;
	ScrollingBackground scrollingBackground;
	private BitmapFont font;
	private boolean gameOver = false;
	private Texture gameOverTexture;
	Music backgroundMusic;
	private Sound shootSound;


	@Override
	public void create () {
		batch = new SpriteBatch();
		player = new Player();
		shootSound = Gdx.audio.newSound(Gdx.files.internal("shootsound.mp3"));
		enemy = new Enemy(shootSound);
		scrollingBackground = new ScrollingBackground();
		font = new BitmapFont();
		gameOver = false;
		gameOverTexture = new Texture("gameover.png");
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
		backgroundMusic.setVolume(0.1f);
		backgroundMusic.setLooping(true);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		scrollingBackground.update();

		batch.begin();

		scrollingBackground.draw(batch);

		enemy.update();
		enemy.draw(batch);

		if (!gameOver) { // Vérifiez si le jeu est toujours en cours
			player.update();
			player.draw(batch);

			if (player.isHitByBullet(enemy.getBullets())) {
				// Le joueur est touché par une balle, définir l'écran de fin de jeu ici
				ArrayList<Bullet> enemyBullets = enemy.getBullets();
				player.setHit(true);


				// Arrêtez la mise à jour du jeu
				gameOver = true;
			}
		} else {
			// Le jeu est en mode "game over"
			// Affichez l'écran de fin de jeu ici
			// Par exemple, vous pouvez afficher un message "Game Over" à la place du message précédent
			font.draw(batch, "Appuyer sur R pour Rejouer", (float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 10);
			batch.draw(gameOverTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

			// Ajoutez une touche ou un événement pour redémarrer le jeu
			if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
				// Réinitialisez les états du jeu pour redémarrer
				player.reset();
				enemy.reset();
				gameOver = false;
			}
		}
		if (!backgroundMusic.isPlaying()) {
			backgroundMusic.play();
			backgroundMusic.setLooping(true);
		}

		batch.end();
	}


	@Override
	public void dispose () {
		batch.dispose();
		enemy.getTexture().dispose();
		player.getTexture().dispose();
		font.dispose();
		backgroundMusic.dispose();
		shootSound.dispose();
	}
}
