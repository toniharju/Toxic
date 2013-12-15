package Mimic;

import java.util.LinkedList;
import java.util.List;
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
class Light {
	
	private Vector2f mOrigin;
	private Sprite mSprite = new Sprite();
	
	Light( Vector2f origin, Texture texture ) {
		
		mOrigin = origin;
		mSprite.setPosition( origin );
		mSprite.setTexture( texture );
		
	}
	
	public Vector2f getOrigin() {
		
		return mOrigin;
		
	}
	
	public Sprite getSprite() {
		
		return mSprite;
		
	}
	
}

public class Lighting {
	
	private static boolean mLightingAllowed = false;
	
	private static boolean mWasCreated = false;
	private static boolean mStaticCreated = false;
	
	private static Sprite mStaticSprite = new Sprite();
	
	private static RenderTexture mStaticLayer = new RenderTexture();
	private static RectangleShape mDarkLayer = new RectangleShape();
	
	private static List< Light > mStaticLightList = new LinkedList();
	
	public static void addStaticLight( Vector2f origin, Texture texture ) {
		
		mStaticLightList.add( new Light( origin, texture ) );
		
	}
	
	public static void setLightingAllowed( boolean toggle ) { mLightingAllowed = toggle; }
	public static boolean getLightingAllowed() { return mLightingAllowed; }
	
	private static synchronized void create() {
		
		if( mWasCreated ) return;
		
		try {
			
			mStaticLayer.create( Base.getWindowSize().x, Base.getWindowSize().y );
			
		} catch ( TextureCreationException ex ) {
			
			Logger.getLogger( Lighting.class.getName() ).log( Level.SEVERE, null, ex );
			
		}
		
		Color dark = new Color( 0, 0, 0, 170 );
		
		mDarkLayer.setSize( new Vector2f( Base.getWindowSize() ) );
		mDarkLayer.setFillColor( new Color( 85, 85, 85 ) );
		
		mStaticSprite.setTexture( mStaticLayer.getTexture() );
		
		mWasCreated = true;
		
	}
	
	public static synchronized void createStatic() {
		
		if( !mWasCreated || mStaticCreated ) return;
		
		if( mStaticLayer == null || !mLightingAllowed ) return;
		
		if( !mStaticLightList.isEmpty() ) {
			
			for( Light light : mStaticLightList ) {
				
				light.getSprite().setPosition( Vector2f.sub( light.getOrigin(), mStaticSprite.getPosition() ) );
				
			}
			
		}
		
		mStaticSprite.setPosition( Vector2f.sub( Manager.getActiveScene().getView().getCenter(), Base.getWindowHalfSize() ) );
		
		mStaticLayer.clear();
		
		mStaticLayer.draw( mDarkLayer );
		if( !mStaticLightList.isEmpty() ) {
			
			for( Light light : mStaticLightList ) {
				
				mStaticLayer.draw( light.getSprite() );
				
			}
			
		}
		
		mStaticLayer.display();
		
	}
	
	public static synchronized void update() {
		
		if( !mWasCreated ) {
			
			create();
			
		}
		
		if( !mStaticLightList.isEmpty() ) {
			
			for( Light light : mStaticLightList ) {
				
				light.getSprite().setPosition( Vector2f.sub( light.getOrigin(), mStaticSprite.getPosition() ) );
				
			}
			
		}
		
		mStaticSprite.setPosition( Vector2f.sub( Manager.getActiveScene().getView().getCenter(), Base.getWindowHalfSize() ) );
		
	}
	
	public static void render() {
		
		if( !mWasCreated || mStaticLayer == null || !mLightingAllowed ) return;
		
		Base.draw( mStaticSprite, new RenderStates( BlendMode.MULTIPLY ) );
		
	}
	
}

class LightingThread implements Runnable {
	
	@Override
	public void run() {
		
		while( Base.getWindow().isOpen() ) {
		
			Lighting.createStatic();
			Lighting.update();
			
			try {
				Thread.sleep( 20 );
			} catch ( InterruptedException ex ) {
				Logger.getLogger( LightingThread.class.getName() ).log( Level.SEVERE, null, ex );
			}
			
		}
		
	}
	
}
