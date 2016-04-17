package AvlTrees;
import AvlTrees.TreeGenerator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

public class AvlTree<T extends Comparable<T>> implements TreeOperations<T>{
    protected static final boolean debug = false;
    
    protected boolean hasBeenModified;
    
    protected T key;
    protected long height;
    protected AvlTree<T> left, right;
    protected AvlTree<T> father;
    protected boolean visitedLeft;
    protected boolean visitedRight;
    
    protected long totalNumberOfElements;
    
    private TreeGen generator;
    
    
    @SuppressWarnings("rawtypes")
    public static long getHeight(AvlTree node){
        if (node == null) return -1;
        else return node.height;
    }
    
    public AvlTree(){
        height = 0;
        totalNumberOfElements = 0;
        hasBeenModified = false;
        visitedLeft = false;
        visitedRight = false;
        father = null;
        key = null;
        left=null;
        right=null;
        generator=null;
    }
    public T getNodeValue(){ return key; }
    public AvlTree<T> getLeftChild(){ return left; }
    public AvlTree<T> getRightChild(){ return right; }
    public long getLength(){ return totalNumberOfElements; }
    
    public boolean insert(T value){
        if (debug) System.err.println("Inserting " + value);
        if(key == null){
            key = value;
            totalNumberOfElements++;
            hasBeenModified=true;
            // update height
            height = Math.max(getHeight(left), getHeight(right)) + 1;
            return true;
        }else if(key.compareTo(value) == 1){ // key > value
            if(left == null){
                left = new AvlTree<>();
                left.father = this;
            }
            boolean res;
            if(res = left.insert(value)){
                hasBeenModified=true;
                totalNumberOfElements++;
            }
            
            if(getHeight(left) - getHeight(right) == 2){ // needs rebalancing
                if(value.compareTo(left.key) == 1) // if value > left.key
                    left.anticlockwiseRotation();
                
                clockwiseRotation();
            }
            // update height
            height = Math.max(getHeight(left), getHeight(right)) + 1;
            return res;
            // completely symmetric
        }else if(key.compareTo(value) == -1){ // key < value
            if(right == null){
                right = new AvlTree<>();
                right.father = this;
            }
            boolean res;
            if(res = right.insert(value)){
                hasBeenModified=true;
                totalNumberOfElements++;
            }
            
            if(getHeight(right) - getHeight(left) == 2){ // needs rebalancing
                if(value.compareTo(right.key) == -1) // if value < right.key
                    right.clockwiseRotation();
                
                anticlockwiseRotation();
            }
            // update height
            height = Math.max(getHeight(left), getHeight(right)) + 1;
            return res;
        }
        return false; // If the key is already in the tree
    }
    
    // TODO Add Drawing in comments AND solve warnings
    private void clockwiseRotation() {
        AvlTree<T> z = this;
        if (debug) System.err.println("Clockwise rotation on " + z.key);
        AvlTree<T> v = z.left;
        // TODO fare in modo che l'utente non si accorga che la radice può essere cambiata!
        T zKey = z.key;
        // Primo passo: invertire le chiavi
        z.key = v.key;
        v.key = zKey;
        // Warning: Recall that z is the pointer to the data structure. I change the value with v.key
        AvlTree<T> alpha = v.left;
        AvlTree<T> beta = v.right;
        AvlTree<T> gamma = z.right;
        z.left = alpha;
        z.right = v; // Crucial! z here is the pointer with new value v.key, while v has value z.key
        v.left = beta;
        v.right = gamma;
        
        if(beta != null)
        	beta.father = v; // v is the node with new value z.key
        if(gamma != null)
        	gamma.father = v;
        if(alpha != null)
        	alpha.father = z;
        // the parents of v and z don't change, since I only swapped their values and moved one from one branch to another
        
        z.height = Math.max(getHeight(z.left), getHeight(z.right)) + 1;
        v.height = Math.max(getHeight(v.left), getHeight(v.right)) + 1;
    }
    
    // TODO Add Drawing in comments AND solve warnings
    private void anticlockwiseRotation() {
        AvlTree<T> v = this;
        if (debug) System.err.println("Anticlockwise rotation on " + v.key);
        AvlTree<T> z = v.right;
        // TODO fare in modo che l'utente non si accorga che la radice può essere cambiata!
        T vKey = v.key;
        // Step 1: swap keys
        v.key = z.key;
        z.key = vKey;
        // Warning: Recall that v is the pointer to the data structure. I change the value with z.key
        AvlTree<T> alpha = v.left;
        AvlTree<T> beta = z.left;
        AvlTree<T> gamma = z.right;
        v.right = gamma;
        v.left = z; // Crucial! v here is the pointer with new value z.key, while z has value v.key
        z.left = alpha;
        z.right = beta;
        if(beta != null)
        	beta.father = z; // z is the node with new value v.key
        if(gamma != null)
        	gamma.father = z;
        if(alpha != null)
        	alpha.father = v;
        // the parents of v and z don't change, since I only swapped their values and moved one from one branch to another
        
        z.height = Math.max(getHeight(z.left), getHeight(z.right)) + 1;
        v.height = Math.max(getHeight(v.left), getHeight(v.right)) + 1;
    }
    
    public AvlTree<T> search(T value){
        if (key == null) return null;
        
        if (key.compareTo(value) == 0) return this; // key == value
        else if(key.compareTo(value) == 1 && left != null) // key > value
            return left.search(value);
        else if(key.compareTo(value) == -1 && right != null) // key < value
            return right.search(value);
        return null; // if one of the child is null can't go in there, return null
    }
    
    public boolean delete(T value){
    	AvlTree<T> nodeToDelete = search(value);
        if(nodeToDelete == null) return false;
        
        hasBeenModified=true;
        totalNumberOfElements--;
        
        // Base case: Leaf. I'm here if i'm a leaf or there has been a recursive call
        if(nodeToDelete.left == null && nodeToDelete.right == null){
            if(nodeToDelete.father.key.compareTo(nodeToDelete.key) > 0) // nodeToDelete is left child
                nodeToDelete.father.left = null;
            else if(nodeToDelete.father.key.compareTo(nodeToDelete.key) < 0) // nodeToDelete is right child
                nodeToDelete.father.right = null;
            
            // I must update the height of the father, and propagate the new height towards the root
            // Moreover whenever a node discovers the need of rebalancing, it must be done
            if(nodeToDelete.father != null)
            	nodeToDelete.father.propagateAndRebalance();
        }
        
        // If left branch is null, substitute this node with the right branch
        if(nodeToDelete.left == null){ // => right not null
            if(nodeToDelete.father.key.compareTo(nodeToDelete.key) > 0) // nodeToDelete is left child
            	nodeToDelete.father.left = nodeToDelete.right;
            else if(nodeToDelete.father.key.compareTo(nodeToDelete.key) < 0) // nodeToDelete is right child
            	nodeToDelete.father.right = nodeToDelete.right;
            
            // update heights and eventually rebalance from node to root
            if(nodeToDelete.father != null)
            	nodeToDelete.father.propagateAndRebalance();
            
        // If right branch is null, substitute this node with the left branch
        }else if(nodeToDelete.right == null){ // => left not null
            if(nodeToDelete.father.key.compareTo(nodeToDelete.key) > 0) // nodeToDelete is left child
            	nodeToDelete.father.left = nodeToDelete.left;
            else if(nodeToDelete.father.key.compareTo(nodeToDelete.key) < 0) // nodeToDelete is right child
            	nodeToDelete.father.right = nodeToDelete.left;
            
            // update heights and eventually rebalance from node to root 
            if(nodeToDelete.father != null)
            	nodeToDelete.father.propagateAndRebalance();
            
        }else{ // Internal node with both children
            AvlTree<T> minDxSubtree = nodeToDelete.minimumOfRightSubtree();
            
            // HOW TO DO IT WITH TAIL RECURSION? Without swapping first
            nodeToDelete.right.delete(minDxSubtree.key); // Push deletion towards the leaves!
            
            nodeToDelete.key = minDxSubtree.key; // if this swap is done before, in some situations (right child is a leaf)
            // the test of which branch i am in fails because the value of the father is inconsistent     
        }
        
        return true;
    }
    
    public boolean needsBalancing (){
        if (Math.abs(getHeight(left) - getHeight(right)) < 2){
        	if(left != null && right != null)
        		return left.needsBalancing() || right.needsBalancing();
        	
        	else if (left == null && right == null) return false;
        	
        	else if (left == null)
        		return right.needsBalancing();
        	else if (right == null)
        		return left.needsBalancing();
        }
        
        return true;
    }
    
    public void printAncestors(){
    	if (father != null){
    		System.out.println("Node " + key + " my father is " + father.key );
    		father.printAncestors();
    	}
    }
    
    // TODO correctness needs to be proven
    private void propagateAndRebalance(){
    	if(debug) System.err.println("Node " + key + "left height =  " + getHeight(this.left) + " right height = " + getHeight(this.right));
        this.height = Math.max(getHeight(this.left), getHeight(this.right));
        
        
        if(getHeight(left) - getHeight(right) == 2){ // needs rebalancing
        
            clockwiseRotation();
            
        }else if(getHeight(right) - getHeight(left) == 2){ // needs rebalancing
            
            anticlockwiseRotation();
    
        }
        // update height
        height = Math.max(getHeight(left), getHeight(right)) + 1;
        
        if(father != null)
        	father.propagateAndRebalance(); // Propagate eventual rebalance towards the root!
    }
                               
    private AvlTree<T> minimumOfRightSubtree(){
        AvlTree<T> temp = right;
        while(temp.left != null) temp = temp.left;
        if(debug) System.err.println("The minimum is " + temp.key);
        return temp;
    }
    
    public boolean contains(T value){
        return ((search(value)) != null);
    }
    
    public TreeGenerator<T> generator(){
        if(generator == null) generator = new TreeGen();
        else{
            hasBeenModified = false;
            generator.nodesStack.push(this);
        }
        return generator;
    }
    
    protected class TreeGen implements TreeGenerator<T>, Iterator<AvlTree<T>>{
        
        // Ho bisogno di uno stack dei padri da consultare
        public Stack<AvlTree<T>> nodesStack;
        
        public TreeGen(){
            hasBeenModified = false; // From now on data structure cannot be modified until the generator has been consumed
            nodesStack = new Stack<>();
            nodesStack.push(AvlTree.this);
            if(AvlTree.this.left != null)
                AvlTree.this.visitedLeft = false;
            if(AvlTree.this.right != null)
                AvlTree.this.visitedRight = false;
        }
        
        @Override
        public AvlTree<T> visitNext() throws ModifiedDuringGenerationException{
            
            /* Cases:
             0) Empty stack -> null
             1) Leaf -> return leaf
             2) Both subtrees visited --> return myself
             3) Left subtree not visited --> visit it
             4) Left subtree visited but right subtree no --> return myself and visit right subtree
             */
            
            // Base case
            if(nodesStack.isEmpty()){
                return null;
            }
            
            if (hasBeenModified) throw new ModifiedDuringGenerationException("Data Structure cannot be modified until you consume the generator");
            
            // Get the element on top of the stack
            AvlTree<T> currentElement = nodesStack.pop();
            
            // Base case: The leaf returns itself, if not deleted
            if(currentElement.left == null && currentElement.right == null){
                // Restore and return element if it is not deleted
                currentElement.visitedLeft = false;
                currentElement.visitedRight = false;
                return currentElement;
            }
            
            // Case 1: All my subtrees have been visited
            if(currentElement.visitedLeft && currentElement.visitedRight){
                // Restore and return element if it is not deleted
                currentElement.visitedLeft = false;
                currentElement.visitedRight = false;
                return currentElement;
            }
            
            // If I have not visited left subtree, explore it
            if(!currentElement.visitedLeft){
                if(currentElement.left == null){
                    currentElement.visitedLeft = true; // no need to explore
                    nodesStack.push(currentElement);
                    return visitNext();
                }
                else{
                    nodesStack.push(currentElement); // Evaluated after left subtree 
                    nodesStack.push(currentElement.left); // Evaluated First
                    currentElement.visitedLeft = true; // I don't want to re-add the same subtree
                    return visitNext(); // Pick the first element == currentElement.left
                }
            }
            
            // The left subtree has been explored. Now explore the right one
            // Be careful: now I have to return the current value first
            if(!currentElement.visitedRight){
                if(currentElement.right == null){
                    currentElement.visitedRight = true; // no need to explore
                    nodesStack.push(currentElement);
                    return visitNext();
                }
                else{
                    nodesStack.push(currentElement.right);
                    nodesStack.push(currentElement); // This way both subtrees of currentElement have been inserted and I can return currentElement
                    currentElement.visitedRight = true; // I don't want to re-add the same subtree
                    return visitNext();
                }
            }
            return null;	
        }
        
        @Override
        public boolean hasNext() {
            return !nodesStack.isEmpty();
        }
        
        @Override
        public AvlTree<T> next() {
            AvlTree<T> res;
            try {
                if ((res = visitNext()) == null) throw new NoSuchElementException();
                else return res;
            } catch (ModifiedDuringGenerationException e) {
                if(true) throw new IllegalStateException("Cannot modify the data structure until the iterator is consumed"); 
            }
            return null;	
        }
    }	
    
    @Override
    public Iterator<AvlTree<T>> iterator() {
        return new TreeGen();
    }
}