using SalesTracker.Common.Enum;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SalesTracker.Model
{
    public class License : BaseProperties
    {
        public LicenseType LicenseTypeId { get; set; }

        public long MaxAdmins { get; set; }

        public long MaxManagers { get; set; }

        public long MaxSalesWorkers { get; set; }
    }
}
