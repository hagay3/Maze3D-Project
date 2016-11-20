package algorithms.search;

import java.io.Serializable;

/**
 * This class is used to define A State in a specific problem.
 * The problem is a 3D maze.
 */


public class State<T> implements Comparable<State<T>>,Serializable {
	private static final long serialVersionUID = 42L;
	private State<T> cameFrom;
	private double cost;
	private T value;
	private String key;
	public State<T> getCameFrom() {
		return cameFrom;
	}
	public void setCameFrom(State<T> cameFrom) {
		this.cameFrom = cameFrom;
	}
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public T getValue() {
		return value;
	}
	public void setValue(T value) {
		this.value = value;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	public State(T val) {
		this.value = val;
		this.key = val.toString();
	}
	
	
	@Override
	public String toString() {
		return value.toString();
	}
	
	/**
	 * Check if 2 given States equal
	 * @param obj , its the other State
	 * @return boolean true for equality and false either 
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		
		@SuppressWarnings("unchecked")
		State<T> other = (State<T>) obj;

		return other.getKey().equals(this.key);
	}
	@Override
	public int compareTo(State<T> s) {
		return (int)(this.getCost() - s.getCost());	
		// return > 0 if this > s
		//        < 0 if this < s
		//        = 0 if this == s
	}
	
	public int compare(State<T> s1,State<T> s2) {
		return (int)(s1.getCost() - s2.getCost());	
		// return > 0 if this > s
		//        < 0 if this < s
		//        = 0 if this == s
	}
	
	
}
