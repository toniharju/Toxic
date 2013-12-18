package Mimic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsfml.graphics.Color;
import org.jsfml.graphics.FloatRect;
import org.jsfml.graphics.Image;
import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.PrimitiveType;
import org.jsfml.graphics.RectangleShape;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.graphics.TextureCreationException;
import org.jsfml.graphics.Vertex;
import org.jsfml.graphics.VertexArray;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Keyboard.Key;
import org.jsfml.window.Mouse;

/**
 *
 * @author
 * Toni Harju
 */
public class GameMap {

	private static boolean mDebug = false;
	private final Vector2i mGridSize = new Vector2i( 32, 32 );
	private Vector2i mMouseGridPos = new Vector2i( 0, 0 );
	
	private String mMapFilename;
	
	private Vector2i mMapSize = new Vector2i( Base.getWindowSize().x, Base.getWindowSize().y );
	private Vector2i mMapChunks = new Vector2i( 0, 0 );
	private Vector2i mMapGridSize = new Vector2i( mMapSize.x / mGridSize.x, mMapSize.y / mGridSize.y );
	
	private int[][] mCollisionLayer = new int[ mMapGridSize.x ][ mMapGridSize.y ];
	
	private Sprite[][] mMapSprite;
	private VertexArray mLineArray = new VertexArray( PrimitiveType.LINES );
	private VertexArray mQuadArray = new VertexArray( PrimitiveType.QUADS );
	
	GameMap( String filename ) throws IOException {
		
		mMapFilename = filename;
		
		File f = new File( filename );
			
		f.createNewFile();
		
		try ( BufferedReader fReader = new BufferedReader( new FileReader( f ) ) ) {
			
			String line = fReader.readLine();
			
			while( line != null ) {
				
				switch( line ) {
					
					case "header:":
						
						mMapSize = new Vector2i( Integer.parseInt( fReader.readLine() ), Integer.parseInt( fReader.readLine() ) );
						mMapGridSize = new Vector2i( mMapSize.x / mGridSize.x, mMapSize.y / mGridSize.y );
						mCollisionLayer = new int[ mMapGridSize.x ][ mMapGridSize.y ];
						
						mMapChunks = new Vector2i( mMapSize.x / 512, mMapSize.y / 512 );
						
						mMapSprite = new Sprite[ mMapChunks.x ][ mMapChunks.y ];
						
						for( int y = 0; y < mMapChunks.y; y++ ) {
							
							for( int x = 0; x < mMapChunks.x; x++ ) {
								
								mMapSprite[ x ][ y ] = new Sprite();
								mMapSprite[ x ][ y ].setPosition( x * 512, y * 512 );
								
							}
							
						}
						
						break;
						
					case "assets:":
						
						
						
						break;
						
					case "collision:":
							
						for( int y = 0; y < mMapGridSize.y; y++ ) {
							
							line = fReader.readLine();
							
							for( int x = 0; x < mMapGridSize.x; x++ ) {
								
								mCollisionLayer[ x ][ y ] = Integer.parseInt( line.substring( x, x + 1 ) );
								
							}
							
						}
							
						break;
					
				}
				
				line = fReader.readLine();
				
			}
		}
			
		
		
		createDebugGrid();
		updateDebugMap();
		
	}
	
	public final int[][] getCollisionData() {
		
		return mCollisionLayer;
		
	}
	
	public int[][] getExpandedCollisionData() {
		
		int[][] temp = new int[ mCollisionLayer.length ][ mCollisionLayer[ 0 ].length ];
		
		for( int y = 0; y < mCollisionLayer[ 0 ].length; y++ ) {
			
			for( int x = 0; x < mCollisionLayer.length; x++ ) {
				
				if( mCollisionLayer[ x ][ y ] == 1 ) {
					
					temp[ x ][ y ] = 1;
					if( x + 1 < getMapGridSize().x ) temp[ x + 1 ][ y ] = 1; if( x + 1 < getMapGridSize().x && y - 1 >= 0 ) temp[ x + 1 ][ y - 1 ] = 1;
					if( x - 1 >= 0 ) temp[ x - 1 ][ y ] = 1;				 if( x - 1 >= 0 && y + 1 < getMapGridSize().y ) temp[ x - 1 ][ y + 1 ] = 1;
					if( y + 1 < getMapGridSize().y ) temp[ x ][ y + 1 ] = 1; if( x - 1 >= 0 && y - 1 >= 0 ) temp[ x - 1 ][ y - 1 ] = 1;
					if( y - 1 >= 0 ) temp[ x ][ y - 1 ] = 1;				 if( x + 1 < getMapGridSize().x && y + 1 < getMapGridSize().y ) temp[ x + 1 ][ y + 1 ] = 1;
					
				}
				
			}
			
		}
		
		return temp;
		
	}
	
	public void setImage( Image img ) { 
		
		if( img != null ) {
			
			for( int y = 0; y < mMapChunks.y; y++ ) {
				
				for( int x = 0; x < mMapChunks.x; x++ ) {
					
					Texture temp = new Texture();
					
					try {
						
						temp.loadFromImage( img, new IntRect( 512 * x, 512 * y, 512, 512 ) );
						mMapSprite[ x ][ y ].setTexture( temp );
						
					} catch ( TextureCreationException ex ) {
						
						Logger.getLogger( GameMap.class.getName() ).log( Level.SEVERE, null, ex );
						
					}
					
				}
				
			}
			
		}
	
	}
	
	public Vector2i getMapSize() { return mMapSize; }
	public Vector2i getMapGridSize() { return mMapGridSize; }
	public Vector2i getGridSize() { return mGridSize; }
	
	public static void toggleDebug() { mDebug = !mDebug; }
	
	public void render() {
		
		IntRect camera = new IntRect( ( int )( Manager.getActiveScene().getView().getCenter().x - Base.getWindowHalfSize().x ) / 512,
									  ( int )( Manager.getActiveScene().getView().getCenter().y - Base.getWindowHalfSize().y ) / 512, 
									  ( int )( Manager.getActiveScene().getView().getCenter().x + Base.getWindowHalfSize().x ) / 512,
									  ( int )( Manager.getActiveScene().getView().getCenter().y + Base.getWindowHalfSize().y ) / 512 );
		
		for( int y = camera.top; y <= camera.height; y++ ) {
			
			for( int x = camera.left; x <= camera.width; x++ ) {
				
				if( mMapSprite[ x ][ y ].getTexture() == null ) continue;
				
				Base.draw( mMapSprite[ x ][ y ] );
				
			}
			
		}
		
	}
	
	public void debugUpdate() {
		
		if( Input.isKeyUp( Key.F12 ) ) toggleDebug();
		
		if( !mDebug ) return;
		
		Vector2f mousePos = Base.mapPixelToCoords( Mouse.getPosition( Base.getWindow() ) );
		mMouseGridPos = new Vector2i( ( int ) ( mousePos.x / mGridSize.x ), ( int ) ( mousePos.y / mGridSize.y ) );
		
		if( mMouseGridPos.x >= 0 && mMouseGridPos.y >= 0 && mMouseGridPos.x < mMapGridSize.x && mMouseGridPos.y < mMapGridSize.y ) {
		
			if( Mouse.isButtonPressed( Mouse.Button.LEFT ) ) {
				
				mCollisionLayer[ mMouseGridPos.x ][ mMouseGridPos.y ] = 1;
				
			}
			
			if( Mouse.isButtonPressed( Mouse.Button.RIGHT ) ) {
				
				mCollisionLayer[ mMouseGridPos.x ][ mMouseGridPos.y ] = 0;
				
			}
			
			if( Input.isMouseUp( Mouse.Button.LEFT ) || Input.isMouseUp( Mouse.Button.RIGHT ) ) {
			
				updateDebugMap();
			
			}
		
		}
		
		if( Input.isKeyUp( Key.F5 ) ) {
			
			File f = new File( mMapFilename );
			
			try( BufferedWriter fWriter = new BufferedWriter( new FileWriter( f ) ) ) {
				
				fWriter.write( "header:" );						fWriter.newLine();
				fWriter.write( String.valueOf( mMapSize.x ) );	fWriter.newLine();
				fWriter.write( String.valueOf( mMapSize.y ) );	fWriter.newLine();
				fWriter.write( "assets:" );						fWriter.newLine();
				fWriter.write( "collision:" );					fWriter.newLine();
				
				for( int y = 0; y < mMapGridSize.y; y++ ) {
					
					for( int x = 0; x < mMapGridSize.x; x++ ) {
						
						fWriter.write( String.valueOf( mCollisionLayer[ x ][ y ] ) );
						
					}
					
					fWriter.newLine();
					
				}
				
				fWriter.close();
				
			} catch ( IOException ex ) {
				
				Logger.getLogger( GameMap.class.getName() ).log( Level.SEVERE, null, ex );
				
			}
			
		}
		
	}
	
	public void debugRender() {
		
		if( !mDebug ) return;
		
		RectangleShape selectedGrid = new RectangleShape( new Vector2f( mGridSize ) );
		selectedGrid.setFillColor( Color.TRANSPARENT );
		selectedGrid.setOutlineThickness( 2 );
		selectedGrid.setOutlineColor( Color.MAGENTA );
		selectedGrid.setPosition( 32 * mMouseGridPos.x, 32 * mMouseGridPos.y );
		
		Base.draw( mLineArray );
		Base.draw( mQuadArray );
		Base.draw( selectedGrid );
		
	}
	
	private void createDebugGrid() {
		
		int horizontal_lines = ( int ) ( mMapSize.y / mGridSize.y );
		int vertical_lines = ( int ) ( mMapSize.x / mGridSize.x );
		
		for( int i = 1; i < horizontal_lines; i++ ) {
			
			mLineArray.add( new Vertex( new Vector2f( 0, 32 * i ), Color.WHITE ) );
			mLineArray.add( new Vertex( new Vector2f( mMapSize.x, 32 * i ), Color.WHITE ) );
			
		}
		
		for( int i = 1; i < vertical_lines; i++ ) {
			
			mLineArray.add( new Vertex( new Vector2f( 32 * i, 0 ), Color.WHITE ) );
			mLineArray.add( new Vertex( new Vector2f( 32 * i, mMapSize.y ), Color.WHITE ) );
			
		}
		
	}
	
	private void updateDebugMap() {

		mQuadArray.clear();
		
		int[][] expandedCollision = getExpandedCollisionData();
		
		for( int y = 0; y < mMapGridSize.y; y++ ) {
			
			for( int x = 0; x < mMapGridSize.x; x++ ) {
				
				if( expandedCollision[ x ][ y ] == 1 ) {
					
					mQuadArray.add( new Vertex( new Vector2f( 4 + mGridSize.x * x, 4 + mGridSize.y * y ), Color.MAGENTA ) );
					mQuadArray.add( new Vertex( new Vector2f( 4 + mGridSize.x * x, 4 + mGridSize.y * y + mGridSize.x - 8 ), Color.MAGENTA) );
					mQuadArray.add( new Vertex( new Vector2f( 4 + mGridSize.x * x + mGridSize.x - 8, 4 + mGridSize.y * y + mGridSize.x - 8 ), Color.MAGENTA ) );
					mQuadArray.add( new Vertex( new Vector2f( 4 + mGridSize.x * x + mGridSize.x - 8, 4 + mGridSize.y * y ), Color.MAGENTA ) );
					
				}
				
				if( mCollisionLayer[ x ][ y ] == 0 ) continue;

				mQuadArray.add( new Vertex( new Vector2f( 4 + mGridSize.x * x, 4 + mGridSize.y * y ), Color.RED ) );
				mQuadArray.add( new Vertex( new Vector2f( 4 + mGridSize.x * x, 4 + mGridSize.y * y + mGridSize.x - 8 ), Color.RED ) );
				mQuadArray.add( new Vertex( new Vector2f( 4 + mGridSize.x * x + mGridSize.x - 8, 4 + mGridSize.y * y + mGridSize.x - 8 ), Color.RED ) );
				mQuadArray.add( new Vertex( new Vector2f( 4 + mGridSize.x * x + mGridSize.x - 8, 4 + mGridSize.y * y ), Color.RED ) );

			}
			
		}
		
	}
	
	public boolean contains( Vector2f position ) {
		
		if( position.x >= 0 && position.x <= mMapSize.x && position.y >= 0 && position.y <= mMapSize.y ) return true;
		
		return false;
		
	}
	
	public boolean gridContains( Vector2i position ) {
		
		if( position.x >= 0 && position.x < mMapGridSize.x && position.y >= 0 && position.y < mMapGridSize.y ) return true;
		
		return false;
		
	}
	
	public boolean intersects( FloatRect rect ) {
		
		if( rect.left >= 0 && rect.left + rect.width <= mMapSize.x && rect.top >= 0 && rect.top + rect.height <= mMapSize.y ) return true;
		
		return false;
		
	}
	
	public boolean gridIntersects( IntRect rect ) {
		
		if( rect.left >= 0 && rect.left + rect.width < mMapGridSize.x && rect.top >= 0 && rect.top + rect.height < mMapGridSize.y ) return true;
		
		return false;
		
	}
	
}
