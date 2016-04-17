
#include <algorithm>
#include <stack>
#include <iostream>

using namespace std;


template <class T>
class AvlTree {
	
	template <class U>
	friend class TreeGenerator;
protected:
	static const bool debug = true;
	bool hasBeenModified;
	T* key;
	int height;
	AvlTree *left;
	AvlTree *right;
	AvlTree *father;
	long totalNumberOfElements;
	bool visitedLeft;
	bool visitedRight;

public:
	AvlTree() {
		height = 0;
		totalNumberOfElements = 0;
		hasBeenModified = false;
		visitedLeft = false;
		visitedRight = false;
		father = NULL;
		key = NULL;
		left = NULL;
		right = NULL;
	}

	bool Insert(T* value) {

		if (key == NULL) {
			key = value; // NEVER PASS A REFERENCE (&value). Use pointers. Modifying the object but not the reference did modify also key values on the tree. I want a different pointer for each object.
			totalNumberOfElements++;
			hasBeenModified = true;
			updateHeight();

			return true;
		}
		else if ((*key) > *value) {
			if (left == NULL)
			{
				left = new AvlTree();
				left->father = this;
			}

			bool res;
			if ((res = left->Insert(value))) {
				hasBeenModified = true;
				totalNumberOfElements++;
			}
			if (GetHeight(left) - GetHeight(right) == 2) {
				// Needs rebalancing
				if (*value > (*(left->key)))
					left->AnticlockwiseRotation();

				ClockwiseRotation();
			}
			updateHeight();
			return res;
		}
		else if ((*key) < *value) {
			if (right == NULL)
			{
				right = new AvlTree();
				right->father = this;
			}

			bool res;
			if ((res = right->Insert(value))) {
				hasBeenModified = true;
				totalNumberOfElements++;
			}
			if (GetHeight(right) - GetHeight(left) == 2) {
				// Needs rebalancing
				if (*value < (*(right->key)))
					right->ClockwiseRotation();

				AnticlockwiseRotation();
			}
			updateHeight();
			return res;
		}
		return false; // If the key is already in the tree
	}

	AvlTree<T>* Search(T* value) {
		if (key == NULL) return NULL;

		if (*key == *value) return this;
		else if (*key > *value && left != NULL)
			return left->Search(value);
		else if (*key < *value && right != NULL)
			return right->Search(value);
		return NULL; // if one of the child is NULL can't go in there, return NULL
	}

	bool Contains(T* value) {
		return ((Search(*value)) != NULL);
	}

	bool Delete(T* value) {
		AvlTree<T>* nodeToDelete = Search(value);
		if (nodeToDelete == NULL) return false;

		hasBeenModified = true;
		totalNumberOfElements--;

		// Base case: Leaf. I'm here if i'm a leaf or there has been a recursive call
		if (nodeToDelete->left == NULL && nodeToDelete->right == NULL)
		{
			if (*(nodeToDelete->father->key) > *(nodeToDelete->key)) // nodeToDelete is left child
				nodeToDelete->father->left = NULL;
			else if (*(nodeToDelete->father->key) < *(nodeToDelete->key)) // nodeToDelete is right child
				nodeToDelete->father->right = NULL;

			// I must update the height of the father, and propagate the new height towards the root
			// Moreover whenever a node discovers the need of rebalancing, it must be done
			if (nodeToDelete->father != NULL)
				nodeToDelete->father->PropagateAndRebalance();
		}

		// If left branch is NULL, substitute this node with the right branch
		if (nodeToDelete->left == NULL)
		{ // => right not NULL
			if (*(nodeToDelete->father->key) > *(nodeToDelete->key)) // nodeToDelete is left child
				nodeToDelete->father->left = nodeToDelete->right;
			else if (*(nodeToDelete->father->key) < *(nodeToDelete->key)) // nodeToDelete is right child
				nodeToDelete->father->right = nodeToDelete->right;

			// update heights and eventually rebalance from node to root
			if (nodeToDelete->father != NULL)
				nodeToDelete->father->PropagateAndRebalance();

			// If right branch is NULL, substitute this node with the left branch
		}
		else if (nodeToDelete->right == NULL)
		{ // => left not NULL
			if (*(nodeToDelete->father->key) > *(nodeToDelete->key)) // nodeToDelete is left child
				nodeToDelete->father->left = nodeToDelete->left;
			else if (*(nodeToDelete->father->key) < *(nodeToDelete->key)) // nodeToDelete is right child
				nodeToDelete->father->right = nodeToDelete->left;

			// update heights and eventually rebalance from node to root 
			if (nodeToDelete->father != NULL)
				nodeToDelete->father->PropagateAndRebalance();

		}
		else { // Internal node with both children
			AvlTree<T>* minDxSubtree = nodeToDelete->MinimumOfRightSubtree();

			// HOW TO DO IT WITH TAIL RECURSION? Without swapping first
			nodeToDelete->right->Delete(minDxSubtree->key); // Push deletion towards the leaves!

			nodeToDelete->key = minDxSubtree->key; // if this swap is done before, in some situations (right child is a leaf)
												   // the test of which branch i am in fails because the value of the father is inconsistent     
		}
		return true;
	}

	static int GetHeight(AvlTree<T>* node) {
		if (node == NULL) return -1;
		else return node->height;
	}

	TreeGenerator<T>* GetGenerator() {
		return new TreeGenerator<T>(this);
	}

	T* GetNodeValue() { return key; }

private:
	void updateHeight() {
		height = max(GetHeight(left), GetHeight(right)) + 1;
	}

	// Add drawings
	void ClockwiseRotation() {
		AvlTree<T>* z = this;
		
		AvlTree<T>* v = z->left;
		T* zKey = z->key;
		// First step: swap keys
		z->key = v->key;
		v->key = zKey;
		// Warning: recall that z is the pointer to the data structure. I change the value with v.key
		AvlTree<T>* alpha = v->left;
		AvlTree<T>* beta = v->right;
		AvlTree<T>* gamma = z->right;
		z->left = alpha;
		z->right = v; // Crucial! z here is the pointer to the new value v.key, while v has value z.key
		v->left = beta;
		v->right = gamma;

		if (beta != NULL) {
			beta->father = v; // v is the node with new value z.key
		}
		if (gamma != NULL) {
			gamma->father = v; // v is the node with new value z.key
		}
		if (alpha != NULL) {
			alpha->father = z; // v is the node with new value z.key
		}
		// The parents of z and v does not need to change, i only swapped the values

		z->updateHeight();
		v->updateHeight();
	}

	// Add drawings
	void AnticlockwiseRotation() {
		AvlTree<T>* v = this;
		
		AvlTree<T>* z = v->right;
		T* vKey = v->key;
		// First step: swap keys
		v->key = z->key;
		z->key = vKey;
		// Warning: recall that z is the pointer to the data structure. I change the value with v.key
		AvlTree<T>* alpha = v->left;
		AvlTree<T>* beta = z->left;
		AvlTree<T>* gamma = z->right;
		v->right = gamma;
		v->left = z; // Crucial! v here is the pointer to the new value z.key, while z has value v.key
		z->left = alpha;
		z->right = beta;

		if (beta != NULL) {
			beta->father = v; // v is the node with new value z.key
		}
		if (gamma != NULL) {
			gamma->father = v; // v is the node with new value z.key
		}
		if (alpha != NULL) {
			alpha->father = z; // v is the node with new value z.key
		}
		// The parents of z and v does not need to change, i only swapped the values

		z->updateHeight();
		v->updateHeight();
	}

	// TODO correctness needs to be proven
	void PropagateAndRebalance()
	{
		
		this->height = max(GetHeight(this->left), GetHeight(this->right));


		if (GetHeight(left) - GetHeight(right) == 2)
		{ // needs rebalancing

			ClockwiseRotation();

		}
		else if (GetHeight(right) - GetHeight(left) == 2)
		{ // needs rebalancing

			AnticlockwiseRotation();

		}

		updateHeight();

		if (father != NULL)
			father->PropagateAndRebalance(); // Propagate eventual rebalance towards the root!
	}

	AvlTree<T>* MinimumOfRightSubtree() {
		AvlTree<T>* temp = right;
		while (temp->left != NULL) temp = temp->left;
		
		return temp;
	}
};


template <class T>
class TreeGenerator {


private:
	stack<AvlTree<T>*> nodesStack;
public:
	TreeGenerator(AvlTree<T>* root) { // Pointer necessary

		nodesStack.push(root);
		if (!(root->left == NULL))
			root->visitedLeft = false;
		if (!(root->right == NULL))
			root->visitedRight = false;
	}

	bool hasNext() { return !nodesStack.empty(); }

	AvlTree<T>* visitNext() {
		/* Cases:
		0) Empty stack -> null
		1) Leaf -> return leaf
		2) Both subtrees visited --> return myself
		3) Left subtree not visited --> visit it
		4) Left subtree visited but right subtree no --> return myself and visit right subtree
		*/

		// Base case
		if (nodesStack.empty()) {
			return NULL;
		}

		//if (hasBeenModified) throw new ModifiedDuringGenerationException("Data Structure cannot be modified until you consume the generator");

		// Get the element on top of the stack
		AvlTree<T>* currentElement = nodesStack.top();
		nodesStack.pop();

		// Base case: The leaf returns itself, if not deleted
		if (currentElement->left == NULL && currentElement->right == NULL) {
			// Restore and return element if it is not deleted
			currentElement->visitedLeft = false;
			currentElement->visitedRight = false;
			return currentElement;
		}

		// Case 1: All my subtrees have been visited
		if (currentElement->visitedLeft && currentElement->visitedRight) {
			// Restore and return element if it is not deleted
			currentElement->visitedLeft = false;
			currentElement->visitedRight = false;
			return currentElement;
		}

		// If I have not visited left subtree, explore it
		if (!currentElement->visitedLeft) {
			if (currentElement->left == NULL) {
				currentElement->visitedLeft = true; // no need to explore
				nodesStack.push(currentElement);
				return visitNext();
			}
			else {
				nodesStack.push(currentElement); // Evaluated after left subtree
				nodesStack.push(currentElement->left); // Evaluated First
				currentElement->visitedLeft = true; // I don't want to re-add the same subtree
				return visitNext(); // Pick the first element == currentElement.left
			}
		}

		// The left subtree has been explored. Now explore the right one
		// Be careful: now I have to return the current value first
		if (!currentElement->visitedRight) {
			if (currentElement->right == NULL) {
				currentElement->visitedRight = true; // no need to explore
				nodesStack.push(currentElement);
				return visitNext();
			}
			else {
				nodesStack.push(currentElement->right);
				nodesStack.push(currentElement); // This way both subtrees of currentElement have been inserted and I can return currentElement
				currentElement->visitedRight = true; // I don't want to re-add the same subtree
				return visitNext();
			}
		}
		return NULL;
	}
};