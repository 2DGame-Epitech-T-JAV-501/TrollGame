package com.jump.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;


import java.util.ArrayList;
import java.util.Iterator;

public class JumpGame extends ApplicationAdapter {
	SpriteBatch batch;
	Player player;
	Enemy enemy;
	ScrollingBackground scrollingBackground;
	private BitmapFont font;
	private boolean gameOver = false;
	private Texture gameOverTexture;
	private Texture winTexture;
	Music backgroundMusic;
	private Sound shootSound;
	Music gameOverMusic;
	private ArrayList<Collectible> collectibles;
	private float distancePourNouveauCollectible = 10.0f;
	private float dernierCollectibleGenere = 0.0f;
	private int collectedItems = 0;
	private final int WINNING_COLLECTIBLE_COUNT = 1;
	private boolean playerWon = false;


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
		winTexture = new Texture("WinTexture.png");
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
		backgroundMusic.setVolume(0.1f);
		backgroundMusic.play();
		backgroundMusic.setLooping(true);
		gameOverMusic = Gdx.audio.newMusic(Gdx.files.internal("gameover_music.mp3"));
		gameOverMusic.setVolume(0.5f);
		collectibles = new ArrayList<Collectible>();
		collectibles.add(new Collectible(new Texture("collectible.png"), 1900, 200,200));
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();

		// Dessiner l'arrière-plan et l'ennemi indépendamment de l'état du jeu
		scrollingBackground.draw(batch);
		enemy.draw(batch);

		if (!gameOver) {
			scrollingBackground.update();
			enemy.update(player.getDistance());
			player.update();
			player.draw(batch);

			String distanceText = String.format("Distance: %.2f m", player.getDistance());
			font.draw(batch, distanceText, Gdx.graphics.getWidth() - 1900, Gdx.graphics.getHeight() - 20);
			font.draw(batch, "Argent: " + player.getMoney(), 20, Gdx.graphics.getHeight() - 50);

			// Gérer les collectibles
			float distanceParcourue = player.getDistance();
			if (distanceParcourue - dernierCollectibleGenere >= distancePourNouveauCollectible) {
				collectibles.add(new Collectible(new Texture("collectible.png"), Gdx.graphics.getWidth(), 200, 100));
				dernierCollectibleGenere = distanceParcourue;
			}

			Iterator<Collectible> iter = collectibles.iterator();
			while (iter.hasNext()) {
				Collectible collectible = iter.next();
				collectible.update(Gdx.graphics.getDeltaTime());
				collectible.draw(batch);

				if (player.getBounds().overlaps(collectible.getBounds())) {
					iter.remove();
					player.addMoney(100);
					collectedItems++;

					if (collectedItems >= WINNING_COLLECTIBLE_COUNT) {
						playerWon = true;
						gameOver = true;
						backgroundMusic.stop();
					}
				}
			}

			// Vérifier si le joueur est touché par une balle
			if (player.isHitByBullet(enemy.getBullets())) {
				player.setHit(true);
				gameOver = true;
				playerWon = false;
				backgroundMusic.stop();
				gameOverMusic.play();
			}
		} else {
			if (playerWon) {
				batch.draw(winTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			} else {
				batch.draw(gameOverTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			}
			font.draw((Batch) batch, (CharSequence) "Appuyer sur R pour Rejouer", (float) ((float) Gdx.graphics.getWidth() / 2.2), (float) ((float) Gdx.graphics.getHeight()/1.1));

			if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
				resetGame();
			}
		}

		batch.end();
	}


	private void resetGame() {
		player.reset();
		enemy.reset();
		collectibles.clear();
		dernierCollectibleGenere = 0.0f;
		gameOver = false;
		gameOverMusic.stop();
		backgroundMusic.play();
		collectedItems = 0;

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
