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
    
    public partial class user_login_history
    {
        public long LoginHistoryID { get; set; }
        public Nullable<long> UserID { get; set; }
        public string IPAddress { get; set; }
        public Nullable<System.DateTime> LastLoggedDate { get; set; }
        public string Message { get; set; }
    
        public virtual user user { get; set; }
    }
}
