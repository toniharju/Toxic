package Game;

import Mimic.Base;
import Mimic.Entity;
import Mimic.IMainEvents;
import Mimic.Input;
import Mimic.Inventory;
import Mimic.Lighting;
import Mimic.Manager;
import Mimic.Resource;
import Mimic.Scene;
import org.jsfml.audio.Music;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Font;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Text;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard;

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
	
	private Sprite mInventory = new Sprite();
	private Sprite mPistol = new Sprite();
	private Sprite mPistolAmmo = new Sprite();
	private boolean mIsInventoryOpen = false;
	
	MainScene() {
		
		super( "MainScene" );
		
	}
	
	public boolean isInventoryOpen() { return mIsInventoryOpen; }
	public Sprite getInventorySprite() { return mInventory; }
	public Sprite getPistolSprite() { return mPistol; }
	public Sprite getPistolAmmoSprite() { return mPistolAmmo; }
	
	@Override
	public void onCreate() {
		
		Manager.setActiveGameMap( Resource.getGameMap( "res/maps/map1.map" ) );
		Manager.getActiveGameMap().setImage( Resource.getImage( "res/maps/assets/map1.png" ) );
		
		Inventory.initialize();
		Inventory.set( Inventory.ItemType.Weapon, Inventory.Item.Pistol, 1 );
		Inventory.set( Inventory.ItemType.Ammo, Inventory.Item.Pistol, 20 );
		
		mInventory.setTexture( Resource.getTexture( "res/images/inventory.png" ) );
		mPistol.setTexture( Resource.getTexture( "res/images/pistol.png" ) );
		mPistolAmmo.setTexture( Resource.getTexture( "res/images/pistolAmmo.png" ) );
		
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
		
		if( Input.isKeyHit( Keyboard.Key.TAB ) ) {
			
			if( mIsInventoryOpen ) {
			
				Base.setPause( false );
				mIsInventoryOpen = false;
				
			} else {
				
				mInventory.setPosition( Vector2f.sub( Manager.getActiveScene().getView().getCenter(), new Vector2f( mInventory.getLocalBounds().width / 2, mInventory.getLocalBounds().height / 2 ) ) );
				mPistol.setPosition( mInventory.getPosition() );
				mPistol.move( 42, 42 );
				mPistolAmmo.setPosition( mPistol.getPosition() );
				mPistolAmmo.move( 81, 0 );
				
				Base.setPause( true );
				mIsInventoryOpen = true;
				
			}
			
		}
		
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