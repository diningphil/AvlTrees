package UnitTest;
import AvlTrees.*;

public class Main {

	public static void main(String[] args) throws Exception {
		AvlTree<Integer> tree = new AvlTree<Integer>();
		Test.insertionTest(tree);
		Thread.sleep(100);
		System.out.println("Length of the tree: " + tree.getLength());
		Thread.sleep(100);
		Test.generatorTest(tree);
		Test.searchTest(tree);
		Test.deletionTest(tree);
		Thread.sleep(100);
		System.out.println("Length of the tree: " + tree.getLength());
		Thread.sleep(100);
		Test.generatorTest(tree);
			
	}
}
