using Salestrack.Common;
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
    public class ProductCategory : BaseEntity
    {
        public ProductCategory()
        {

        }

        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public long ProductCategoryId { get; set; }
        public string Name { get; set; }
    
        public long CompanyId { get; set; }
        public Company Comapny { get; set; }

        public ICollection<Product> Products { get; set; }
    }
}
