package Game;

import Mimic.Entity;
import Mimic.Manager;
import org.jsfml.graphics.IntRect;
import org.jsfml.system.Vector2i;

/**
 *
 * @author
 * Toni Harju
 */
class NPC extends Entity {
	
	private final int mLoSDistance = 640;
	
	protected static Entity mPlayer = null;
	
	NPC( String texturePath ) {
		
		super( texturePath );
		
		setType( "NPC" );
		
		addCollidableType( "PLAYER" );
		
		setOrigin( 64, 64 );
		setBoundingBox( new IntRect( 32, 32, 64, 64 ) );
		setMapCollisionAllowed( false );
		//rotate( 45 );
	}
	
	public boolean isDistanceToPlayerLessThan( float distance ) {
		
		float dx = mPlayer.getPosition().x - getPosition().x;
		float dy = mPlayer.getPosition().y - getPosition().y;
		
		if( dx * dx + dy * dy < distance * distance ) return true;
		
		return false;
		
	}
	
	public boolean hasLineOfSight() {
		
		if( mPlayer == null ) return false;
			
		float dx = mPlayer.getPosition().x - getPosition().x;
		float dy = mPlayer.getPosition().y - getPosition().y;
		
		//Ignores walls for now
		if( dx * dx + dy * dy < mLoSDistance * mLoSDistance ) {

			float angle = ( float )Math.toDegrees( Math.atan2( dy, dx ) ) - getRotation();
			
			angle = angle + 90;
			
			angle = angle % 360;
			
			if( angle >= 180 ) angle -= 360;
			if( angle < -180 ) angle += 360;

			if( angle > -90 && angle < 90 ) {
				
				Vector2i grid_size = Manager.getActiveGameMap().getGridSize();
		
				int x0 = ( int )getPosition().x / grid_size.x; int x1 = ( int )mPlayer.getPosition().x / grid_size.x;		
				int y0 = ( int )getPosition().y / grid_size.y; int y1 = ( int )mPlayer.getPosition().y / grid_size.y;
		
				int d2x = Math.abs( x1 - x0 );
				int d2y = Math.abs( y1 - y0 );
		
				int sx = x0 < x1 ? 1 : -1;
				int sy = y0 < y1 ? 1 : -1;
		
				int error = d2x - d2y;
				int e2;
				int cx = x0;
				int cy = y0;
		
				while( true ) {
			
					if( cx == x1 && cy == y1 ) {
				
						break;
				
					}
			
					e2 = 2 * error;
					if( e2 > -1 * d2y ) {
				
						error = error - d2y;
						cx = cx + sx;
				
					}
			
					if( e2 < d2x ) {
				
						error = error + d2x;
						cy = cy + sy;
				
					}
			
					if( Manager.getActiveGameMap().getCollisionData()[ cx ][ cy ] == 1 ) return false;
			
				}
				
				return true;
				
			}
			
		}
		
		return false;
		
	}
	
	@Override
	public void onCreate() {
		
		if( mPlayer == null ) {
			
			for( Entity ent : Manager.getEntities() ) {
				
				if( ent.getType().equals( "PLAYER" ) ) {
					
					mPlayer = ent;
					break;
					
				}
				
			}
			
		}
		
	}
	
	@Override
	public void onUpdate() {
		
		
		
	}

	@Override
	public void onRender() {
		
		
		
	}
	
}