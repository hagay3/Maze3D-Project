package algorithms.search;

/**
 * This interface is used define the Searcher object behavior.
 * A Searcher is an algorithm that can solve "Searchable" problem
 */
public interface Searcher<T> {
    // the search method
    public Solution<T> search(Searchable s);
    
    // get how many nodes were evaluated by the algorithm
    public int getNumberOfNodesEvaluated();
}
