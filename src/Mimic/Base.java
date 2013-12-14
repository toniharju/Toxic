package Mimic;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.ConstView;
import org.jsfml.graphics.Drawable;
import org.jsfml.graphics.RenderStates;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.graphics.View;
import org.jsfml.system.Clock;
import org.jsfml.system.Time;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Mouse;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.Event;

/**
 *
 * @author
 * Toni Harju
 */
public abstract class Base {
	
	private static float mSineTable[] = new float[ 360 ];
	
	private static Time mDeltaTime;
	private static Clock mDeltaTimer = new Clock();
	
	private static Clock mFpsTimer = new Clock();
	private static int mFrameCount = 0;
	private static int mFps = 0;
	
	private static Sprite mCursor =  new Sprite();
	
	private Thread mLightingThread;
	
	public boolean mIsInitialized = false;
	
	private static Vector2f mWindowHalfSize = new Vector2f( 0, 0 );
	
	private static RenderWindow mWindowHandle;
	
	public Base( Vector2i windowSize, String title ) {
		
		mWindowHandle = new RenderWindow( new VideoMode( windowSize.x, windowSize.y ), title );
		
		mWindowHalfSize = new Vector2f( mWindowHandle.getSize().x / 2, mWindowHandle.getSize().y / 2 );
		
		mWindowHandle.setMouseCursorVisible( false );
		mWindowHandle.setFramerateLimit( 120 );
		
		for( int i = 0; i < 360; i++ ) {
			
			mSineTable[ i ] = ( float )Math.sin( Math.toRadians( i ) );
			
		}
		
		mLightingThread = new Thread( new LightingThread() );
		
	}
	
	public static void setCursor( Texture tex ) {
		
		mCursor.setTexture( tex );
		
	}
	
	public static float sin( float angle ) {
		
		int a = ( int )angle;
		
		if( a < 0 ) a = 360 + ( a % 360 );
		if( a > 359 ) a = a % 360;
		
		return mSineTable[ a ];
		
	}
	
	public static float cos( float angle ) { return sin( angle + 90 ); }
	
	public static float getDeltaTime() { return mDeltaTime.asSeconds(); }
	public static int getFps() { return mFps; }
	
	public void run() {
		
		mLightingThread.start();
		
		mFpsTimer.restart();
		
		while( mWindowHandle.isOpen() ) {
			
			if( !mIsInitialized ) {
				
				onCreate();
				mIsInitialized = true;
				
			}
			
			mDeltaTime = mDeltaTimer.restart();
			
			if( mFpsTimer.getElapsedTime().asSeconds() >= 1 ) {
				
				mFps = mFrameCount;
				mFrameCount = 0;
				
				mFpsTimer.restart();
				
			}
			
			mCursor.setPosition( mapPixelToCoords( Mouse.getPosition( mWindowHandle ) ) );

			Manager.updateActiveScene();
			Manager.updateEntities();
			if( Manager.getActiveGameMap() != null ) Manager.getActiveGameMap().debugUpdate();
			onUpdate();
			
			mWindowHandle.clear( Color.BLACK );
			
			Manager.renderActiveScene();
			if( Manager.getActiveGameMap() != null ) Manager.getActiveGameMap().debugRender();
			Manager.renderEntities();
			Lighting.render();
			
			onRender();
			
			mWindowHandle.draw( mCursor );
			
			mWindowHandle.display();
			
			mWindowHandle.draw( mCursor );
			
			for( Event event : mWindowHandle.pollEvents() ) {
			
				if( event.type == Event.Type.CLOSED ) mWindowHandle.close();
			
				Input.updateEvents( event );
				
			}
			
			mFrameCount++;
			
		}
		
	}
	
	public abstract void onCreate();
	public abstract void onUpdate();
	public abstract void onRender();
	
	public static void draw( Drawable drawable ) {
		
		if( mWindowHandle != null ) mWindowHandle.draw( drawable );
		
	}
	
	public static void draw( Drawable drawable, RenderStates states ) {
		
		if( mWindowHandle != null ) mWindowHandle.draw( drawable, states );
		
	}
	
	public static Vector2i getWindowSize() { return mWindowHandle.getSize(); }
	public static Vector2f getWindowHalfSize() { return mWindowHalfSize; }
	
	public static ConstView getView() { return mWindowHandle.getView(); }
	public static RenderWindow getWindow() { return mWindowHandle; }
	
	public static Vector2f mapPixelToCoords( Vector2i position ) {
		
		return Vector2f.add( Vector2f.sub( new Vector2f( position ), Vector2f.div( getView().getSize(), 2 ) ), getView().getCenter() );
		
	}
	
	public static void setView( View view ) {
		
		mWindowHandle.setView( view );
		
	}
	
}