package Mimic;

import java.util.EnumMap;

/**
 *
 * @author
 * Niko Kaukonen
 */
public class Inventory {
	
	public enum ItemType {
		
		Weapon,
		Ammo,
		Quest
		
	}
	
	public enum Item {
		
		Pistol,
		Shotgun,
		
	}
	
	private static EnumMap< ItemType, EnumMap< Item, Integer > > mItemList = new EnumMap( ItemType.class );
	
	public static void initialize() {
		
		mItemList.put( ItemType.Weapon, new EnumMap< Item, Integer >( Item.class ) );
		mItemList.put( ItemType.Ammo, new EnumMap< Item, Integer >( Item.class ) );

		EnumMap< Item, Integer > temp = new EnumMap( Item.class );
		temp.put( Item.Pistol, 0 );
		temp.put( Item.Shotgun, 0 );
		mItemList.put( ItemType.Weapon, temp );
		
		temp = new EnumMap( Item.class );
		temp.put( Item.Pistol, 0 );
		temp.put( Item.Shotgun, 0 );
		mItemList.put( ItemType.Ammo, temp );
		
	}
	
	public static int get( ItemType type, Item item ) {
		
		if( mItemList.containsKey( type ) && mItemList.get( type ).containsKey( item ) )
			return mItemList.get( type ).get( item );
		
		return 0;
		
	}
	
	public static void set( ItemType type, Item item, Integer value ) {
		
		if( mItemList.containsKey( type ) && mItemList.get( type ).containsKey( item ) )
			mItemList.get( type ).put( item, value );
		
	}
	
}
