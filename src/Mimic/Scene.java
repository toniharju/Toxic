package Mimic;

import org.jsfml.graphics.View;

/**
 *
 * @author
 * Toni Harju
 */
public abstract class Scene implements IMainEvents {
	
	private String mName;
	
	private View mView = new View();

	public Scene( String name ) {
		
		mName = name;
		mView = ( View ) Base.getWindow().getDefaultView();
		
	}
	
	public String getName() { return mName; }
	public View getView() { return mView; }
	
	@Override
	public void onCreate() {}
	
	@Override
	public void onDestroy() {}

}
