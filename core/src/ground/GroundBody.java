package ground;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import helpers.GameInfo;

/**
 * Created by TERRORMASTER on 3/16/2018.
 */

public class GroundBody {

    private World world;
    private Body body;

    public GroundBody(World world, Sprite ground){

        this.world= world;
        createBody(ground);

    }


    void createBody(Sprite ground){

        BodyDef bodyDef= new BodyDef();
        bodyDef.type= BodyDef.BodyType.StaticBody;
        bodyDef.position.set(ground.getWidth()/GameInfo.PPM, (-ground.getHeight()/2f-55f)/GameInfo.PPM);

        body= world.createBody(bodyDef);
        PolygonShape shape= new PolygonShape();
        shape.setAsBox(ground.getWidth()/ GameInfo.PPM, ground.getHeight()/GameInfo.PPM);

        FixtureDef fixtureDef=new FixtureDef();
        fixtureDef.shape=shape;
        Fixture fixture= body.createFixture(fixtureDef);
        fixture.setUserData("Ground");
        shape.dispose();

    }



}//GroundBody
