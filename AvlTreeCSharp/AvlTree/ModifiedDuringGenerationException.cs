using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AvlTree
{
    [Serializable()]
    class ModifiedDuringGenerationException : System.Exception
    {
        public ModifiedDuringGenerationException(string message) : base(message) { }        
    }
}
