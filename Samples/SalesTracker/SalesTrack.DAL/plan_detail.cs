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
    
    public partial class plan_detail
    {
        public long PlanDetailID { get; set; }
        public Nullable<long> PlanID { get; set; }
        public Nullable<long> FeatureID { get; set; }
        public Nullable<bool> IsActive { get; set; }
    
        public virtual feature feature { get; set; }
        public virtual plan_master plan_master { get; set; }
    }
}
