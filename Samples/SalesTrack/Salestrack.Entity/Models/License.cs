using Salestrack.Common.Core;
using Salestrack.Common.Enum;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;


using System;
using System.Collections.Generic;

namespace Salestrack.Entity
{
    public class License : BaseEntity
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public long LicenseId { get; set; }

        public LicenseType LicenseTypeId { get; set; }

        public long MaxAdmins { get; set; }

        public long MaxManagers { get; set; }

        public long MaxSalesWorkers { get; set; }

    }
}
