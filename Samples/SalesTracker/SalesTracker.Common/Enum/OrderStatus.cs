using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SalesTracker.Common.Enum
{
    public enum OrderStatus
    {
        None = 0,

        New = 1,

        Approved = 2,

        Disapproved = 3,

        Delivered = 4,

        PartiallyDelivered = 5
    }
}
