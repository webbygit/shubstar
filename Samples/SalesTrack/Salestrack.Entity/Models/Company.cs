using Salestrack.Common;
using Salestrack.Common.Core;
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
    public class Company : BaseEntity
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public long CompanyId { get; set; }

        [Url]
        public string DomainName { get; set; }

        // Company Must have 1 Admin User
        public ICollection<UserProfile>  Users { get; set; }
     }

}
