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
	
	private static boolean mLightingAllowed = false;
	
	private static boolean mWasCreated = false;
	
	private static Sprite mSprite = new Sprite();
	private static Sprite mLight = new Sprite();
	
	private static RenderTexture mMainLayer = new RenderTexture();
	private static RectangleShape mDarkLayer = new RectangleShape();
	
	public static void addLight( Vector2f position, Texture texture ) {
		
		
		
	}
	
	public static void setLightingAllowed( boolean toggle ) { mLightingAllowed = toggle; }
	public static boolean getLightingAllowed() { return mLightingAllowed; }
	
	private static synchronized void create() {
		
		if( mWasCreated ) return;
		
		try {
			
			mMainLayer.create( Base.getWindowSize().x, Base.getWindowSize().y );
			
		} catch ( TextureCreationException ex ) {
			
			Logger.getLogger( Lighting.class.getName() ).log( Level.SEVERE, null, ex );
			
		}
		
		Color dark = new Color( 0, 0, 0, 170 );
		
		mDarkLayer.setSize( new Vector2f( Base.getWindowSize() ) );
		mDarkLayer.setFillColor( new Color( 85, 85, 85 ) );
		
		mSprite.setTexture( mMainLayer.getTexture() );
		
		mLight.setTexture( Resource.getTexture( "res/images/light1.png" ) );
		mLight.setPosition( new Vector2f( 100, 32 ) );
		
		mWasCreated = true;
		
	}
	
	public static synchronized void update() {
		
		if( !mWasCreated ) {
			
			create();
			
		}
		
		if( mMainLayer == null || !mLightingAllowed ) return;
		
		mLight.setPosition( Vector2f.sub( new Vector2f( 100, 32 ), mSprite.getPosition() ) );
		mSprite.setPosition( Vector2f.sub( Manager.getActiveScene().getView().getCenter(), Base.getWindowHalfSize() ) );
		
		mMainLayer.clear();
		
		mMainLayer.draw( mDarkLayer );
		mMainLayer.draw( mLight );
		
		mMainLayer.display();
		
	}
	
	public static void render() {
		
		if( !mWasCreated || mMainLayer == null || !mLightingAllowed ) return;
		
		Base.draw( mSprite, new RenderStates( BlendMode.MULTIPLY ) );
		
	}
	
}

class LightingThread implements Runnable {
	
	@Override
	public void run() {
		
		while( Base.getWindow().isOpen() ) {
		
			Lighting.update();
			
			try {
				Thread.sleep( 10 );
			} catch ( InterruptedException ex ) {
				Logger.getLogger( LightingThread.class.getName() ).log( Level.SEVERE, null, ex );
			}
			
		}
		
	}
	
}
