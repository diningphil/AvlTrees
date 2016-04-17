package AvlTrees;

public interface TreeOperations<T extends Comparable<T>> extends Iterable<AvlTree<T>> {
	
        // Pre: non null argument
		public boolean insert(T value);
		// Post: false if value is already in the tree (not inserted), true otherwise
		
        // Pre: non null argument
		public AvlTree<T> search(T value);
		// Post: returns the searched node, null if it doesn't exist
    
        // Pre: non null argument
        public boolean contains(T value);
        // Post: returns true if the element exists, false otherwise
		
        // Pre: non null argument
		public boolean delete(T value);
		// Post: Returns the deleted element, null if it doesn't exist
		
		// Pre: The data structure cannot be modified unless the generator returns null.
		// In other words, it must be consumed first.
		public TreeGenerator<T> generator();
		// Post: Returns a generator, or reinitialize it.
}
