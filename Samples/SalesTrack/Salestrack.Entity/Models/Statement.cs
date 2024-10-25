using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Salestrack.Common.Core;

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Salestrack.Entity
{
    public class Statement : BaseEntity
    {
        public Statement()
        {

        }

        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public long StatementId { get; set; }

        public DateTime StatementDate { get; set; }
        public DateTime StatementMonth { get; set; }
        public string StatementNote { get; set; }

        public float BillAmount { get; set; }
        public ICollection<StatementItem> StatementItems { get;set;}

        public float BalanceAmount { get; set; }
        public ICollection<Payment> Payments { get; set; }
    }
}
