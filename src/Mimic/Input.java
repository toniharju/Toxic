package Mimic;

import org.jsfml.window.Keyboard;
import org.jsfml.window.Keyboard.Key;
import org.jsfml.window.Mouse;
import org.jsfml.window.Mouse.Button;
import org.jsfml.window.event.Event;

/**
 *
 * @author
 * Toni Harju
 */
public class Input {
	
	private static boolean[] mKeyPressed = new boolean[ Key.values().length ];
	private static boolean[] mKeyWasPressed = new boolean[ Key.values().length ];
	private static boolean[] mKeyReleased = new boolean[ Key.values().length ];
	
	private static boolean[] mMousePressed = new boolean[ Button.values().length ];
	private static boolean[] mMouseWasPressed = new boolean[ Button.values().length ];
	private static boolean[] mMouseReleased = new boolean[ Button.values().length ];
	
	public static boolean isKeyDown( Key key ) {
		
		if( Keyboard.isKeyPressed( key ) ) return true;
		
		return false;
		
	}
	
	public static boolean isKeyHit( Key key ) {
		
		if( mKeyPressed[ key.ordinal() ] ) {
			
			mKeyPressed[ key.ordinal() ] = false;
			
			return true;
			
		}
		
		return false;
		
	}
	
	public static boolean isKeyUp( Key key ) {
		
		if( mKeyReleased[ key.ordinal() ] ) {
			
			mKeyReleased[ key.ordinal() ] = false;
			return true;
			
		}
		
		return false;
		
	}
	
	public static boolean isMouseDown( Button button ) {
		
		return Mouse.isButtonPressed( button );
		
	}
	
	public static boolean isMouseHit( Button button ) {
		
		if( mMousePressed[ button.ordinal() ] ) {
			
			mMousePressed[ button.ordinal() ] = false;
			return true;
			
		}
		
		return false;
		
	}
	
	public static boolean isMouseUp( Button button ) {
		
		if( mMouseReleased[ button.ordinal() ] ) {
			
			mMouseReleased[ button.ordinal() ] = false;
			return true;
			
		}
		
		return false;
		
	}
	
	public static void updateEvents( Event event ) {
		
		for( int i = 0; i < Mouse.Button.values().length; i++ ) {
			
			mKeyReleased[ i ] = false;
			mMouseReleased[ i ] = false;
			
		}
		
		switch( event.type ) {
			
			case KEY_PRESSED:

				if( !mKeyWasPressed[ event.asKeyEvent().key.ordinal() ] ) {
				
					mKeyPressed[ event.asKeyEvent().key.ordinal() ] = true;
					mKeyWasPressed[ event.asKeyEvent().key.ordinal() ] = true;
					
				}
				
				break;
				
			case KEY_RELEASED:
				
				mKeyReleased[ event.asKeyEvent().key.ordinal() ] = true;
				mKeyPressed[ event.asKeyEvent().key.ordinal() ] = false;
				mKeyWasPressed[ event.asKeyEvent().key.ordinal() ] = false;
				
				break;
				
			case MOUSE_BUTTON_PRESSED:
				
				if( !mMouseWasPressed[ event.asMouseButtonEvent().button.ordinal() ] ) {
					
					mMousePressed[ event.asMouseButtonEvent().button.ordinal() ] = true;
					mMouseWasPressed[ event.asMouseButtonEvent().button.ordinal() ] = true;
					
				}
				
				break;
				
			case MOUSE_BUTTON_RELEASED:
				
				mMouseReleased[ event.asMouseButtonEvent().button.ordinal() ] = true;
				mMousePressed[ event.asMouseButtonEvent().button.ordinal() ] = false;
				mMouseWasPressed[ event.asMouseButtonEvent().button.ordinal() ] = false;
				
				break;
			
		}
		
	}
	
}
