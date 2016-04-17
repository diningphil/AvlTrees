using System;
using System.Collections.Generic;
using System.Threading;
using AvlTree;

public static class Test {

	public static void InsertionTest(AvlTree<int> node) {
		
		for(int i = 10; i >= 0; i--){	
			Console.WriteLine("Ready to insert " + i);
			node.Insert(i);
			Thread.Sleep(100);
		}
	}
	
	public static void DeletionTest(AvlTree<int> node){
			
		
		//Test 1: remove root and place 8 instead
		node.Delete(7);
		
		//Test 2: remove 10 causing clockwise rotation on 8
		node.Delete(10);
		
		//Test 3: remove 1, that now is internal node with both children
		node.Delete(1);
        
        if(node.NeedsBalancing()) throw new Exception("NEEDS REBALANCING");
    }
	
	public static void searchTest(AvlTree<int> node){
		
		for(int i = 1; i < 12; i++){	
			Console.WriteLine("Ready to search " + i);
			if((node.Search(i)) != null) Console.WriteLine("Found!");
			else Console.WriteLine("Not Found...");
			Thread.Sleep(100);
		}
	}

	public static void GeneratorTest(AvlTree<int> tree) {
        
		IEnumerator<AvlTree<int>> gen = tree.GetEnumerator();
      
		Console.WriteLine("Test double generator");
        // Explicit use of a generator
        while (gen.MoveNext()){
			Thread.Sleep(100);
            Console.WriteLine(gen.Current.GetNodeValue());
		}
        
        // Example of foreach construct using IEnumerable<int> tree
		foreach(AvlTree<int> node in tree){
			Thread.Sleep(100);
			Console.WriteLine(node.GetNodeValue());
			node.PrintAncestors();
		}
		Console.WriteLine("End of generator test");  	
	}
	
}
