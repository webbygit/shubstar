using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using SalesTracker.Common.Enum;

namespace SalesTracker.Model
{
    public class Order
    {
        public long ID { get; set; }

        public long UserId { get; set; }

        public long ClientId { get; set; }

        public DateTime OrderDate { get; set; }

        public OrderStatus OrderStatus { get; set; }
    }

    public class OrderDetails
    {
        public long OrderId { get; set; }

        public long ProductId { get; set; }

        public decimal Quantity { get; set; }

        public bool IsDelivered { get; set; }

        public DateTime? DeliveryDate { get; set; }
    }
}
