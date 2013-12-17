package Game;

import Mimic.Manager;
import Mimic.Base;
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
		
		
		
	}
	
}

public class Game {

	public static void main( String[] args ) {
		
		GameBase game = new GameBase();
		
		game.run();
		
	}
	
}
