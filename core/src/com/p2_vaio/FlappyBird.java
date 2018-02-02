package com.p2_vaio;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture birds[];
	Texture toptube,bottomtube;
	Texture gameOver;

	int flap_state=0;
	float accumulator = 0f;
	float accumulator1 = 0f;

	float birdY=0;
	float velocity = 0;
	float gravity =2;

	Circle birdCircle;
	Rectangle toptuberectangle[];
	Rectangle bottomtuberectangle[];


	//batch(just for textures) for shapes
	//ShapeRenderer shapeRenderer ;

	int gamestate=0;

	float gap =400;
	float maxtubeofset;

	float tubevelocity =4;
	int no_of_tubes =4;
	float tubeX[] = new float[no_of_tubes];
	float tubeofset[]= new float[no_of_tubes];
	float distancebetweentubes;

	Random random;

	int score;
	int scoringtube;
	BitmapFont bitmapFont;

	//runs one time on run
	@Override
	public void create () {
		batch = new SpriteBatch();
		birds = new Texture[2];

		//shapeRenderer = new ShapeRenderer();
		birdCircle = new Circle();

		toptuberectangle = new Rectangle[no_of_tubes];
		bottomtuberectangle = new Rectangle[no_of_tubes];
		gameOver = new Texture("flappy-bird-game-over.png");

        score=0;
        scoringtube=0;
        bitmapFont = new BitmapFont();
        bitmapFont.setColor(Color.WHITE);
        bitmapFont.getData().setScale(7);

		//setting the background to bg image
		background= new Texture("bg.png");
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");
		toptube = new Texture("toptube.png");
		bottomtube = new Texture("bottomtube.png");



		maxtubeofset=Gdx.graphics.getHeight()/2-gap/2-100;
		random = new Random();
		distancebetweentubes=Gdx.graphics.getWidth()*3/4;

		startGame();

	}

	public void startGame(){
		birdY=Gdx.graphics.getHeight()/2-birds[flap_state].getHeight()/2;

		for(int i=0;i<no_of_tubes;i++){
			tubeofset[i] = (random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-gap-200);
			tubeX[i] = Gdx.graphics.getWidth()/2-toptube.getWidth()/2 +Gdx.graphics.getWidth() +i*distancebetweentubes;

			toptuberectangle[i] = new Rectangle();
			bottomtuberectangle[i] = new Rectangle();


		}
	}

	//happens continously as the app run
	@Override
	public void render () {

		batch.begin();
		//repeatedlt displaying it on screen
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if(gamestate==1) {

			if(tubeX[scoringtube]<Gdx.graphics.getWidth()/2){
				score++;
				Gdx.app.log("Collision","No"+" "+score);
				if(scoringtube<no_of_tubes-1)
					scoringtube++;
				else
					scoringtube=0;
			}

			//detecting the tap
			if(Gdx.input.justTouched()){
 				//birdY++;
				velocity=-20;


			}

			for(int i=0;i<no_of_tubes;i++) {

				if(tubeX[i]<-toptube.getWidth()){

					tubeofset[i] = (random.nextFloat()-0.5f)*(Gdx.graphics.getHeight()-gap-200);
					tubeX[i]+=no_of_tubes*distancebetweentubes;

				}else{
					tubeX[i] -= tubevelocity;

				}


				batch.draw(toptube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeofset[i]);
				batch.draw(bottomtube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeofset[i]);

				toptuberectangle[i] = new Rectangle(tubeX[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeofset[i],toptube.getWidth(),toptube.getHeight());
				bottomtuberectangle[i]= new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeofset[i],bottomtube.getWidth(),bottomtube.getHeight());

			}

			if(birdY>0){

				velocity+=gravity;
				birdY-=velocity;
			}else{
				gamestate=2;
			}

		}else if (gamestate==0){
			if(Gdx.input.justTouched()){

				//birdY++;
				gamestate=1;
			}
		}else if(gamestate==2){

			batch.draw(gameOver, Gdx.graphics.getWidth() / 2 - gameOver.getWidth() / 2, Gdx.graphics.getHeight()/2-gameOver.getHeight()/2);

			float dt = Gdx.graphics.getDeltaTime(); //dt is the number of seconds that have passed in between each frame
			accumulator1 += dt;
			if (accumulator1 > 2)//one second has passed
			{
				if(Gdx.input.justTouched()){
					//birdY++;
					gamestate=1;
					startGame();
					score=0;
					scoringtube=0;
					velocity=0;

				}
				accumulator1 -= 0.15;
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




		batch.draw(birds[flap_state], Gdx.graphics.getWidth() / 2 - birds[flap_state].getWidth() / 2, birdY);

		bitmapFont.draw(batch,String.valueOf(score),100,200);



		birdCircle.set(Gdx.graphics.getWidth()/2,birdY+birds[flap_state].getHeight()/2,birds[flap_state].getHeight()/2);

		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.RED);

		//shapeRenderer.circle(birdCircle.x , birdCircle.y,birdCircle.radius);
		for(int i =0 ; i<no_of_tubes;i++){
			//shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeofset[i],toptube.getWidth(),toptube.getHeight());
			//shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeofset[i],bottomtube.getWidth(),bottomtube.getHeight());


			if(Intersector.overlaps(birdCircle,toptuberectangle[i])||Intersector.overlaps(birdCircle,bottomtuberectangle[i])){
				//Gdx.app.log("Collision","yes");
				gamestate=2;
			}

		}

		//shapeRenderer.end();
		batch.end();

	}
	
	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
	}
}
