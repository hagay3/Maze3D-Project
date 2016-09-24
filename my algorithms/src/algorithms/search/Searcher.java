package algorithms.search;

/**
 * This interface is used define the Searcher object behavior.
 * A Searcher is an algorithm that can solve "Searchable" problem
 */
public interface Searcher<T> {
	/**
	 * Solving the problem with proper algorithm
	 * @param s Searchable<T> type 
	 * @param T Type of the problem(Searchable) 
	 * @return Solution<T> to the problem 
	 */
    public Solution<T> search(Searchable s);
    
	/**
	 * get how many nodes were evaluated by the algorithm
	 * @return int number of nodes
	 */
    public int getNumberOfNodesEvaluated();
}
