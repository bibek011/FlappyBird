package scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bksapps.flappybird.GameMain;

import bird.Bird;
import ground.GroundBody;
import helpers.GameInfo;
import pipes.Pipes;

/**
 * Created by TERRORMASTER on 3/15/2018.
 */

public class Gameplay implements Screen {

    private GameMain game;
    private OrthographicCamera mainCamera;
    private OrthographicCamera debugCamera;
    private Box2DDebugRenderer box2DDebugRenderer;
    private Viewport gameViewport;

    private Array<Sprite>backgrounds= new Array<Sprite>();
    private Array<Sprite>grounds= new Array<Sprite>();
    private Bird bird;
    private GroundBody groundBody;

    //Remove later only for testing purpose
    private Pipes pipes;

    private World world;


    public Gameplay(GameMain game){
        this.game=game;
        mainCamera= new OrthographicCamera(GameInfo.WIDTH, GameInfo.HEIGHT);
        mainCamera.position.set(GameInfo.WIDTH/2f, GameInfo.HEIGHT/2f, 0);
        gameViewport= new StretchViewport(GameInfo.WIDTH, GameInfo.HEIGHT, mainCamera);

        debugCamera= new OrthographicCamera();
        debugCamera.setToOrtho(false, GameInfo.WIDTH/GameInfo.PPM,
                GameInfo.HEIGHT/GameInfo.PPM);
        debugCamera.position.set(GameInfo.WIDTH/2f, GameInfo.HEIGHT/2f, 0);

        box2DDebugRenderer= new Box2DDebugRenderer();


        createBackgrounds();
        createGrounds();
        world= new World(new Vector2(0, -9.8f), true);

        bird= new Bird(world, GameInfo.WIDTH/2f-80, GameInfo.HEIGHT/2f);
        groundBody= new GroundBody(world, grounds.get(0));

        //Remove later only for testing purpose
        pipes= new Pipes(world, GameInfo.WIDTH/2f);

    }

    @Override
    public void show() {

    }

    public void update(float delta){

        moveBackground();
        moveGround();
        handleInput(delta);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.getBatch().begin();

        drawBackgrounds(game.getBatch());

        drawGrounds(game.getBatch());

        bird.drawIdle(game.getBatch());

        //Remove later
        pipes.drawPipes(game.getBatch());

        game.getBatch().end();

        box2DDebugRenderer.render(world, debugCamera.combined);

        bird.updateBird();

        //Remove later
        pipes.updatePipes();

        world.step(Gdx.graphics.getDeltaTime(), 6, 2);

    }

    void handleInput(float dt){
        if(Gdx.input.justTouched()){
            bird.birdFlap();
        }
    }

    void createBackgrounds(){
        for(int i=0; i<3; i++){
            Sprite bg= new Sprite(new Texture("Background/Day.jpg"));
            bg.setPosition(i*bg.getWidth(), 0);
            backgrounds.add(bg);
        }
    }

    void createGrounds(){
        for(int i=0; i<3; i++){
            Sprite ground= new Sprite(new Texture("Background/Ground.png"));
            ground.setPosition(i*ground.getWidth(), -ground.getHeight()/2f-55);
            grounds.add(ground);
        }
    }

    void drawBackgrounds(SpriteBatch batch){
        for(Sprite s : backgrounds){
            batch.draw(s, s.getX(), s.getY());
        }
    }
    void drawGrounds(SpriteBatch batch){
        for(Sprite ground: grounds){
            batch.draw(ground, ground.getX(), ground.getY());
        }
    }

    void moveBackground(){
        for(Sprite bg :backgrounds){
            float x1= bg.getX()-2f;
            bg.setPosition(x1, bg.getY());

            if(bg.getX()+bg.getWidth() + (GameInfo.WIDTH/2f)< mainCamera.position.x){

                float x2= bg.getX()+bg.getWidth()*backgrounds.size;
                bg.setPosition(x2, bg.getY());

            }
        }
    }

    void moveGround(){
        for(Sprite ground : grounds){
            float x1 = ground.getX()-1f;
            ground.setPosition(x1, ground.getY());

            if(ground.getX()+ground.getWidth() + (GameInfo.WIDTH/2f) < mainCamera.position.x){
                float x2= ground.getX()+ground.getWidth()*grounds.size;
                ground.setPosition(x2, ground.getY());
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        gameViewport.update(width, height);
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
        for (Sprite s : backgrounds){
            s.getTexture().dispose();
        }
    }
}
