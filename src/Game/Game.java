package Game;

import Mimic.Manager;
import Mimic.Base;
import Mimic.Lighting;
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
		
		//Scene.create( new LoadScene() );
		//Scene.create( new MainScene() );
		
		//Scene.setActive( "LoadScene" );
		
	}
	
	@Override
	public void onUpdate() {
		
		//System.out.println( Input.isKeyHit( Key.SPACE ) );
		
		//if( Manager.getActiveScene().getName().equals( "MainScene" ) ) Lighting.update();
		
	}

	@Override
	public void onRender() {
		
		//if( Manager.getActiveScene().getName().equals( "MainScene" ) ) Lighting.render();
		
	}
	
}

public class Game {

	public static void main( String[] args ) {
		
		GameBase game = new GameBase();
		
		game.run();
		
	}
	
}
