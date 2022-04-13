//
//enum CreatureActions{
//	NULL,
//	STAY,
//	ZERO,
//	ONE,
//	TWO,
//	THREE,
//}





public abstract class Creature {
	protected double ultimate_reward_collected = 0;

	protected int state_dimension;
	
	public Creature( int state_dimension  ) {
		this.state_dimension = state_dimension;
		
	}

	
	public int get_state_dimension() {
		return this.state_dimension;
	}
	
	
	public double get_ultimate_reward_collected() {
		return this.ultimate_reward_collected;
	}

	
	
	
	//// This method returns PairIntegerDouble. The integer corresponds to the action index. 
	//// The double equals the reward r(state, action). 
	//// In this implementation the reward is an internal property of the creature.
	public abstract PairIntegerDouble step(int[] state);
	
	
	public abstract void process_evolver_advice(double[] evolver_advice);
	
	
	public abstract double[] get_creature_info();

	
	public abstract void reset();
	
	public abstract void update(int[] state, PairIntegerDouble action_reward, int[] next_state);
	
}
