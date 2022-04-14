package com.mygdx.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class flappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	//ShapeRenderer shapeRenderer;

	Texture[] birds;
	int flapState=0;
	float birdY = 0;
	float velocity=0;

	Circle birdCircle;
	Rectangle[] topRectangle,bottomRectangle;

	int gameState=0;
	int score=0;
	int scoringTube=0;
	float gravity=2;
	BitmapFont font;

	Texture toptube,bottomtube,gameover;
	float gap=400;
	float maxTubeoffset;
	Random randomGenerator;
	float tubeVelocity=4;
	int numberOfTubes=4;
	float[] tubeX=new float[numberOfTubes];
	float[] tubeOffset=new float[numberOfTubes];
	float distanceBetweenTubes;

	
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("background.png");
		gameover = new Texture("gameOver.png");
		birdCircle=new Circle();
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);
		topRectangle=new Rectangle[numberOfTubes];
		bottomRectangle=new Rectangle[numberOfTubes];
		//shapeRenderer=new ShapeRenderer();
		birds = new Texture[2];
		birds[0]=new Texture("birdup.png");
		birds[1]=new Texture("birddown.png");

		toptube=new Texture("toptube.png");
		bottomtube=new Texture("bottomtube.png");
		maxTubeoffset=Gdx.graphics.getHeight()/2-gap/2-100;
		randomGenerator= new Random();
		distanceBetweenTubes=Gdx.graphics.getWidth()*3/4;
		startGame();
	}

	public void startGame(){
		birdY=(Gdx.graphics.getHeight() / 2)-birds[0].getHeight()/2;
		for (int i=0; i<numberOfTubes; i++){
			tubeOffset[i]= (randomGenerator.nextFloat()-0.5f) * (Gdx.graphics.getHeight()-gap-200);
			tubeX[i]=Gdx.graphics.getWidth()/2-toptube.getWidth()/2+Gdx.graphics.getWidth()+ i * distanceBetweenTubes;
			topRectangle[i]=new Rectangle();
			bottomRectangle[i]=new Rectangle();
		}
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		if (gameState == 1) {
			if (tubeX[scoringTube] < Gdx.graphics.getWidth()/2){
				score++;
				Gdx.app.log("Score", String.valueOf(score));
				if (scoringTube < numberOfTubes -1){
					scoringTube++;
				} else {
					scoringTube=0;
				}
			}
			if (Gdx.input.justTouched()){
				velocity=-30;
			}
			for (int i=0; i<numberOfTubes; i++) {
				if (tubeX[i]<-toptube.getWidth()){
					tubeX[i]+= numberOfTubes * distanceBetweenTubes;
                    tubeOffset[i]= (randomGenerator.nextFloat()-0.5f) * (Gdx.graphics.getHeight()-gap-200);
				} else {
					tubeX[i] -= tubeVelocity;

				}
				batch.draw(toptube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
				batch.draw(bottomtube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeOffset[i]);

				topRectangle[i].set(tubeX[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],toptube.getWidth(),toptube.getHeight());
				bottomRectangle[i].set(tubeX[i],Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeOffset[i],bottomtube.getWidth(),bottomtube.getHeight());
			}
			if (birdY>0) {
				velocity += gravity;
				birdY -= velocity;
			} else {
			    gameState=2;
            }
		} else if (gameState == 0){
			if (Gdx.input.justTouched()){
				gameState=1;
			}
		} else if (gameState ==2){

		    batch.draw(gameover,Gdx.graphics.getWidth()/2 - gameover.getWidth()/2,Gdx.graphics.getHeight()/2 - gameover.getHeight()/2);
			if (Gdx.input.justTouched()){
				gameState=1;
				startGame();
				score=0;
				scoringTube=0;
				velocity=0;
			}
        }
		if (flapState == 0) {
			flapState = 1;
		} else {
			flapState = 0;
		}
		batch.draw(birds[flapState], (Gdx.graphics.getWidth() / 2) - birds[flapState].getWidth() / 2, birdY);

		font.draw(batch,String.valueOf(score),100,200);

		birdCircle.set(Gdx.graphics.getWidth()/2, birdY+ birds[flapState].getHeight()/2,birds[flapState].getWidth()/2);
		batch.end();



		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.RED);
		//shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);
		for (int i=0;i< numberOfTubes;i++){
			//shapeRenderer.rect(topRectangle[i].x,topRectangle[i].y,topRectangle[i].width,topRectangle[i].height);
			//shapeRenderer.rect(bottomRectangle[i].x,bottomRectangle[i].y,bottomRectangle[i].width,bottomRectangle[i].height);
			if (Intersector.overlaps(birdCircle,topRectangle[i]) || Intersector.overlaps(birdCircle,bottomRectangle[i])){
				gameState=2;

			}
		}
		//shapeRenderer.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
		//shapeRenderer.dispose();
	}
}
