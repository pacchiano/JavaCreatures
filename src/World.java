
public abstract class World {

	
	public World( ) {}
	

	
	public abstract int[] get_state();

	public abstract void reset_world();
	
	
	public abstract void step(int creature_action);
	
	
}
