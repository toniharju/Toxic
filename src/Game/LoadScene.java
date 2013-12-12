package Game;

import Mimic.Base;
import Mimic.Manager;
import Mimic.Resource;
import Mimic.Scene;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Font;
import org.jsfml.graphics.Text;

/**
 *
 * @author
 * Toni Harju
 */
class ResourceLoader implements Runnable {
	
	@Override
	public void run() {
		
		Resource.getTexture( "res/images/player.png" );
		Resource.getTexture( "res/images/light1.png" );
		Resource.getTexture( "res/images/pistol_bullet.png" );
		Resource.getTexture( "res/images/mainMenuClear.png" );
		Resource.getTexture( "res/images/menu_buttons.png" );
		Resource.getTexture( "res/maps/assets/map1.png" );
		Resource.getTexture( "res/images/crosshair.png" );
		
		Resource.getMusic( "res/sounds/dark_ambient.ogg" );
		Resource.getMusic( "res/sounds/main_menu.ogg" );
		
	}
	
}

public class LoadScene extends Scene {
	
	private Font mFont;
	private Thread mThread;
	private int mThreadCount = 0;
	
	LoadScene() {
		
		super( "LoadScene" );
		
	}
	
	@Override
	public void onCreate() {
		
		mFont = Resource.getFont( "res/fonts/arial.ttf" );
		
		mThread = new Thread( new ResourceLoader() );
		
	}

	@Override
	public void onUpdate() {
		
		if( !mThread.isAlive() ) {
			
			if( mThreadCount == 0 ) {
				
				mThreadCount++;
				mThread.start();
				
			} else {
				
				Manager.setActiveScene( "MenuScene" );
				
			}
			
		}
		
	}

	@Override
	public void onRender() {
		
		Text text = new Text( "Loading...", mFont, 20 );
		
		text.setColor( Color.WHITE );
		text.setPosition( 20, Base.getWindowSize().y - 40 );
		Base.draw( text );
		
	}
	
}
