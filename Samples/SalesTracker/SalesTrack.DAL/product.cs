//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated from a template.
//
//     Manual changes to this file may cause unexpected behavior in your application.
//     Manual changes to this file will be overwritten if the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

namespace SalesTrack.DAL
{
    using System;
    using System.Collections.Generic;
    
    public partial class product
    {
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2214:DoNotCallOverridableMethodsInConstructors")]
        public product()
        {
            this.deal_product_mapping = new HashSet<deal_product_mapping>();
            this.product_images = new HashSet<product_images>();
        }
    
        public long ProductID { get; set; }
        public string ProductTitle { get; set; }
        public string ProductName { get; set; }
        public Nullable<long> ProductCategoryID { get; set; }
        public string Description { get; set; }
        public string Keywords { get; set; }
        public string Width { get; set; }
        public string Height { get; set; }
        public string DimUnit { get; set; }
        public string Weight { get; set; }
        public string WeightUnit { get; set; }
        public string UPCCode { get; set; }
        public string ISBNNumber { get; set; }
        public Nullable<double> DefaultPrice { get; set; }
        public Nullable<bool> IsActive { get; set; }
        public Nullable<System.DateTime> CreatedOn { get; set; }
        public string CreatedBy { get; set; }
        public Nullable<System.DateTime> ModifiedOn { get; set; }
        public string ModifiedBy { get; set; }
        public Nullable<long> UserProfileID { get; set; }
    
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2227:CollectionPropertiesShouldBeReadOnly")]
        public virtual ICollection<deal_product_mapping> deal_product_mapping { get; set; }
        public virtual product_category product_category { get; set; }
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2227:CollectionPropertiesShouldBeReadOnly")]
        public virtual ICollection<product_images> product_images { get; set; }
        public virtual user_profile user_profile { get; set; }
    }
}
