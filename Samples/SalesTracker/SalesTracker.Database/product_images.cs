//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated from a template.
//
//     Manual changes to this file may cause unexpected behavior in your application.
//     Manual changes to this file will be overwritten if the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

namespace SalesTracker.Database
{
    using System;
    using System.Collections.Generic;
    
    public partial class product_images
    {
        public long ImageID { get; set; }
        public Nullable<long> ProductID { get; set; }
        public string ImageName { get; set; }
    
        public virtual product product { get; set; }
    }
}
