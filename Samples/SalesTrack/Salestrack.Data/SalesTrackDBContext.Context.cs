﻿//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated from a template.
//
//     Manual changes to this file may cause unexpected behavior in your application.
//     Manual changes to this file will be overwritten if the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

namespace Salestrack.Data
{
    using System;
    using System.Data.Entity;
    using System.Data.Entity.Infrastructure;
    
    public partial class salestrackdbConnection : DbContext
    {
        public salestrackdbConnection()
            : base("name=salestrackdbConnection")
        {
        }
    
        protected override void OnModelCreating(DbModelBuilder modelBuilder)
        {
            throw new UnintentionalCodeFirstException();
        }
    
        public virtual DbSet<brand> brands { get; set; }
        public virtual DbSet<business> businesses { get; set; }
        public virtual DbSet<deal_product_mapping> deal_product_mapping { get; set; }
        public virtual DbSet<deal> deals { get; set; }
        public virtual DbSet<feature> features { get; set; }
        public virtual DbSet<plan_detail> plan_detail { get; set; }
        public virtual DbSet<plan_master> plan_master { get; set; }
        public virtual DbSet<product_category> product_category { get; set; }
        public virtual DbSet<product_images> product_images { get; set; }
        public virtual DbSet<product> products { get; set; }
        public virtual DbSet<salesperson> salespersons { get; set; }
        public virtual DbSet<salesperson_business_mapping> salesperson_business_mapping { get; set; }
        public virtual DbSet<user_login_history> user_login_history { get; set; }
        public virtual DbSet<user_permissions> user_permissions { get; set; }
        public virtual DbSet<user_profile> user_profile { get; set; }
        public virtual DbSet<user_role_mapping> user_role_mapping { get; set; }
        public virtual DbSet<user_roles> user_roles { get; set; }
        public virtual DbSet<user> users { get; set; }
    }
}
