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
	Music gameOverMusic;


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
		backgroundMusic.play();
		backgroundMusic.setLooping(true);
		gameOverMusic = Gdx.audio.newMusic(Gdx.files.internal("gameover_music.mp3"));
		gameOverMusic.setVolume(0.5f);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (!gameOver) {

			scrollingBackground.update();
			enemy.update(player.getDistance());
		}

		batch.begin();

		scrollingBackground.draw(batch);
		enemy.draw(batch);

		if (!gameOver) {
			player.update();
			player.draw(batch);
			enemy.update(player.getDistance());
			String distanceText = String.format("Distance: %.2f m", player.getDistance());
			font.draw(batch, distanceText, Gdx.graphics.getWidth() - 1900, Gdx.graphics.getHeight() - 20);

			if (player.isHitByBullet(enemy.getBullets())) {
				player.setHit(true);
				gameOver = true;
				backgroundMusic.stop();
				gameOverMusic.play();
			}
		} else {
			font.draw(batch, "Appuyer sur R pour Rejouer", (float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 10);
			batch.draw(gameOverTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

			if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
				resetGame();
			}
		}

		batch.end();
	}

	private void resetGame() {
		player.reset();
		enemy.reset();
		gameOver = false;
		gameOverMusic.stop();
		backgroundMusic.play();
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
