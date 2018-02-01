package com.p2_vaio;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture birds[];

	int flap_state=0;
	float accumulator = 0f;

	float birdY=0;
	float velocity = 0;
	float gravity =2;

	int gamestate=0;

	//runs one time on run
	@Override
	public void create () {
		batch = new SpriteBatch();
		birds = new Texture[2];
		//setting the background to bg image
		background= new Texture("bg.png");
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");

		birdY=Gdx.graphics.getHeight()/2-birds[flap_state].getHeight()/2;
	}

	//happens continously as the app run
	@Override
	public void render () {

		if(gamestate!=0) {

			//detecting the tap
			if(Gdx.input.justTouched()){
				//birdY++;
				velocity=-20;
			}

			if(birdY>0||velocity<0){

				velocity+=gravity;
				birdY-=velocity;
			}



		}else{
			if(Gdx.input.justTouched()){

				//birdY++;
				gamestate=1;
			}
		}

		float dt = Gdx.graphics.getDeltaTime(); //dt is the number of seconds that have passed in between each frame
		accumulator += dt;
		if (accumulator > 0.15)//one second has passed
		{
			if (flap_state == 0)
				flap_state = 1;
			else
				flap_state = 0;
			accumulator -= 0.15;
		}
		batch.begin();
		//repeatedlt displaying it on screen
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.draw(birds[flap_state], Gdx.graphics.getWidth() / 2 - birds[flap_state].getWidth() / 2, birdY);
		batch.end();




	}
	
	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
	}
}
