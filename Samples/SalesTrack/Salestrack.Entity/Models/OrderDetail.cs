using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Salestrack.Common.Core;
using System;



namespace Salestrack.Entity
{

    public class OrderDetail : BaseEntity
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public long OrderDetailId { get; set; }

        public long ProductId { get; set; }

        public decimal Quantity { get; set; }

        public bool IsDelivered { get; set; }

        public DateTime? DeliveryDate { get; set; }

        public long OrderId { get; set; }
        public Order Order { get; set; }
    }

}