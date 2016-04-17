package AvlTrees;

public interface TreeGenerator<T extends Comparable<T>> {
	public AvlTree<T> visitNext() throws ModifiedDuringGenerationException;
}
