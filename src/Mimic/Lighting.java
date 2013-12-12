package Mimic;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsfml.graphics.BlendMode;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderTexture;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.graphics.TextureCreationException;
import org.jsfml.system.Vector2f;

/**
 *
 * @author
 * Toni Harju
 */
public class Lighting {
	
	private static boolean mWasCreated = false;
	
	private static Sprite mSprite = new Sprite();
	private static Sprite mLight = new Sprite();
	
	private static RenderTexture mMainLayer = new RenderTexture();
	private static RectangleShape mDarkLayer = new RectangleShape();
	
	public static void addLight( Vector2f position, Texture texture ) {
		
		
		
	}
	
	public static void create() {
		
		try {
			
			GameMap active_gamemap = Manager.getActiveGameMap();
			
			mMainLayer.create( active_gamemap.getMapSize().x, active_gamemap.getMapSize().y );
			
		} catch ( TextureCreationException ex ) {
			
			Logger.getLogger( Lighting.class.getName() ).log( Level.SEVERE, null, ex );
			
		}
		
		Color dark = new Color( 0, 0, 0, 170 );
		
		mDarkLayer.setSize( new Vector2f( Manager.getActiveGameMap().getMapSize() ) );
		//mDarkLayer.setSize( new Vector2f( Base.getWindowSize() ) );
		mDarkLayer.setFillColor( new Color( 85, 85, 85 ) );
		
		mSprite.setTexture( mMainLayer.getTexture() );
		
		mLight.setTexture( Resource.getTexture( "res/images/light1.png" ) );
		mLight.setPosition( new Vector2f( 100, 32 ) );
		
		mMainLayer.clear();
		
		mMainLayer.draw( mDarkLayer );
		mMainLayer.draw( mLight );
		
		mMainLayer.display();
		
	}
	
	public static void update() {
		
		if( mMainLayer == null ) return;
		
		/*if( !mWasCreated ) {
			
			mWasCreated = true;
			
			create();
			
		}*/
		
		//mDarkLayer.setPosition( Vector2f.sub( Manager.getActiveScene().getView().getCenter(), Base.getWindowHalfSize() ) );
		//mSprite.setPosition( Vector2f.sub( Manager.getActiveScene().getView().getCenter(), Base.getWindowHalfSize() ) );
		
		/*mMainLayer.clear();
		
		mMainLayer.draw( mDarkLayer );
		mMainLayer.draw( mLight );
		
		mMainLayer.display();*/
		
	}
	
	public static void render() {
		
		if( mMainLayer == null ) return;
		
		Base.draw( mSprite, new RenderStates( BlendMode.MULTIPLY ) );
		
	}
	
}
