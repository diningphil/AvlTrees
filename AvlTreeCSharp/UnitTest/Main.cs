using System;
using System.Threading;
using AvlTree;

public class TestClass {

    static void Main(string[] args) {

        AvlTree<int> tree = new AvlTree<int>();
		Test.InsertionTest(tree);
		Thread.Sleep(100);
		Console.WriteLine("Length of the tree: " + tree.GetLength());
		Thread.Sleep(100);
		Test.GeneratorTest(tree);
		Test.searchTest(tree);
		Test.DeletionTest(tree);
		Thread.Sleep(100);
        Console.WriteLine("Length of the tree: " + tree.GetLength());
		Thread.Sleep(100);
		Test.GeneratorTest(tree);
			
	}
}