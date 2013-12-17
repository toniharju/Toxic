package Game;

import Mimic.Base;
import Mimic.Input;
import Mimic.Manager;
import Mimic.Resource;
import Mimic.Dialog;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Text;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard.Key;
import org.jsfml.window.Mouse;
import org.jsfml.window.Mouse.Button;

/**
 *
 * @author
 * Toni Harju
 */
public class Drunkard extends NPC {
	
	private boolean mIsInDialog = false;
	private Text mMainText = new Text();
	private Text[] mAnswerText = new Text[ 3 ];
	private Sprite mDialogBG = new Sprite();
	
	private String[] mNextLabel = new String[ 3 ];
	
	Drunkard() {
		
		super( "images/npc_1.png" );
		
		mDialogBG.setTexture( Resource.getTexture( "res/images/dialogMenu.png" ) );
		
		//float pos_y = Manager.getActiveScene().getView().getCenter().y + Base.getWindowHalfSize().y;
		
		mMainText.setFont( Resource.getFont( "res/fonts/arial.ttf" ) );
		//mMainText.setPosition( 20, pos_y - 100 );
		mMainText.setColor( Color.WHITE );
		
		for( int i = 0; i < 3; i++ ) {
			
			mAnswerText[ i ] = new Text();
			mAnswerText[ i ].setFont( Resource.getFont( "res/fonts/arial.ttf" ) );
			//mAnswerText[ i ].setPosition( 20, pos_y + 40 * i - 50 );
			
		}
		
	}
	
	@Override
	public void onCreate() {
		
		super.onCreate();
		
	}
	
	@Override
	public void onUpdate() {
		
		pointTo( mPlayer.getPosition() );
		
		if( isDistanceToPlayerLessThan( 150 ) ) {
			
			if( Input.isKeyHit( Key.E ) && !Base.isPaused() ) {
				
				Dialog.load( "res/dialog/drunkard.txt" );
				
				float pos_y = Manager.getActiveScene().getView().getCenter().y + Base.getWindowHalfSize().y;
				
				mMainText.setString( Dialog.get( "greet", Dialog.Param.Text ) );
				mMainText.setPosition( 20, pos_y - 220 );
				mAnswerText[ 0 ].setString( Dialog.get( "greet", Dialog.Param.Choice1 ) );
				mAnswerText[ 0 ].setPosition( 20, pos_y - 180 );
				mAnswerText[ 1 ].setString( Dialog.get( "greet", Dialog.Param.Choice2 ) );
				mAnswerText[ 1 ].setPosition( 20, pos_y - 140 );
				mAnswerText[ 2 ].setString( Dialog.get( "greet", Dialog.Param.Choice3 ) );
				mAnswerText[ 2 ].setPosition( 20, pos_y - 100 );
				mNextLabel[ 0 ] = Dialog.get( "greet", Dialog.Param.NextLabel1 );
				mNextLabel[ 1 ] = Dialog.get( "greet", Dialog.Param.NextLabel2 );
				mNextLabel[ 2 ] = Dialog.get( "greet", Dialog.Param.NextLabel3 );
				
				mDialogBG.setPosition( 0, Manager.getActiveScene().getView().getCenter().y + Base.getWindowHalfSize().y - 256 );
				
				Base.setPause( true );
				
				mIsInDialog = true;
				
			}
			
			if( Input.isKeyHit( Key.ESCAPE ) ) {
				
				Base.setPause( false );
				
				mIsInDialog = false;
				
			}
			
			if( Input.isMouseUp( Button.LEFT ) ) {
				
				Vector2f mouse_pos = Base.mapPixelToCoords( Mouse.getPosition( Base.getWindow() ) );
				
				for( int i = 0; i < 3; i++ ) {
					
					Vector2f pos = mAnswerText[ i ].getPosition();
					Vector2f size = new Vector2f( mAnswerText[ i ].getLocalBounds().width, mAnswerText[ i ].getLocalBounds().height );
					
					if( mouse_pos.x > pos.x && mouse_pos.x < pos.x + size.x && mouse_pos.y > pos.y && mouse_pos.y < pos.y + size.y ) {
						
						mMainText.setString( Dialog.get( mNextLabel[ i ], Dialog.Param.Text ) );
						mAnswerText[ 0 ].setString( Dialog.get( mNextLabel[ i ], Dialog.Param.Choice1 ) );
						mAnswerText[ 1 ].setString( Dialog.get( mNextLabel[ i ], Dialog.Param.Choice2 ) );
						mAnswerText[ 2 ].setString( Dialog.get( mNextLabel[ i ], Dialog.Param.Choice3 ) );
						mNextLabel[ 0 ] = Dialog.get( mNextLabel[ i ], Dialog.Param.NextLabel1 );
						mNextLabel[ 1 ] = Dialog.get( mNextLabel[ i ], Dialog.Param.NextLabel1 );
						mNextLabel[ 2 ] = Dialog.get( mNextLabel[ i ], Dialog.Param.NextLabel1 );
						
					}
					
				}
				
			}
			
		}
		
	}
	
	@Override
	public void onRender() {
		
		if( mIsInDialog ) {
			
			Base.draw( mDialogBG );
			Base.draw( mMainText );
			
			for( int i = 0; i < 3; i++ ) {
				
				Base.draw( mAnswerText[ i ] );
				
			}
			
		}
		
	}
	
}
