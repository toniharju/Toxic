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
		Entity temp = Manager.create( new Enemy() );
		temp.setPosition( 850, 500 );
		
		Entity temp2 = Manager.create( new Drunkard() );
		temp2.setPosition( 300, 600 );
		
		mAmbient = Resource.getMusic( "res/sounds/dark_ambient.ogg" );
		mAmbient.setVolume( 20 );
		mAmbient.play();
		
		Lighting.setLightingAllowed( true );
		Lighting.addStaticLight( new Vector2f( 100, 100 ), Resource.getTexture( "res/images/light1.png" ) );
		
	}
	
	@Override
	public void onUpdate() {
		
		getView().setCenter( mPlayer.getPosition() );
		
	}

	@Override
	public void onRender() {
		
		mFpsText.setString( String.valueOf( Base.getFps() ) );
		mFpsText.setPosition( Vector2f.sub( Manager.getActiveScene().getView().getCenter(), Base.getWindowHalfSize() ) );
		mFpsText.move( 5, 5 );
		
		Manager.getActiveGameMap().render();
		Base.draw( mFpsText );
		
	}
	
}