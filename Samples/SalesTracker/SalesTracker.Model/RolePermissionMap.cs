using SalesTracker.Common.Enum;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SalesTracker.Model
{
    public class RolePermissionMap
    {
        public long ID { get; set; }

        public long ComapnyId { get; set; }

        public Role  RoleId { get; set; }

        public Permission PermissionId { get; set; }
    }
}
