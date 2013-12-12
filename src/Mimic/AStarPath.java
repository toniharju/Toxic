package Mimic;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import org.jsfml.graphics.IntRect;
import org.jsfml.system.Vector2i;

/**
 *
 * @author
 * Toni Harju
 */
public class AStarPath {
	
	private static List< AStarNode > mOpenList = new LinkedList();
	private static List< AStarNode > mClosedList = new LinkedList();
	
	private static List< AStarNode > removeNodeFromList( AStarNode node, List< AStarNode > list ) {
		
		int i = 0;
		for( AStarNode n : list ) {
			
			if( n.getId() == node.getId() ) {
				
				list.remove( i );
				break;
				
			}
			
			i++;
			
		}
		
		return list;
		
	}
	
	private static boolean isNodeInList( AStarNode node, List< AStarNode > list ) {
		
		for( AStarNode n : list ) {
			
			if( n.getId() == node.getId() ) return true;
			
		}
		
		return false;
		
	}
	
	private static AStarNode getCheapestNode( List< AStarNode > list ) {
		
		AStarNode cheapest = list.get( 0 );
		
		for( AStarNode n : list ) {
			
			if( n.getF() < cheapest.getF() ) cheapest = n;
			
		}
		
		return cheapest;
		
	}
	
	private static AStarNode getNodeByPosition( Vector2i position, List< AStarNode > list ) {
		
		for( AStarNode n : list ) {
			
			if( n.getX() == position.x && n.getY() == position.y ) return n;
			
		}
		
		return null;
		
	}
	
	public static List< Vector2i > generatePath( int[][] collisionData, Vector2i from, Vector2i to ) {

		List< AStarNode > closedList = createNodes( collisionData, from, to );
		List< Vector2i > pathList = new LinkedList();
		
		AStarNode currentNode = closedList.get( closedList.size() - 1 );
		
		pathList.add( new Vector2i( currentNode.getX(), currentNode.getY() ) );
		
		while( true ) {
			
			//if( currentNode == null ) return pathList;
			
			if( currentNode.getX() == from.x && currentNode.getY() == from.y ) {
				
				Collections.reverse( pathList );
				
				return pathList;
				
			}
			
			currentNode = currentNode.getParent();
			
			pathList.add( new Vector2i( currentNode.getX(), currentNode.getY() ) );
			
		}
		
	}
	
	private static List< AStarNode > createNodes( int[][] collisionData, Vector2i from, Vector2i to ) {
		
		mOpenList = new LinkedList();
		mClosedList = new LinkedList();
		
		IntRect collisionBounds = new IntRect( 0, 0, collisionData.length, collisionData[ 0 ].length );
		
		AStarNode current = new AStarNode( from, to );
		
		mOpenList.add( current );
		
		while( true ) {
			
			if( current.getX() == to.x && current.getY() == to.y ) break;
			
			for( int y = current.getY() - 1; y < current.getY() + 2; y++ ) {
				
				for( int x = current.getX() - 1; x < current.getX() + 2; x++ ) {
					
					if( ( x == current.getX() && y == current.getY() ) ||
						( !collisionBounds.contains( new Vector2i( x, y ) ) ) ||
						( getNodeByPosition( new Vector2i( x, y ), mClosedList ) != null ) ) continue;
					
					//if( !isPositionInList( new Vector2i( x, y ), mClosedList ) ) {
					
					AStarNode tempNode = getNodeByPosition( new Vector2i( x, y ), mOpenList );
					
					if( tempNode != null ) {
						
						if( x == current.getX() || y == current.getY() ) {
								
							if( current.getG() + 10 < tempNode.getG() ) {
									
								removeNodeFromList( tempNode, mOpenList );
								tempNode = new AStarNode( new Vector2i( x, y ), 10, current );
								mOpenList.add( tempNode );
									
							} else {
									
								continue;
									
							}
								
						} else {
								
							if( current.getG() + 14 < tempNode.getG() ) {
									
								removeNodeFromList( tempNode, mOpenList );
								tempNode = new AStarNode( new Vector2i( x, y ), 14, current );
								mOpenList.add( tempNode );
									
							} else {
									
								continue;
									
							}
								
						}
							
						continue;
							
					}

					if( collisionData[ x ][ y ] != 1 ) {
						
						//Orthogonal
						if( x == current.getX() || y == current.getY() ) {

							mOpenList.add( new AStarNode( new Vector2i( x, y ), 10, current ) );
							
						//Diagonal
						} else {
								
							int tx = current.getX() - x;
							int ty = current.getY() - y;
								
							//!isCoordinatesOutOfBounds( new Vector2i( x, y + ty ), collisionBounds ) SHOULDN'T BE NEEDED
							if( collisionData[ x + tx ][ y ] != 1 && collisionData[ x ][ y + ty ] != 1 ) {
								
								mOpenList.add( new AStarNode( new Vector2i( x, y ), 14, current ) );
									
							}
								
						}
						
					}
						
					//}
					
				}
				
			}
			
			mOpenList = removeNodeFromList( current, mOpenList );
			mClosedList.add( current );
			
			Collections.sort( mOpenList, new Comparator< AStarNode >() {

				@Override
				public int compare( AStarNode n1, AStarNode n2 ) {
					
					return ( n1.getF() < n2.getF() ) ? -1 : ( n1.getF() > n2.getF() ) ? 1 : 0;
					
				}
			
			} );
			
			current = mOpenList.get( 0 );
			//current = getCheapestNode( mOpenList );
			
			mOpenList = removeNodeFromList( current, mOpenList );
			mClosedList.add( current );
			
		}
		
		return mClosedList;
		
	}
	
}