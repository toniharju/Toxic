package Game;

import Mimic.Manager;
import Mimic.Base;
import Mimic.Inventory;
import Mimic.Inventory;
import Mimic.Resource;
import org.jsfml.graphics.Text;
import org.jsfml.system.Vector2i;


/**
 *
 * @author
 * Toni Harju
 */
class GameBase extends Base {
	
	GameBase() {
		
		super( new Vector2i( 1024, 768 ), "Game" );
		
	}
	
	@Override
	public void onCreate() {
		
		Manager.create( new LoadScene() );
		Manager.create( new MenuScene() );
		Manager.create( new MainScene() );
		
		Manager.setActiveScene( "LoadScene" );
		
	}
	
	@Override
	public void onUpdate() {
		
		
		
	}

	@Override
	public void onRender() {
		
		if( Manager.getActiveScene().getName().equals( "MainScene" ) ) {
			
			MainScene temp = ( MainScene )Manager.getActiveScene();
			
			if( temp.isInventoryOpen() ) {
				
				Base.draw( temp.getInventorySprite() );
				
				if( Inventory.get( Inventory.ItemType.Weapon, Inventory.Item.Pistol ) == 1 ) Base.draw( temp.getPistolSprite() );
				if( Inventory.get( Inventory.ItemType.Ammo, Inventory.Item.Pistol ) > 0 ) {
					
					Text ammo = new Text( String.valueOf( Inventory.get( Inventory.ItemType.Ammo, Inventory.Item.Pistol ) ), Resource.getFont( "res/fonts/arial.ttf" ), 12 );
					ammo.setPosition( temp.getPistolAmmoSprite().getPosition().x + 50, temp.getPistolAmmoSprite().getPosition().y + 35 );
					
					Base.draw( temp.getPistolAmmoSprite() );
					Base.draw( ammo );
					
				}
				
			}
			
		}
		
	}
	
}

public class Game {

	public static void main( String[] args ) {
		
		GameBase game = new GameBase();
		
		game.run();
		
	}
	
}
