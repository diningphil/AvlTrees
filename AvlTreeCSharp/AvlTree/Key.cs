using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AvlTree
{
    public class Key<T> where T : IComparable<T>
    {
        public T key;

        public Key(T v){
            key = v;
        }

    }
}
