package Mimic;

import java.util.HashMap;
import org.jsfml.graphics.IntRect;
import org.jsfml.graphics.Sprite;
import org.jsfml.graphics.Texture;
import org.jsfml.system.Vector2f;

/**
 *
 * @author
 * Toni Harju
 */
public abstract class Entity extends Sprite implements IMainEvents, ICollisionEvents {
	
	private int mListId;
	
	private String mType = "Base";
	
	private boolean mIsPushable = false;
	private boolean mIsMapCollisionAllowed = true;
	
	private Vector2f mVelocity = new Vector2f( 0, 0 );
	
	private IntRect mBoundingBox = new IntRect( 0, 0, 0, 0 );
	private HashMap< String, Boolean > mCollidableTypeMap = new HashMap();
	
	public Entity( String textureFile ) {
		
		Texture temp = Resource.getTexture( "res/" + textureFile );
		
		setTexture( temp );
		
		mBoundingBox = new IntRect( 0, 0, getTexture().getSize().x, getTexture().getSize().y );
		
	}
	
	protected void setType( String type ) { mType = type; }
	protected void setId( int id ) { mListId = id; }
	
	public void addCollidableType( String type ) { mCollidableTypeMap.put( type, true ); }
	public HashMap< String, Boolean > getCollidableTypes() { return mCollidableTypeMap; }
	
	public void setPushable( boolean toggle ) { mIsPushable = toggle; }
	public void setBoundingBox( IntRect box ) { mBoundingBox = box; }
	
	public void setMapCollisionAllowed( boolean set ) { mIsMapCollisionAllowed = set; }
	public boolean isMapCollisionAllowed() { return mIsMapCollisionAllowed; }
	
	public int getId() { return mListId; }
	public String getType() { return mType; }
	public IntRect getBoundingBox() { return mBoundingBox; }
	public boolean isPushable() { return mIsPushable; }
	
	public void setVelocity( Vector2f velocity ) { mVelocity = velocity; }
	public void setVelocity( float x, float y ) { mVelocity = new Vector2f( x, y ); }
	public Vector2f getVelocity() { return mVelocity; }
	
	public void moveTo( Vector2f position, float speed ) {
		
		Vector2f delta = new Vector2f( position.x - getPosition().x, position.y - getPosition().y );
		
		float angle = ( float ) Math.atan2( delta.y, delta.x );
		float degAngle = ( float ) Math.toDegrees( angle );
		
		if( delta.x * delta.x + delta.y * delta.y < speed * speed ) {
		
			setPosition( position.x, position.y );
			
		} else {
			
			setVelocity( ( float ) ( speed * Base.cos( degAngle ) ), ( float ) ( speed * Base.sin( degAngle ) ) );
			
		}
		
	}
	
	public void pointTo( Vector2f position ) {
		
		Vector2f delta = new Vector2f( position.x - getPosition().x, position.y - getPosition().y );
		
		float angle = ( float ) Math.toDegrees( Math.atan2( delta.y, delta.x ) );
		
		setRotation( 90 + angle );
		
	}
	
	public void pointTo( Vector2f position, float speed ) {
		
		Vector2f delta = new Vector2f( position.x - getPosition().x, position.y - getPosition().y );
		
		float angle = ( float ) Math.atan2( delta.y, delta.x );
		
		angle = angle - ( float ) Math.toRadians( getRotation() + 90 );
		
		float absolute_angle = Math.abs( angle );
		
		if( absolute_angle > 3.13 && absolute_angle < 3.15 ) return;
		
		if( angle > Math.PI )
			angle -= 2 * Math.PI;
		else if( angle < -Math.PI )
			angle += 2 * Math.PI;

		rotate( angle > 0 ? -speed : speed );
		
	}
	
	@Override
	public void onMapCollision() {}
	
	@Override
	public void onEntityCollision( Entity that ) {}
	
	@Override
	public void onCreate() {}
	
	@Override
	public void onDestroy() {}
	
}
