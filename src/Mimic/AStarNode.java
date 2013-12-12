package Mimic;

import org.jsfml.system.Vector2i;

/**
 *
 * @author
 * Toni Harju
 */
public class AStarNode {
	
	private static int mIdCounter = -1;
	
	private int mId;
	
	private Vector2i mPosition;
	
	private int mF;
	private int mG;
	private int mH;
	
	private AStarNode mParent;
	
	private static Vector2i mGoal;
	
	AStarNode( Vector2i position, Vector2i goal ) {
		
		mIdCounter++;
		mId = mIdCounter;
		
		mPosition = position;
		mG = 0;
		mH = 0;
		mF = 0;
		
		mGoal = goal;
		
	}
	
	AStarNode( Vector2i position, int g ) {
		
		mIdCounter++;
		mId = mIdCounter;
		
		mPosition = position;
		mG = g;
		mH = ( Math.abs( mGoal.x - position.x ) + Math.abs( mGoal.y - position.y ) ) * 10;
		
		mF = mG + mH;
		
	}
	
	AStarNode( Vector2i position, int g, AStarNode parent ) {
		
		mIdCounter++;
		mId = mIdCounter;
		
		mPosition = position;
		mParent = parent;
		mG = g + parent.getG();
		mH = ( Math.abs( mGoal.x - position.x ) + Math.abs( mGoal.y - position.y ) ) * 10;
		
		mF = mG + mH;

	}
	
	public int getX() { return mPosition.x; }
	public int getY() { return mPosition.y; }
	
	public int getF() { return mF; }
	public int getG() { return mG; }
	public int getH() { return mH; }
	
	public AStarNode getParent() { return mParent; }
	
	public int getId() { return mId; }
	
}
