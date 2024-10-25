using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Salestrack.Common.Core;

namespace Salestrack.Entity
{
    public class StatementItem : BaseEntity
    {
        public StatementItem()
        {

        }

        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public long StatementItemId { get; set; }
        public string ItemDescription { get; set; }
        public float Quantity { get; set; }
        public float UnitRate { get; set; }

        [NotMapped]
        public float Rate { get { return Quantity* UnitRate; } }
        
        // Foreign Key
        public long StatementId { get; set; }
        public Statement Statement { get; set; }
    }
}