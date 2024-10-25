using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Salestrack.Common.Enum
{
    public enum Permission
    {
        None = 0,

        ReadOnly = 1,

        ManageManagers = 2,

        ManageSalesWorkers = 3,

        ApproveOrders = 4
    }
}
