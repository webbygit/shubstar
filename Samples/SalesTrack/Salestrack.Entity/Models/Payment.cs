using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Salestrack.Common.Core;
using System;

namespace Salestrack.Entity
{
    public enum PaymentType { Cash, CreditCard, Check}

    public class Payment : BaseEntity
    {
        public Payment()
        {

        }

        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public long PaymentId { get; set; }
        public PaymentType PaymentType { get; set; }
        public float Amount { get; set; }
        public DateTime PaymentDate { get; set; }
        public DateTime PaymentRecievedDate { get; set; }
        public string Note { get; set; }
    }
}