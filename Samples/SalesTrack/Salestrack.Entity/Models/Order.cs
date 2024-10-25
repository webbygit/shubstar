using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Salestrack.Common.Core;
using System;
using Salestrack.Common.Enum;
using System.Collections.Generic;

namespace Salestrack.Entity
{
    public class Order : BaseEntity
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public long OrderId { get; set; }

        public long UserId { get; set; }

        public long ClientId { get; set; }
        
        public DateTime OrderDate { get; set; }

        public OrderStatus OrderStatus { get; set; }

        public virtual ICollection<Discount> Discounts { get; set; }

        public virtual ICollection<OrderDetail> OrderDetails { get; set; }
    }

}
