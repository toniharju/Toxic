/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Game;

import Mimic.Base;
import Mimic.Entity;
import Mimic.ICollisionEvents;
import Mimic.Manager;
import org.jsfml.graphics.IntRect;
import org.jsfml.system.Vector2f;

/**
 *
 * @author
 * Toni Harju
 */
public class PistolBullet extends Entity implements ICollisionEvents {
	
	private final int mSpeed = 2000;
	private float mTrajectory = 0;
	
	PistolBullet( Vector2f origin, Vector2f offset, float angle ) {
	
		super( "images/pistol_bullet.png" );
		
		setType( "BULLET" );
		
		//addCollidableType( "NPC" );
		addCollidableType( "ENEMY" );
		
		setOrigin( new Vector2f( 0, 4 ) );
		
		Vector2f point = Vector2f.add( origin, offset );
		
		float pos_x = ( float )( origin.x + Base.cos( angle ) * ( point.x - origin.x ) - Base.sin( angle ) * ( point.y - origin.y ) );
		float pos_y = ( float )( origin.y + Base.sin( angle ) * ( point.x - origin.x ) + Base.cos( angle ) * ( point.y - origin.y ) );
		
		setPosition( pos_x, pos_y );
		setRotation( angle );
		setBoundingBox( new IntRect( 0, 0, 9, 9 ) );
		
		mTrajectory = angle + 270;
	
		setMapCollisionAllowed( true );
		
	}

	@Override
	public void onUpdate() {

		setVelocity( ( float )( mSpeed * Base.cos( mTrajectory ) ) * Base.getDeltaTime(), ( float )( mSpeed * Base.sin( mTrajectory ) ) * Base.getDeltaTime() );
	
	}

	@Override
	public void onRender() {

		
		
	}
	
	@Override
	public void onMapCollision() {
		
		Manager.destroy( this );
		
	}
	
	@Override
	public void onEntityCollision( Entity that ) {
		
		Manager.destroy( that );
		//Manager.destroy( this );
		
	}
	
}
