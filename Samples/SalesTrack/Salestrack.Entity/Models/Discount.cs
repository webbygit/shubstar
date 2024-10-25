using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Salestrack.Common.Core;
using System;

namespace Salestrack.Entity
{
    public enum DiscountType { Percentage, Amount }
    public enum DiscountScope { Order, OrderItem }

    public class Discount
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public long DiscountId { get; set; }
        public DiscountType DiscountType { get; set; }
        public DiscountScope DiscountScope { get; set; }

        public float MinimumPurchase { get; set; }
        public float MaximumDiscountAmount { get; set; }

        public DateTime BeginDate { get; set; }
        public DateTime ExpiryDate { get; set; }
    }
}
