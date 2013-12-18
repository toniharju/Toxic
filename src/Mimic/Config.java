package Mimic;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author
 * Roope Miettunen
 */
public class Config {
    
    private static Map< String, String > mCache = new HashMap<>();
    
    public static void load( String fileName ) {
  
        File f = new File( fileName );
        
        try( BufferedReader reader = new BufferedReader( new FileReader( f ) ) ) {
            
            while( true ) {
                
                String line;
                
                if( ( line = reader.readLine() ) == null ) break;
                
                line = line.trim();
                
                if( line.length() == 0 ) continue;
                
                int separator_pos = line.indexOf( "=" );
              
                /* Reads key and value from file with separator_position */
                String key;
                String value;
                
                key = line.substring( 0, separator_pos );
                value = line.substring( separator_pos + 1, line.length() );
				
                mCache.put( key.trim(), value.trim() );
                
            }
            
        } catch ( IOException ex ) {
            
            Logger.getLogger( Config.class.getName() ).log( Level.SEVERE, null, ex );
            
        }
        
    }
    
    public static String get( String key ) {
		
        return mCache.containsKey( key ) ? mCache.get( key ) : "";
        
    }

}
