using Salestrack.Common;
using Salestrack.Common.Enum;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Salestrack.Common.Core;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Salestrack.Entity
{

    public class CompanyLicense : BaseEntity
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public long CompanyLicenseId { get; set; }

        public long CompanyId { get; set; }
        public Company Company { get; set; }

        public long LicenseId { get; set; }
        public License License { get; set; }

        public DateTime StartDate { get; set; }
        public DateTime? EndDate { get; set; }
    }
}