package Game;

import Mimic.Base;
import Mimic.IMainEvents;
import Mimic.Input;
import Mimic.Lighting;
import Mimic.Manager;
import Mimic.Resource;
import Mimic.Scene;
import org.jsfml.audio.Music;
import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.Sprite;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Mouse;
import org.jsfml.window.Mouse.Button;

/**
 *
 * @author
 * Toni Harju
 */
public class MenuScene extends Scene implements IMainEvents {

	private Music mMenuMusic;
	private Sprite mButtons = new Sprite();
	private Sprite mBackground = new Sprite();
	
	private boolean mButtonNewGameHighlighted = false;
	
	MenuScene() {
		
		super( "MenuScene" );
		
	}
	
	@Override
	public void onCreate() {
		
		Base.setCursor( Resource.getTexture( "res/images/crosshair.png" ) );
		
		mBackground.setTexture( Resource.getTexture( "res/images/mainMenu.png" ) );
		
		mButtons.setTexture( Resource.getTexture( "res/images/menu_buttons.png" ) );
		mButtons.setPosition( 266, 600 );
		
		mMenuMusic = Resource.getMusic( "res/sounds/main_menu.ogg" );
		mMenuMusic.setVolume( 25 );
		mMenuMusic.setLoop( true );
		mMenuMusic.play();
		
		Lighting.setLightingAllowed( false );
		
	}
	
	@Override
	public void onUpdate() {
		
		mButtonNewGameHighlighted = false;
		
		Vector2i mousePos = Mouse.getPosition( Base.getWindow() );
		
		if( mousePos.x > 266 && mousePos.x < 758 ) {
			
			if( mousePos.y > 600 && mousePos.y < 632 ) {
				
				mButtonNewGameHighlighted = true;
				
				if( Input.isMouseUp( Button.LEFT ) ) {
					
					mMenuMusic.stop();
					
					Manager.setActiveScene( "MainScene" );
					
				}
				
			}
			
		}
		
	}

	@Override
	public void onRender() {
		
		Base.draw( mBackground );
		
		if( mButtonNewGameHighlighted ) {
			
			mButtons.setTextureRect( new IntRect( 0, 32, 492, 64 ) );
			Base.draw( mButtons );
			
		} else {
			
			mButtons.setTextureRect( new IntRect( 0, 0, 492, 32 ) );
			Base.draw( mButtons );
			
		}
		
	}
	
}
