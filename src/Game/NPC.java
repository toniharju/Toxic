package Game;

import Mimic.AStarPath;
import Mimic.Base;
import Mimic.Entity;
import Mimic.GameMap;
import Mimic.Manager;
import java.util.List;
import org.jsfml.graphics.IntRect;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

/**
 *
 * @author
 * Toni Harju
 */
class NPC extends Entity {
	
	private boolean mFoundWaypoints = false;
	private List< Vector2i > mWaypointList = null;
	
	private final int mLoSDistance = 640;
	private boolean mHadLoS = false;
	
	private static Entity mPlayer = null;
	
	NPC() {
		
		super( "images/player.png" );
		
		setType( "NPC" );
		
		addCollidableType( "Player" );
		addCollidableType( "PistolBullet" );
		
		setOrigin( 64, 64 );
		setPosition( 852, 640 );
		setBoundingBox( new IntRect( 32, 32, 64, 64 ) );
		setMapCollisionAllowed( false );
		//rotate( 45 );
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
				
				if( ent.getType().equals( "Player" ) ) {
					
					mPlayer = ent;
					break;
					
				}
				
			}
			
		}
		
	}
	
	@Override
	public void onUpdate() {
		
		if( hasLineOfSight() ) {
			
			pointTo( mPlayer.getPosition() );
			moveTo( mPlayer.getPosition(), 100 * Base.getDeltaTime() );
			
			mHadLoS = true;
			mFoundWaypoints = false;
			
		} else {
			
			if( mHadLoS ) {
				
				if( !mFoundWaypoints ) {
		
					GameMap active_gamemap = Manager.getActiveGameMap();
			
					mWaypointList = AStarPath.generatePath( active_gamemap.getExpandedCollisionData(), 
															new Vector2i( ( int )getPosition().x / active_gamemap.getGridSize().x, ( int ) getPosition().y / active_gamemap.getGridSize().y ), 
															new Vector2i( ( int )mPlayer.getPosition().x / active_gamemap.getGridSize().x, ( int )mPlayer.getPosition().y / active_gamemap.getGridSize().y ) );
		
					mFoundWaypoints = true;
		
				}
				
				mHadLoS = false;
				
			}
			
		}
		
		if( mWaypointList != null && mWaypointList.size() > 0 ) {
			
			GameMap active_gamemap = Manager.getActiveGameMap();
			
			Vector2i waypointPosition = Vector2i.componentwiseMul( mWaypointList.get( 0 ), active_gamemap.getGridSize() );
			
			pointTo( new Vector2f( waypointPosition ), 200 * Base.getDeltaTime() );
			moveTo( new Vector2f( waypointPosition ), 80 * Base.getDeltaTime() );
			
			if( waypointPosition.equals( new Vector2i( getPosition() ) ) ) {
				
				mWaypointList.remove( 0 );
				
			}
			
		} else {
			
			mWaypointList = null;
			
		}
		
	}

	@Override
	public void onRender() {
		
		
		
	}
	
}