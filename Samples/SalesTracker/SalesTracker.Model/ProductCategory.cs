using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SalesTracker.Model
{
    public class ProductCategory : BaseProperties
    {
        public long CompanyId { get; set; }
    }

    public class Product : BaseProperties
    {
        public long ProductCategoryId { get; set; }

        public decimal UnitPrice { get; set; }
    }
}
