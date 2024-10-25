using SalesTracker.Common.Enum;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SalesTracker.Model
{
    public class Company : BaseProperties
    {
        public string DomainName { get; set; }

        public Users CompanyAdmin { get; set; }
    }

    public class CompanyLicense : BaseProperties
    {
        public long CompanyId { get; set; }

        public long LicenseId { get; set; }

        public DateTime StartDate { get; set; }

        public DateTime? EndDate { get; set; }
    }

    public class CompanyPermission : BaseProperties
    {
        public long ComapnyId { get; set; }

        public Permission PermissionId { get; set; }
    }

    public class CompanyRole : BaseProperties
    {
        public long CompanyId { get; set; }

        public Role RoleId { get; set; }
    }
}
