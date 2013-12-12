package Mimic;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.IntRect;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;


/**
 *
 * @author
 * Toni Harju
 */
public class Manager {
	
	private static int mEntityNum = 0;
	
	private static HashMap< String, Boolean > mSceneInitialized = new HashMap();
	
	private static HashMap< String, Scene > mSceneList = new HashMap();
	private static List< Entity > mEntityList = new LinkedList();
	
	private static List< Entity > mEntitiesToCreate = new LinkedList();
	private static List< Integer > mEntitiesToDestroy = new LinkedList();
	
	private static GameMap mActiveGameMap = null;
	
	private static String mActiveScene = "";
	
	public static List< Entity > getEntities() {
		
		return mEntityList;
		
	}
	
	public static Entity create( Entity entity ) {
		
		entity.setId( mEntityNum );
		mEntityNum++;
		
		mEntitiesToCreate.add( entity );
		
		return entity;
		
	}
	
	public static Scene create( Scene scene ) {
		
		if( mSceneList.containsKey( scene.getName() ) ) {
			
			throw new IllegalArgumentException();
			
		}
		
		//scene.getView().setSize( new Vector2f( Base.getWindowSize().x, Base.getWindowSize().y ) );
		Base.setView( scene.getView() );
		
		mSceneList.put( scene.getName(), scene );
		
		mSceneInitialized.put( scene.getName(), false );
		
		return scene;
		
	}
	
	public static void destroy( Entity entity ) {
		
		mEntitiesToDestroy.add( entity.getId() );
		
	}
	
	public static void setActiveScene( String name ) {
		
		mActiveScene = name;
		
	}
	
	public static void setActiveGameMap( GameMap map ) {
		
		mActiveGameMap = map;
		
	}
	
	public static Scene getActiveScene() {
		
		if( mSceneList.containsKey( mActiveScene ) ) return mSceneList.get( mActiveScene );
		
		return null;
		
	}
	
	public static GameMap getActiveGameMap() {
		
		return mActiveGameMap;
		
	}
	
	public static void updateActiveScene() {
		
		if( mSceneList.containsKey( mActiveScene ) ) {
			
			Scene active_scene = mSceneList.get( mActiveScene );
			
			if( !mSceneInitialized.get( mActiveScene ) ) {
				
				active_scene.onCreate();
				mSceneInitialized.put( mActiveScene, true );
				
			} else {
			
				active_scene.onUpdate();
				GameMap active_gamemap = Manager.getActiveGameMap();
				
				if( active_gamemap != null ) {
			
					if( active_scene.getView().getCenter().x < Base.getWindowHalfSize().x )
						active_scene.getView().setCenter( Base.getWindowHalfSize().x, active_scene.getView().getCenter().y );
					else if( active_scene.getView().getCenter().x > active_gamemap.getMapSize().x - Base.getWindowHalfSize().x )
						active_scene.getView().setCenter( active_gamemap.getMapSize().x - Base.getWindowHalfSize().x, active_scene.getView().getCenter().y );
			
					if( active_scene.getView().getCenter().y < Base.getWindowHalfSize().y )
						active_scene.getView().setCenter( active_scene.getView().getCenter().x, Base.getWindowHalfSize().y );
					else if( active_scene.getView().getCenter().y > active_gamemap.getMapSize().y - Base.getWindowHalfSize().y )
						active_scene.getView().setCenter( active_scene.getView().getCenter().x, active_gamemap.getMapSize().y - Base.getWindowHalfSize().y );
			
				}
			
				Base.setView( active_scene.getView() );
				if( active_gamemap != null ) active_gamemap.debugUpdate();
			
			}
			
		}
		
	}
	
	public static void updateEntities() {
		
		if( !mEntitiesToCreate.isEmpty() ) {
		
			for( Entity entity : mEntitiesToCreate ) {
			
				mEntityList.add( entity );
				entity.onCreate();
				
			}
		
			mEntitiesToCreate.clear();
		
		}
		
		for( Entity entity1 : mEntityList ) {

			Vector2f ent1_bb_pos = Vector2f.sub( Vector2f.add( entity1.getPosition(), new Vector2f( entity1.getBoundingBox().left, entity1.getBoundingBox().top ) ), entity1.getOrigin() );
			
			entity1.onUpdate();
			
			FloatRect ent1_bb_xvel = new FloatRect( ent1_bb_pos.x + entity1.getVelocity().x, ent1_bb_pos.y, entity1.getBoundingBox().width, entity1.getBoundingBox().height );
			FloatRect ent1_bb_yvel = new FloatRect( ent1_bb_pos.x, ent1_bb_pos.y + entity1.getVelocity().y, entity1.getBoundingBox().width, entity1.getBoundingBox().height );
			
			if( entity1.isMapCollisionAllowed() ) {
				
				GameMap active_gamemap = Manager.getActiveGameMap();
				
				Vector2i grid_size = active_gamemap.getGridSize();
				
				int[][] collision_layer = active_gamemap.getCollisionData();
				
				IntRect ent1_grid_bb_xvel = new IntRect( ( int )ent1_bb_xvel.left / grid_size.x, ( int )ent1_bb_xvel.top / grid_size.y, ( int )ent1_bb_xvel.width / grid_size.x, ( int )ent1_bb_xvel.height / grid_size.y );
				IntRect ent1_grid_bb_yvel = new IntRect( ( int )ent1_bb_yvel.left / grid_size.y, ( int )ent1_bb_yvel.top / grid_size.y, ( int )ent1_bb_yvel.width / grid_size.x, ( int )ent1_bb_yvel.height / grid_size.y );
				
				boolean collided = false;
				
				if( active_gamemap.gridIntersects( ent1_grid_bb_xvel ) ) {
					
					if( entity1.getVelocity().x < 0 ) {
					
						int mh = ent1_grid_bb_xvel.height + 1;
					
						for( int h = 0; h < mh; h++ ) {
						
							if( collision_layer[ ent1_grid_bb_xvel.left ][ ent1_grid_bb_xvel.top + h ] == 1 ) {
						
								entity1.setVelocity( 0, entity1.getVelocity().y ); collided = true; break;
							
							}
							
						}
					
					} else if( entity1.getVelocity().x > 0 ) {
					
						int mh = ent1_grid_bb_xvel.height + 1;
					
						for( int h = 0; h < mh; h++ ) {
						
							if( collision_layer[ ent1_grid_bb_xvel.left + ent1_grid_bb_xvel.width ][ ent1_grid_bb_xvel.top + h ] == 1 ) {
							
								entity1.setVelocity( 0, entity1.getVelocity().y ); collided = true; break;
							
							}
						
						}
					
					}
				
				} else {
					
					entity1.setVelocity( 0, entity1.getVelocity().y ); collided = true;
					
				}
				
				if( active_gamemap.gridIntersects( ent1_grid_bb_yvel)) {
					
					if( entity1.getVelocity().y < 0 ) {

						int mw = ent1_grid_bb_yvel.width + 1;
					
						for( int w = 0; w < mw; w++ ) {
					
							if( collision_layer[ ent1_grid_bb_yvel.left + w ][ ent1_grid_bb_yvel.top ] == 1 ) {
							
								entity1.setVelocity( entity1.getVelocity().x, 0 ); collided = true; break;
							
							}
						
						}
					
					} else if( entity1.getVelocity().y > 0 ) {
					
						int mw = ent1_grid_bb_yvel.width + 1;
					
						for( int w = 0; w < mw; w++ ) {
						
							if( collision_layer[ ent1_grid_bb_yvel.left + w ][ ent1_grid_bb_yvel.top + ent1_grid_bb_yvel.height ] == 1 ) {
							
								entity1.setVelocity( entity1.getVelocity().x, 0 ); collided = true; break;
							
							}
						
						}
					
					}
				
				} else {
					
					entity1.setVelocity( entity1.getVelocity().x, 0 ); collided = true;
					
				}
				
				if( collided ) entity1.onMapCollision();
				
			}
			
			for( Entity entity2 : mEntityList ) {
			
				if( entity1.getId() == entity2.getId() ) continue;
				
				if( !entity1.getCollidableTypes().containsKey( entity2.getType() ) ) continue;
				
				boolean collided = false;
				
				Vector2f ent2_bb_pos = Vector2f.sub( Vector2f.add( entity2.getPosition(), new Vector2f( entity2.getBoundingBox().left, entity2.getBoundingBox().top ) ), entity2.getOrigin() );
				
				FloatRect ent2_bb = new FloatRect( ent2_bb_pos.x, ent2_bb_pos.y, entity2.getBoundingBox().width, entity2.getBoundingBox().height );
				
				if( ent1_bb_pos.x != ent1_bb_xvel.left && ent1_bb_xvel.intersection( ent2_bb ) != null ) {
					
					collided = true;
					entity1.setVelocity( 0, entity1.getVelocity().y );
					
				}
				
				if( ent1_bb_pos.y != ent1_bb_yvel.top && ent1_bb_yvel.intersection( ent2_bb ) != null ) {
					
					collided = true;
					entity1.setVelocity( entity1.getVelocity().x, 0 );
					
				}
				
				if( collided ) {
					
					entity1.onEntityCollision( entity2 );
					entity2.onEntityCollision( entity1 );
					
				}
				
			}
			
			entity1.move( entity1.getVelocity() );
			
			entity1.setVelocity( 0, 0 );
			
		}
		
		if( !mEntitiesToDestroy.isEmpty() ) {
			
			for( Integer i : mEntitiesToDestroy ) {
				
				for( Iterator< Entity > iter = mEntityList.iterator(); iter.hasNext(); ) {
					
					Entity ent = iter.next();
					
					ent.onDestroy();
					if( ent.getId() == i ) iter.remove();
					
				}
				
			}
			
			mEntitiesToDestroy.clear();
			
		}
		
	}
	
	public static void renderActiveScene() {
		
		if( mSceneList.containsKey( mActiveScene ) && mSceneInitialized.get( mActiveScene ) ) {
			
			Scene active_scene = mSceneList.get( mActiveScene );
			
			active_scene.onRender();
			
		}
		
	}
	
	public static void renderEntities() {

		if( !mSceneList.containsKey( mActiveScene ) ) return;
		
		for( Entity entity : mEntityList ) {
			
			entity.onRender();
			
			Base.draw( entity );
			
		}
		
	}
	
}
