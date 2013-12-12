package Game;

import Mimic.Base;
import Mimic.Entity;
import Mimic.ICollisionEvents;
import Mimic.Input;
import Mimic.Manager;
import org.jsfml.graphics.IntRect;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Keyboard;
import org.jsfml.window.Mouse;

/**
 *
 * @author
 * Toni Harju
 */
class Player extends Entity implements ICollisionEvents {

	Player() {
		
		super( "images/player.png" );
		
		setType( "Player" );
		
		addCollidableType( "NPC" );
		
		setOrigin( 64, 64 );
		setPosition( 352, 352 );
		setBoundingBox( new IntRect( 32, 32, 64, 64 ) );
		
		setPushable( true );
		
	}
	
	@Override
	public void onUpdate() {

		if( Keyboard.isKeyPressed( Keyboard.Key.A ) ) setVelocity( -200 * Base.getDeltaTime(), getVelocity().y );
		if( Keyboard.isKeyPressed( Keyboard.Key.D ) ) setVelocity( 200 * Base.getDeltaTime(), getVelocity().y );
		if( Keyboard.isKeyPressed( Keyboard.Key.W ) ) setVelocity( getVelocity().x, -200 * Base.getDeltaTime() );
		if( Keyboard.isKeyPressed( Keyboard.Key.S ) ) setVelocity( getVelocity().x, 200 * Base.getDeltaTime() );

		if( Input.isMouseHit( Mouse.Button.LEFT ) ) {
			
			Manager.create( new PistolBullet( getPosition(), new Vector2f( 10, -100 ), getRotation() ) );
			
		}
		
		pointTo( Base.mapPixelToCoords( Mouse.getPosition( Base.getWindow() ) ) );
		
	}

	@Override
	public void onRender() {

		
		
	}
	
	@Override
	public void onEntityCollision( Entity that ) {
		
		//Entity.destroy( that.getId() );
		
	}
	
}