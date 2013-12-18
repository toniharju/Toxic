package Game;

import Mimic.Base;
import Mimic.Entity;
import Mimic.ICollisionEvents;
import Mimic.Input;
import Mimic.Inventory;
import Mimic.Manager;
import Mimic.Resource;
import org.jsfml.audio.Sound;
import org.jsfml.graphics.IntRect;
import org.jsfml.system.Clock;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard;
import org.jsfml.window.Mouse;

/**
 *
 * @author
 * Toni Harju
 */
class Player extends Entity implements ICollisionEvents {
	
	private Sound mPistolShot = new Sound();
	private Sound mPistolEmpty = new Sound();
	private Sound mFootstep = new Sound();
	private Clock mFootstepClock = new Clock();
	private boolean mIsMoving = false;
	
	Player() {
		
		super( "images/playerPistol.png" );
		
		setType( "PLAYER" );
		
		mPistolShot.setBuffer( Resource.getSound( "res/sounds/pistol_shot.wav" ) );
		mPistolEmpty.setBuffer( Resource.getSound( "res/sounds/pistol_empty.wav" ) );
		mFootstep.setBuffer( Resource.getSound( "res/sounds/footstep.wav" ) );
		mFootstep.setVolume( 20 );
		
		addCollidableType( "NPC" );
		addCollidableType( "ENEMY" );
		
		setOrigin( 64, 64 );
		setPosition( 352, 352 );
		setBoundingBox( new IntRect( 32, 32, 64, 64 ) );
		
		setPushable( true );
		
		mFootstepClock.restart();
		
	}
	
	@Override
	public void onUpdate() {
		
		if( Base.isPaused() ) return;
		
		 mIsMoving = false;
		
		if( Keyboard.isKeyPressed( Keyboard.Key.A ) ) { setVelocity( -200 * Base.getDeltaTime(), getVelocity().y ); mIsMoving = true; }
		if( Keyboard.isKeyPressed( Keyboard.Key.D ) ) { setVelocity( 200 * Base.getDeltaTime(), getVelocity().y ); mIsMoving = true; }
		if( Keyboard.isKeyPressed( Keyboard.Key.W ) ) { setVelocity( getVelocity().x, -200 * Base.getDeltaTime() ); mIsMoving = true; }
		if( Keyboard.isKeyPressed( Keyboard.Key.S ) ) { setVelocity( getVelocity().x, 200 * Base.getDeltaTime() ); mIsMoving = true; }

		if(  mIsMoving && mFootstepClock.getElapsedTime().asMilliseconds() > 500 ) {
			
			mFootstep.play();
			
			mFootstepClock.restart();
			
		}
		
		if( Input.isMouseHit( Mouse.Button.LEFT ) ) {
			
			if( Inventory.get( Inventory.ItemType.Ammo, Inventory.Item.Pistol ) > 0 ) {
			
				Manager.create( new PistolBullet( getPosition(), new Vector2f( 10, -100 ), getRotation() ) );
			
				Inventory.set( Inventory.ItemType.Ammo, Inventory.Item.Pistol, Inventory.get( Inventory.ItemType.Ammo, Inventory.Item.Pistol ) - 1 );
				
				mPistolShot.play();
				
			} else {
				
				mPistolEmpty.play();
				
			}
			
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