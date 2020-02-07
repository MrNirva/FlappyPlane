package com.arcula.flappy.plane;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;

import java.util.Random;

public class FlappyPlane extends ApplicationAdapter {

	private SpriteBatch batch;

	private Texture background;
	private Texture player;
	private Texture enemy1;
	private Texture enemy2;
	private Texture enemy3;

	private float width;
	private float height;

	private float playerX = 0;
	private float playerY = 0;

	private int gameState = 0;
	//Oyun durumu değerimiz

	private float playerVelocity = 0;

	private int numberOfEnemies = 4;

	private float [] enemyX;

	private float distance = 0;

	private float [] enemyOffSet1;
	private float [] enemyOffSet2;
	private float [] enemyOffSet3;

	private Random random;

	private Circle playerCircle;

	private Circle[] enemyCircle1;
	private Circle[] enemyCircle2;
	private Circle[] enemyCircle3;

	private int score = 0;
	private int scoredEnemy = 0;

	private BitmapFont font;

	@Override
	public void create () {

		batch = new SpriteBatch();

		background = new Texture("backg.png");
		player = new Texture("player.png");
		enemy1 = new Texture("enemy1.png");
		enemy2 = new Texture("enemy2.png");
		enemy3 = new Texture("enemy3.png");


		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(5);

		// En ve Boy oranlarını alıyoruz
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();

		// Player konumu
		playerX = width/3;
		playerY = height/3;

		// Düşmanlar arası mesafe
		distance = width/2;

		// X ekseninde düşmanların rastgele gelmesi için değer atıyoruz
		enemyX = new float[numberOfEnemies];

		random = new Random();

		// X ekseninde düşmanların rastgele gelmesi için her biri için dizi oluşturuyoruz
		enemyOffSet1 = new float[numberOfEnemies];
		enemyOffSet2 = new float[numberOfEnemies];
		enemyOffSet3 = new float[numberOfEnemies];

		// Çarpışmaları anlayabilmek için görünmez bir daire çiziyoruz.
		playerCircle = new Circle();
		enemyCircle1 = new Circle[numberOfEnemies];
		enemyCircle2 = new Circle[numberOfEnemies];
		enemyCircle3 = new Circle[numberOfEnemies];

		for (int i =0; i<numberOfEnemies; i++){

			enemyX[i] = width +i * distance;

			enemyOffSet1[i] = random.nextInt(Gdx.graphics.getHeight());
			enemyOffSet2[i] = random.nextInt(Gdx.graphics.getHeight());
			enemyOffSet3[i] = random.nextInt(Gdx.graphics.getHeight());

			enemyCircle1[i] = new Circle();
			enemyCircle2[i] = new Circle();
			enemyCircle3[i] = new Circle();

		}
	}

	// Program başladığında sürekli çalışan yer
	@Override
	public void render () {

		batch.begin();
		batch.draw(background,0,0,width,height);

		if (gameState==1){

			if (enemyX[scoredEnemy]< playerX) {

				score++;

				if(scoredEnemy<numberOfEnemies-1){
					scoredEnemy++;
				}
				else{
					scoredEnemy =0;
				}

			}

			if (Gdx.input.justTouched()){
				playerVelocity = -9;
			}

			for (int i = 0; i<numberOfEnemies; i++){

				if (enemyX [i]< width /20){

					enemyX[i] = width + numberOfEnemies * distance;
					enemyOffSet1[i] = random.nextInt(Gdx.graphics.getHeight());
					enemyOffSet2[i] = random.nextInt(Gdx.graphics.getHeight());
					enemyOffSet3[i] = random.nextInt(Gdx.graphics.getHeight());

				}else{
					float enemyVelocitiy = 8;
					enemyX[i] = enemyX[i] - enemyVelocitiy;
				}

				batch.draw(enemy1,enemyX[i],enemyOffSet1[i],width/15,height/12);
				batch.draw(enemy2,enemyX[i],enemyOffSet2[i],width/15,height/12);
				batch.draw(enemy3,enemyX[i],enemyOffSet3[i],width/15,height/12);

				enemyCircle1[i] = new Circle(enemyX[i]+width/30,enemyOffSet1[i]+height/20,width/25);
				enemyCircle2[i] = new Circle(enemyX[i]+width/30,enemyOffSet2[i]+height/20,width/25);
				enemyCircle3[i] = new Circle(enemyX[i]+width/30,enemyOffSet3[i]+height/20,width/25);

			}

			if (playerY >0){

				float gravity = 1f;
				// Yer çekimi değerimiz

				playerVelocity = playerVelocity + gravity;
				playerY = playerY - playerVelocity;

			}
			else{
				gameState = 2;
			}

		}else if (gameState == 0){

			if (Gdx.input.justTouched()){
				gameState = 1;
			}

		}else  if (gameState == 2){

			font.draw(batch,"Oyun Bitti!",height/2,width/3);
			font.draw(batch,"Tikla ve Tekrar Oyna!",height/3,width/4);

			if(Gdx.input.justTouched()){

				score = 0;
				playerVelocity =0;

				gameState = 1;

				playerY = height/2;


				for (int i =0; i<numberOfEnemies; i++){

					enemyX[i] = width +i * distance;

					enemyOffSet1[i] = random.nextInt(Gdx.graphics.getHeight());
					enemyOffSet2[i] = random.nextInt(Gdx.graphics.getHeight());
					enemyOffSet3[i] = random.nextInt(Gdx.graphics.getHeight());

					enemyCircle1[i] = new Circle();
					enemyCircle2[i] = new Circle();
					enemyCircle3[i] = new Circle();

				}
			}

			scoredEnemy = 0;

		}


		batch.draw(player, playerX, playerY,width/15,height/12);
		font.draw(batch,String.valueOf(score),width/2,height/2+height/3);
		batch.end();
		playerCircle.set(playerX +width/30, playerY +height/24,width/30);

		for (int i=0; i < numberOfEnemies; i++){

			if(Intersector.overlaps(playerCircle,enemyCircle1[i])||Intersector.overlaps(playerCircle,enemyCircle2[i])
					|| Intersector.overlaps(playerCircle,enemyCircle3[i])) {

				gameState = 2;

			}
		}

	}

	@Override
	public void dispose () {}

}