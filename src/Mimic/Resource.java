package Mimic;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsfml.audio.Music;
import org.jsfml.audio.Sound;
import org.jsfml.audio.SoundBuffer;
import org.jsfml.graphics.Font;
import org.jsfml.graphics.Image;
import org.jsfml.graphics.Texture;

/**
 *
 * @author
 * Toni Harju
 */
public class Resource {
	
	private static HashMap< String, Font > mFontList = new HashMap();
	private static HashMap< String, Image > mImageList = new HashMap();
	private static HashMap< String, SoundBuffer > mSoundList = new HashMap();
	private static HashMap< String, Music > mMusicList = new HashMap();
	private static HashMap< String, GameMap > mMapList = new HashMap();
	private static HashMap< String, Texture > mTextureList = new HashMap();
	
	public static Font getFont( String filename ) {
		
		if( !mFontList.containsKey( filename ) ) {
			
			Font temp = new Font();
			
			try {
				
				temp.loadFromFile( Paths.get( filename ) );
				
				mFontList.put( filename, temp );
				
			} catch ( IOException ex ) {
				
				Logger.getLogger( Resource.class.getName() ).log( Level.SEVERE, null, ex );
				
			}
			
		}
		
		return mFontList.get( filename );
		
	}
	
	public static Image getImage( String filename ) {
		
		if( !mImageList.containsKey( filename ) ) {
			
			Image temp = new Image();
			
			try {
				
				temp.loadFromFile( Paths.get( filename ) );
				System.out.println( "Loaded Image: " + filename );
				mImageList.put( filename, temp );
				
			} catch ( IOException ex ) {
				
				Logger.getLogger( Resource.class.getName() ).log( Level.SEVERE, null, ex );
				
			}
			
		}
		
		return mImageList.get( filename );
		
	}
	
	public static Texture getTexture( String filename ) {
		
		if( !mTextureList.containsKey( filename ) ) {
		
			Texture temp = new Texture();
			
			try {
			
				temp.loadFromFile( Paths.get( filename ) );
				System.out.println( "Loaded Texture: " + filename );
				mTextureList.put( filename, temp );
			
			} catch ( IOException ex ) {
			
				Logger.getLogger( Resource.class.getName() ).log( Level.SEVERE, null, ex );
			
			}
			
		}
		
		return mTextureList.get( filename );
		
	}
	
	public static GameMap getGameMap( String filename ) {
		
		if( !mMapList.containsKey( filename ) ) {
			
			try {
				
				GameMap temp = new GameMap( filename );
				System.out.println( "Loaded Map: " + filename );
				mMapList.put( filename, temp );
				
			} catch ( IOException ex ) {
				
				Logger.getLogger( Resource.class.getName( )).log( Level.SEVERE, null, ex );
				
			}
			
		}
		
		return mMapList.get( filename );
		
	}
	
	public static Music getMusic( String filename ) {
		
		if( !mMusicList.containsKey( filename ) ) {
			
			Music temp = new Music();
			
			try {
				
				temp.openFromFile( Paths.get( filename ) );
				System.out.println( "Opened Music: " + filename );
				mMusicList.put( filename, temp );
				
			} catch ( IOException ex ) {
				
				Logger.getLogger(Resource.class.getName()).log(Level.SEVERE, null, ex);
				
			}
			
		}
		
		return mMusicList.get( filename );
		
	}
	
	public static SoundBuffer getSound( String filename ) {
		
		if( !mSoundList.containsKey( filename ) ) {
			
			SoundBuffer temp = new SoundBuffer();
			
			try {
				
				temp.loadFromFile( Paths.get( filename ) );
				System.out.println( "Opened Music: " + filename );
				mSoundList.put( filename, temp );
				
			} catch ( IOException ex ) {
				
				Logger.getLogger(Resource.class.getName()).log(Level.SEVERE, null, ex);
				
			}
			
		}
		
		return mSoundList.get( filename );
		
	}
	
}
