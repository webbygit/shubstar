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
    
    public partial class deal
    {
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2214:DoNotCallOverridableMethodsInConstructors")]
        public deal()
        {
            this.deal_product_mapping = new HashSet<deal_product_mapping>();
        }
    
        public long DealID { get; set; }
        public Nullable<long> UserProfileID { get; set; }
        public Nullable<System.DateTime> StartDate { get; set; }
        public Nullable<System.DateTime> EndDate { get; set; }
        public string DealType { get; set; }
        public string DealApplicableAs { get; set; }
        public string DealDescription { get; set; }
    
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2227:CollectionPropertiesShouldBeReadOnly")]
        public virtual ICollection<deal_product_mapping> deal_product_mapping { get; set; }
        public virtual user_profile user_profile { get; set; }
    }
}