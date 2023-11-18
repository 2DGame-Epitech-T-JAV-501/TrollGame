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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;


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
	private Stage stage;
	private boolean inMenu = true;
	enum GameState {
		MENU,
		ENTER_PSEUDO,
		PLAYING
	}

	private GameState currentState = GameState.MENU;
	private TextField pseudoInputField;


	@Override
	public void create () {
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		createMenu();
		createPseudoInputField();
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
	private void createMenu() {
		TextButton playButton = new TextButton("Jouer", new Skin(Gdx.files.internal("uiskin.json")));
		playButton.setSize(200, 50); // Définissez la taille du bouton
		float x = (Gdx.graphics.getWidth() - playButton.getWidth()) / 2;
		float y = (Gdx.graphics.getHeight() - playButton.getHeight()) / 2;
		playButton.setPosition(x, y);
		playButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				currentState = GameState.ENTER_PSEUDO;
			}
		});
		stage.addActor(playButton);
	}

	private void createPseudoInputField() {
		pseudoInputField = new TextField("", new Skin(Gdx.files.internal("uiskin.json")));
		pseudoInputField.setMessageText("Enter Pseudo");
		pseudoInputField.setSize(200, 50); // Définissez la taille du champ de texte
		float x = (Gdx.graphics.getWidth() - pseudoInputField.getWidth()) / 2;
		float y = (Gdx.graphics.getHeight() - pseudoInputField.getHeight()) / 2;
		pseudoInputField.setPosition(x, y);
		pseudoInputField.setVisible(false); // Invisible jusqu'à ce que le joueur appuie sur "Jouer"
		stage.addActor(pseudoInputField);
	}
	private boolean pseudoEntered() {
		return Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && !pseudoInputField.getText().isEmpty();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		switch (currentState) {
			case MENU:
				// Gérer l'affichage du menu
				stage.act(Gdx.graphics.getDeltaTime());
				stage.draw();
				break;

			case ENTER_PSEUDO:
				// Gérer la saisie du pseudo
				pseudoInputField.setVisible(true);
				stage.act(Gdx.graphics.getDeltaTime());
				stage.draw();

				if (pseudoEntered()) {
					// Une fois le pseudo saisi, passer à l'état de jeu
					currentState = GameState.PLAYING;
					pseudoInputField.setVisible(false);
				}
				break;

			case PLAYING:
				// Gérer la logique du jeu
				batch.begin();
				scrollingBackground.draw(batch);
				enemy.draw(batch);

				if (!gameOver) {
					handleGameLogic();
				} else {
					handleEndGameScreen();
				}
				batch.end();
				break;
		}
	}

	private void handleCollectibles() {
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
	}

	private void handleGameOver() {
		player.setHit(true);
		gameOver = true;
		playerWon = false;
		backgroundMusic.stop();
		gameOverMusic.play();
	}

	private void handleEndGameScreen() {
		if (playerWon) {
			batch.draw(winTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		} else {
			batch.draw(gameOverTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		}
		font.draw(batch, "Appuyer sur R pour Rejouer", (float) ((float) Gdx.graphics.getWidth() / 2.2), (float) ((float) Gdx.graphics.getHeight()/1.1));

		if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
			resetGame();
		}
	}

	private void handleGameLogic() {
		scrollingBackground.update();
		enemy.update(player.getDistance());
		player.update();
		player.draw(batch);

		String distanceText = String.format("Distance: %.2f m", player.getDistance());
		font.draw(batch, distanceText, Gdx.graphics.getWidth() - 1900, Gdx.graphics.getHeight() - 20);
		font.draw(batch, "Argent: " + player.getMoney(), 20, Gdx.graphics.getHeight() - 50);

		handleCollectibles();

		if (player.isHitByBullet(enemy.getBullets())) {
			handleGameOver();
		}
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
