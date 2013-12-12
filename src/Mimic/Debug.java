package Mimic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.PrimitiveType;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.Vertex;
import org.jsfml.graphics.VertexArray;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Keyboard;
import org.jsfml.window.Keyboard.Key;
import org.jsfml.window.Mouse;

/**
 *
 * @author
 * Toni Harju
 */
public class Debug {
	
	private static boolean mStatus;
	
	private static final Vector2f mGridSize = new Vector2f( 32, 32 );
	private static final Vector2f mMapSize = new Vector2f( 1024, 768 );
	private static final Vector2i mMapGridSize = new Vector2i( ( int )( mMapSize.x / mGridSize.x ), ( int )( mMapSize.y / mGridSize.y ) );
	
	private static Vector2i mMouseGridPos = new Vector2i( 0, 0 );
	
	private static int[][] mCollisionData = new int[ ( int )mGridSize.x ][ ( int )mGridSize.y ];
	
	public static void toggle() { mStatus = !mStatus; }
	
	public static boolean isDebugOn() { return mStatus; }
	
	public static void update() {
		
		if( mStatus == false ) return;
		
		Vector2f mousePos = Base.mapPixelToCoords( Mouse.getPosition( Base.getWindow() ) );
		mMouseGridPos = new Vector2i( ( int ) ( mousePos.x / mGridSize.x ), ( int ) ( mousePos.y / mGridSize.y ) );
		
		if( mMouseGridPos.x >= 0 && mMouseGridPos.y >= 0 && mMouseGridPos.x < mMapGridSize.x && mMouseGridPos.y < mMapGridSize.y ) {
		
			if( Mouse.isButtonPressed( Mouse.Button.LEFT ) ) mCollisionData[ mMouseGridPos.x ][ mMouseGridPos.y ] = 1;
			if( Mouse.isButtonPressed( Mouse.Button.RIGHT ) ) mCollisionData[ mMouseGridPos.x ][ mMouseGridPos.y ] = 0;
		
		}
		
		if( Keyboard.isKeyPressed( Key.F5 ) ) {
			
			File f = new File( "res/maps/map_data.txt" );
			
			try {
				
				FileWriter fWriter = new FileWriter( f );
				
				
				
			} catch ( IOException ex ) {
				
				Logger.getLogger( Debug.class.getName() ).log( Level.SEVERE, null, ex );
				
			}
			
		}
			
	}
	
	public static void render() {
		
		if( mStatus == false ) return;
		
		int horizontal_lines = ( int ) ( mMapSize.y / mGridSize.y );
		int vertical_lines = ( int ) ( mMapSize.x / mGridSize.x );
		
		VertexArray lines = new VertexArray();
		lines.setPrimitiveType( PrimitiveType.LINES );
		
		for( int i = 1; i < horizontal_lines; i++ ) {
			
			lines.add( new Vertex( new Vector2f( 0, 32 * i ), Color.WHITE ) );
			lines.add( new Vertex( new Vector2f( mMapSize.x, 32 * i ), Color.WHITE ) );
			
		}
		
		for( int i = 1; i < vertical_lines; i++ ) {
			
			lines.add( new Vertex( new Vector2f( 32 * i, 0 ), Color.WHITE ) );
			lines.add( new Vertex( new Vector2f( 32 * i, mMapSize.y ), Color.WHITE ) );
			
		}
		
		VertexArray walls = new VertexArray( PrimitiveType.QUADS );

		for( int y = 0; y < mMapGridSize.y; y++ ) {
			
			for( int x = 0; x < mMapGridSize.x; x++ ) {
				
				if( mCollisionData[ x ][ y ] == 0 ) continue;
				
				walls.add( new Vertex( new Vector2f( 4 + mGridSize.x * x, 4 + mGridSize.y * y ), Color.RED ) );
				walls.add( new Vertex( new Vector2f( 4 + mGridSize.x * x, 4 + mGridSize.y * y + mGridSize.x - 8 ), Color.RED ) );
				walls.add( new Vertex( new Vector2f( 4 + mGridSize.x * x + mGridSize.x - 8, 4 + mGridSize.y * y + mGridSize.x - 8 ), Color.RED ) );
				walls.add( new Vertex( new Vector2f( 4 + mGridSize.x * x + mGridSize.x - 8, 4 + mGridSize.y * y ), Color.RED ) );

			}
			
		}
		
		RectangleShape selectedGrid = new RectangleShape( mGridSize );
		selectedGrid.setFillColor( Color.TRANSPARENT );
		selectedGrid.setOutlineThickness( 2 );
		selectedGrid.setOutlineColor( Color.MAGENTA );
		selectedGrid.setPosition( 32 * mMouseGridPos.x, 32 * mMouseGridPos.y );
		
		Base.draw( lines );
		Base.draw( walls );
		Base.draw( selectedGrid );
		
	}
	
}
