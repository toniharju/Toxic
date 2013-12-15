package Game;

import Mimic.AStarPath;
import Mimic.Base;
import Mimic.GameMap;
import Mimic.Manager;
import java.util.List;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;

/**
 *
 * @author
 * Toni Harju
 */
public class Enemy extends NPC {
	
	private boolean mHadLoS = false;
	
	private boolean mFoundWaypoints = false;
	private List< Vector2i > mWaypointList = null;
	
	Enemy() {
		
		super( "images/player.png" );
		
		setType( "ENEMY" );

		addCollidableType( "PLAYER" );
		addCollidableType( "BULLET" );
		
	}
	
	@Override
	public void onCreate() {
		
		super.onCreate();
		
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
		
		if( Base.isPaused() ) return;
		
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
