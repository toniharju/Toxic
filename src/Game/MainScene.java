package Game;

import Mimic.Base;
import Mimic.Entity;
import Mimic.GameMap;
import Mimic.IMainEvents;
import Mimic.Lighting;
import Mimic.Manager;
import Mimic.Resource;
import Mimic.Scene;
import org.jsfml.audio.Music;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Font;
import org.jsfml.graphics.Text;
import org.jsfml.system.Vector2f;

/**
 *
 * @author
 * Toni Harju
 */
class MainScene extends Scene implements IMainEvents {

	private Music mAmbient;
	private Entity mPlayer;
	
	private Font mFont;
	private Text mFpsText = new Text();
	
	MainScene() {
		
		super( "MainScene" );
		
	}
	
	@Override
	public void onCreate() {
		
		Manager.setActiveGameMap( Resource.getGameMap( "res/maps/map1.map" ) );
		Manager.getActiveGameMap().setTexture( Resource.getTexture( "res/maps/assets/map1.png" ) );
		
		mFont = Resource.getFont( "res/fonts/arial.ttf" );
		mFpsText.setFont( mFont );
		mFpsText.setCharacterSize( 16 );
		mFpsText.setColor( Color.WHITE );
		
		mPlayer = Manager.create( new Player() );
		Manager.create( new NPC() );
		
		mAmbient = Resource.getMusic( "res/sounds/dark_ambient.ogg" );
		mAmbient.setVolume( 20 );
		mAmbient.play();
		
		Lighting.setLightingAllowed( true );
		
		//Lighting.create();
		
	}
	
	@Override
	public void onUpdate() {
		
		getView().setCenter( mPlayer.getPosition() );
		
		//Lighting.update();
		
	}

	@Override
	public void onRender() {
		
		mFpsText.setString( String.valueOf( Base.getFps() ) );
		mFpsText.setPosition( Vector2f.sub( Manager.getActiveScene().getView().getCenter(), Base.getWindowHalfSize() ) );
		mFpsText.move( 5, 5 );
		
		Manager.getActiveGameMap().render();
		Base.draw( mFpsText );
		
		//Lighting.render();
		
	}
	
}