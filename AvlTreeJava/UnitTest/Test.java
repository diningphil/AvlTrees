package UnitTest;
import AvlTrees.AvlTree;
import AvlTrees.ModifiedDuringGenerationException;
import AvlTrees.TreeGenerator;

public class Test {
	public static void insertionTest(AvlTree<Integer> node) throws InterruptedException{
		
		for(int i = 10; i >= 0; i--){	
			System.out.println("Ready to insert " + i);
			System.out.flush();
			node.insert(i);
			Thread.sleep(100);
		}
	}
	
	public static void deletionTest(AvlTree<Integer> node) throws Exception{
			
		
		//Test 1: remove root and place 8 instead
		node.delete(7);
		
		//Test 2: remove 10 causing clockwise rotation on 8
		node.delete(10);
		
		//Test 3: remove 1, that now is internal node with both children
		node.delete(1);
        
        if(node.needsBalancing()) throw new Exception("NEEDS REBALANCING");
    }
	
	public static void searchTest(AvlTree<Integer> node) throws InterruptedException{
		
		for(int i = 1; i < 12; i++){	
			System.out.println("Ready to search " + i);
			System.out.flush();
			if((node.search(i)) != null) System.out.println("Found!");
			else System.out.println("Not Found...");
			Thread.sleep(100);
		}
	}

	public static void generatorTest(AvlTree<Integer> tree) throws InterruptedException, ModifiedDuringGenerationException {
		TreeGenerator<Integer> gen = tree.generator();
		System.err.println("Test double generator");
		System.out.println("Ordered values: " + tree.toString());
		AvlTree<Integer> generated;
		while ((generated = gen.visitNext()) != null){
			Thread.sleep(100);
			System.out.println(generated.getNodeValue().toString());
		}
		gen = tree.generator(); // Reistantiate the generator in O(1)
		System.out.println("Ordered values: " + tree.toString());
		
		while ((generated = gen.visitNext()) != null){
			Thread.sleep(100);
			System.out.println(generated.getNodeValue().toString());
			generated.printAncestors();
		}
		System.err.println("End of generator test");
		
	}
	
}
