package com.jump.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.Iterator;

public class Enemy {
    private Texture texture;
    private float x, y;
    private ArrayList<Bullet> bullets;
    private float timeSinceLastShot = 0;
    private Sound shootSound;

    public Enemy(Sound shootSound) {
        texture = new Texture("1_police_attack_Attack2_000.png");
        x = 100;
        y = 200;
        bullets = new ArrayList<Bullet>();
        this.shootSound = shootSound;
    }

    public void update() {
        // Tirer une balle toutes les 2 secondes
        timeSinceLastShot += Gdx.graphics.getDeltaTime();
        if (timeSinceLastShot >= 2) {
            bullets.add(new Bullet(x + texture.getWidth(), y + 10));
            timeSinceLastShot = 0;

                shootSound.play(); // Joue le son du tir

        }

        // Mettre à jour les balles
        Iterator<Bullet> iter = bullets.iterator();
        while (iter.hasNext()) {
            Bullet bullet = iter.next();
            bullet.update();
            if (bullet.isOffScreen()) {
                iter.remove();
            }
        }
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, x, y);
        for (Bullet bullet : bullets) {
            bullet.draw(batch);
        }
    }

    public Texture getTexture() {
        return texture;
    }

    public ArrayList<Bullet> getBullets() {
        return bullets;
    }
    public void reset() {
        x = 100;
        y = 200;
        bullets.clear();
    }
}
