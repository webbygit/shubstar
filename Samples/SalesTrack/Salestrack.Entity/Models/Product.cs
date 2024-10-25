using Salestrack.Common;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Salestrack.Common.Core;
using System.ComponentModel.DataAnnotations.Schema;
using System.ComponentModel.DataAnnotations;

namespace Salestrack.Entity
{
    public class Product : BaseEntity
    {
        public Product()
        {

        }

        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public long ProductId { get; set; }

        public string Code { get; set; }
        public string Name { get; set; }
        public string Title { get; set; }
        public string UPC { get; set; }
        public string SKU { get; set; }

        public decimal UnitPrice { get; set; }

        public long ProductCategoryId { get; set; }
        public ProductCategory ProductCategory { get; set; }
    }
}
