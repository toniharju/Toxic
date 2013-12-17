package Mimic;

import java.io.*;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/*enum DialogParam {

    Text,
    Choice1,
    Choice2,
    Choice3,
    NextLabel1,
    NextLabel2,
    NextLabel3
}*/

/**
 *
 * @author
 * Juha Kleemola & Toni Harju
 */
public class Dialog {
    
	public enum Param {
		
		Text,
		Choice1,
		Choice2,
		Choice3,
		NextLabel1,
		NextLabel2,
		NextLabel3
		
	}
	
    private static HashMap< String, HashMap< Integer, String > > mDialogList = new HashMap<>();
    
	public static String get( String label, Dialog.Param type ) {
		
		if( mDialogList.containsKey( label ) ) {
			
			if( mDialogList.get( label ).containsKey( type.ordinal() ) ) {
				
				String text = mDialogList.get( label ).get( type.ordinal() );
				
				return text;
				
			}
			
		}
		
		return "";
		
	}
	
	public static void load( String txtFile ) {

		File f1 = new File( txtFile );
  
		String line;
  
		try {
   
			String current_label = "";
        
			BufferedReader in1 = new BufferedReader( new FileReader( f1 ) );
   
			boolean isFirst = true;
        
			HashMap< Integer, String > buffer = new HashMap();
        
			while( true ) {
                
				if ( ( line = in1.readLine() ) == null ) break;
				line = line.trim();
				if( line.isEmpty() ) continue;
 
				if( line.endsWith( ":" ) ) {
                    
					if( !isFirst ) mDialogList.put( current_label, buffer );
                    
					buffer = new HashMap();
                            
					current_label = line.substring( 0, line.indexOf( ":" ) );

					isFirst = false;
                    
				} else {
                    
					if( line.startsWith( "T" ) ) {
                        
						int text = Dialog.Param.Text.ordinal();

						buffer.put( text, line.substring( line.indexOf( "\"" ) + 1, line.lastIndexOf( "\"" ) ) );

					} else if( line.startsWith( "C" ) ) {
                        
						int choice1 = Dialog.Param.Choice1.ordinal();
						int nextlabel1 = Dialog.Param.NextLabel1.ordinal();
                            
						int charAt = ( int )line.charAt( 1 ) - 48;
                            
						buffer.put( choice1 + charAt, line.substring( line.indexOf( "\"" ) + 1, line.lastIndexOf( "\"" ) ) );
                            
						int spaces_found = 0;
						String temp_buffer = "";
                            
						for( int i = 0; i < line.length(); i++ ) {
                                
							if( line.charAt( i ) == ' ' ) {
                                    
								spaces_found++;
                                    
							} else {
                                    
								if( spaces_found == 1 ) temp_buffer += line.charAt( i );
                                   
							}
                                
							if( spaces_found == 2 ) break;
                                
						}
                            
						buffer.put( nextlabel1 + charAt, temp_buffer ); 
                        
					}
                    
				}
     
			}
   
		} catch ( IOException ex ) {
			
			Logger.getLogger( Dialog.class.getName() ).log( Level.SEVERE, null, ex );
			
		}
		
	}

}
