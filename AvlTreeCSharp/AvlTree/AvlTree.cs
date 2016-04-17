using AvlTree;
using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AvlTree
{
    public class AvlTree<T> : TreeOperations<T>, IEnumerable<AvlTree<T>> where T : IComparable<T> 
    {
        protected bool hasBeenModified;
        protected Key<T> key;
        protected long height;
        protected AvlTree<T> left, right;
        protected AvlTree<T> father;
        protected bool visitedLeft;
        protected bool visitedRight;
        protected long totalNumberOfElements;
      
        public AvlTree()
        {
            height = 0;
            totalNumberOfElements = 0;
            hasBeenModified = false;
            visitedLeft = false;
            visitedRight = false;
            father = null;
            key = null; // I had to add another class Key<T> to handle null values easily
            left = null;
            right = null;
        }

        // Example of a generator with yield construct
        public IEnumerator<AvlTree<T>> GetElements()
        {
            bool done = false;
            Stack<AvlTree<T>> stack = new Stack<AvlTree<T>>();
            AvlTree<T> current = this;

            while (!done)
            {
                if (current != null)
                {
                    stack.Push(current);
                    current = current.left;
                }
                else
                {
                    if (stack.Count > 0)
                    {
                        current = stack.Pop();
                        yield return current;
                        current = current.right;
                    }
                    else
                        done = true;
                }
            }
        }


        public static long GetHeight(AvlTree<T> node)
        {
            if (node == null) return -1;
            else return node.height;
        }

        public T GetNodeValue() { return key.key; }
        public AvlTree<T> GetLeftChild() { return left; }
        public AvlTree<T> GetRightChild() { return right; }
        public long GetLength() { return totalNumberOfElements; }

        public bool Insert(T value)
        {
#if DEBUG
            Console.WriteLine("Inserting " + value);
#endif
            if (key == null)
            {
                key = new Key<T>(value); 
                totalNumberOfElements++;
                hasBeenModified = true;
                // update height
                height = Math.Max(GetHeight(left), GetHeight(right)) + 1;
                return true;
            }
            else if (key.key.CompareTo(value) == 1)
            { // key > value
                if (left == null)
                {
                    left = new AvlTree<T>();
                    left.father = this;
                }
                bool res;
                if (res = left.Insert(value))
                {
                    hasBeenModified = true;
                    totalNumberOfElements++;
                }

                if (GetHeight(left) - GetHeight(right) == 2)
                { // needs rebalancing
                    if (value.CompareTo(left.key.key) == 1) // if value > left.key
                        left.AnticlockwiseRotation();

                    ClockwiseRotation();
                }
                // update height
                height = Math.Max(GetHeight(left), GetHeight(right)) + 1;
                return res;
                // completely symmetric
            }
            else if (key.key.CompareTo(value) == -1)
            { // key < value
                if (right == null)
                {
                    right = new AvlTree<T>();
                    right.father = this;
                }
                bool res;
                if (res = right.Insert(value))
                {
                    hasBeenModified = true;
                    totalNumberOfElements++;
                }

                if (GetHeight(right) - GetHeight(left) == 2)
                { // needs rebalancing
                    if (value.CompareTo(right.key.key) == -1) // if value < right.key
                        right.ClockwiseRotation();

                    AnticlockwiseRotation();
                }
                // update height
                height = Math.Max(GetHeight(left), GetHeight(right)) + 1;
                return res;
            }
            return false; // If the key is already in the tree
        }

        // TODO Add Drawing in comments AND solve warnings
        private void ClockwiseRotation()
        {
            AvlTree<T> z = this;
#if DEBUG
            Console.WriteLine("Clockwise rotation on " + z.key.key);
#endif
            AvlTree<T> v = z.left;
            // TODO fare in modo che l'utente non si accorga che la radice può essere cambiata!
            T zKey = z.key.key;
            // Primo passo: invertire le chiavi
            z.key.key = v.key.key;
            v.key.key = zKey;
            // Warning: Recall that z is the pointer to the data structure. I change the value with v.key
            AvlTree<T> alpha = v.left;
            AvlTree<T> beta = v.right;
            AvlTree<T> gamma = z.right;
            z.left = alpha;
            z.right = v; // Crucial! z here is the pointer with new value v.key, while v has value z.key
            v.left = beta;
            v.right = gamma;

            if (beta != null)
                beta.father = v; // v is the node with new value z.key
            if (gamma != null)
                gamma.father = v;
            if (alpha != null)
                alpha.father = z;
            // the parents of v and z don't change, since I only swapped their values and moved one from one branch to another

            z.height = Math.Max(GetHeight(z.left), GetHeight(z.right)) + 1;
            v.height = Math.Max(GetHeight(v.left), GetHeight(v.right)) + 1;
        }

        // TODO Add Drawing in comments AND solve warnings
        private void AnticlockwiseRotation()
        {
            AvlTree<T> v = this;
#if DEBUG
            Console.WriteLine("Anticlockwise rotation on " + v.key.key);
#endif
            AvlTree<T> z = v.right;
            T vKey = v.key.key;
            // Step 1: swap keys
            v.key.key = z.key.key;
            z.key.key = vKey;
            // Warning: Recall that v is the pointer to the data structure. I change the value with z.key
            AvlTree<T> alpha = v.left;
            AvlTree<T> beta = z.left;
            AvlTree<T> gamma = z.right;
            v.right = gamma;
            v.left = z; // Crucial! v here is the pointer with new value z.key, while z has value v.key
            z.left = alpha;
            z.right = beta;
            if (beta != null)
                beta.father = z; // z is the node with new value v.key
            if (gamma != null)
                gamma.father = z;
            if (alpha != null)
                alpha.father = v;
            // the parents of v and z don't change, since I only swapped their values and moved one from one branch to another

            z.height = Math.Max(GetHeight(z.left), GetHeight(z.right)) + 1;
            v.height = Math.Max(GetHeight(v.left), GetHeight(v.right)) + 1;
        }

        public AvlTree<T> Search(T value)
        {
            if (key == null) return null;

            if (key.key.CompareTo(value) == 0) return this; // key == value
            else if (key.key.CompareTo(value) == 1 && left != null) // key > value
                return left.Search(value);
            else if (key.key.CompareTo(value) == -1 && right != null) // key < value
                return right.Search(value);
            return null; // if one of the child is null can't go in there, return null
        }

        public bool Delete(T value)
        {
            AvlTree<T> nodeToDelete = Search(value);
            if (nodeToDelete == null) return false;

            hasBeenModified = true;
            totalNumberOfElements--;

            // Base case: Leaf. I'm here if i'm a leaf or there has been a recursive call
            if (nodeToDelete.left == null && nodeToDelete.right == null)
            {
                if (nodeToDelete.father.key.key.CompareTo(nodeToDelete.key.key) > 0) // nodeToDelete is left child
                    nodeToDelete.father.left = null;
                else if (nodeToDelete.father.key.key.CompareTo(nodeToDelete.key.key) < 0) // nodeToDelete is right child
                    nodeToDelete.father.right = null;

                // I must update the height of the father, and propagate the new height towards the root
                // Moreover whenever a node discovers the need of rebalancing, it must be done
                if (nodeToDelete.father != null)
                    nodeToDelete.father.PropagateAndRebalance();
            }

            // If left branch is null, substitute this node with the right branch
            if (nodeToDelete.left == null)
            { // => right not null
                if (nodeToDelete.father.key.key.CompareTo(nodeToDelete.key.key) > 0) // nodeToDelete is left child
                    nodeToDelete.father.left = nodeToDelete.right;
                else if (nodeToDelete.father.key.key.CompareTo(nodeToDelete.key.key) < 0) // nodeToDelete is right child
                    nodeToDelete.father.right = nodeToDelete.right;

                // update heights and eventually rebalance from node to root
                if (nodeToDelete.father != null)
                    nodeToDelete.father.PropagateAndRebalance();

                // If right branch is null, substitute this node with the left branch
            }
            else if (nodeToDelete.right == null)
            { // => left not null
                if (nodeToDelete.father.key.key.CompareTo(nodeToDelete.key.key) > 0) // nodeToDelete is left child
                    nodeToDelete.father.left = nodeToDelete.left;
                else if (nodeToDelete.father.key.key.CompareTo(nodeToDelete.key.key) < 0) // nodeToDelete is right child
                    nodeToDelete.father.right = nodeToDelete.left;

                // update heights and eventually rebalance from node to root 
                if (nodeToDelete.father != null)
                    nodeToDelete.father.PropagateAndRebalance();

            }
            else { // Internal node with both children
                AvlTree<T> minDxSubtree = nodeToDelete.MinimumOfRightSubtree();

                // HOW TO DO IT WITH TAIL RECURSION? Without swapping first
                nodeToDelete.right.Delete(minDxSubtree.key.key); // Push deletion towards the leaves!

                nodeToDelete.key = minDxSubtree.key; // if this swap is done before, in some situations (right child is a leaf)
                                                     // the test of which branch i am in fails because the value of the father is inconsistent     
            }
            return true;
        }

        public bool NeedsBalancing()
        {
            if (Math.Abs(GetHeight(left) - GetHeight(right)) < 2)
            {
                if (left != null && right != null)
                    return left.NeedsBalancing() || right.NeedsBalancing();

                else if (left == null && right == null) return false;

                else if (left == null)
                    return right.NeedsBalancing();
                else if (right == null)
                    return left.NeedsBalancing();
            }

            return true;
        }

        public void PrintAncestors()
        {
            if (father != null)
            {
                Console.WriteLine("Node " + key.key + " my father is " + father.key.key);
                father.PrintAncestors();
            }
        }

        // TODO correctness needs to be proven
        private void PropagateAndRebalance()
        {
#if DEBUG
            Console.WriteLine("Node " + key.key + "left height =  " + GetHeight(this.left) + " right height = " + GetHeight(this.right));
#endif
            this.height = Math.Max(GetHeight(this.left), GetHeight(this.right));


            if (GetHeight(left) - GetHeight(right) == 2)
            { // needs rebalancing

                ClockwiseRotation();

            }
            else if (GetHeight(right) - GetHeight(left) == 2)
            { // needs rebalancing

                AnticlockwiseRotation();

            }
            // update height
            height = Math.Max(GetHeight(left), GetHeight(right)) + 1;

            if (father != null)
                father.PropagateAndRebalance(); // Propagate eventual rebalance towards the root!
        }

        private AvlTree<T> MinimumOfRightSubtree()
        {
            AvlTree<T> temp = right;
            while (temp.left != null) temp = temp.left;
#if DEBUG
            Console.WriteLine("The minimum is " + temp.key.key);
#endif
            return temp;
        }

        public bool Contains(T value)
        {
            return ((Search(value)) != null);
        }

        public IEnumerator<AvlTree<T>> GetEnumerator()
        {
            return GetElements();
        }

        IEnumerator IEnumerable.GetEnumerator()
        {
            return GetEnumerator();
        }
    }
}
