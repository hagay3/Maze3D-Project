package algorithms.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to define the solution that Searcher figured out in Searchable object.
 */

public class Solution<T> implements Serializable{
	/**
	 * serial number
	 */
	private static final long serialVersionUID = 42L;
	
	private List<State<T>> states = new ArrayList<State<T>>();

	public List<State<T>> getStates() {
		return states;
	}

	public void setStates(List<State<T>> states) {
		this.states = states;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (State<T> s : states) {
			sb.append(s.toString()).append(" ");
		}
		return sb.toString();
	}
}
