using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AvlTree
{
    interface TreeOperations<T> where T : IComparable<T> 
    {
        // Pre: non null argument
        bool Insert(T value);
        // Post: false if value is already in the tree (not inserted), true otherwise

        // Pre: non null argument
        AvlTree<T> Search(T value);
        // Post: returns the searched node, null if it doesn't exist

        // Pre: non null argument
        bool Contains(T value);
        // Post: returns true if the element exists, false otherwise

        // Pre:
        bool Delete(T value);
        // Post: Returns the deleted element, null if it doesn't exist

        // Pre
        IEnumerator<AvlTree<T>> GetElements();
        // Returns a ONE TIME CONSUMING generator of the element in ascending order
    }
}
