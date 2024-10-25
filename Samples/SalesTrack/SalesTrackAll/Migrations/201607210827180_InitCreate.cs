namespace SalesTrackAll.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class InitCreate : DbMigration
    {
        public override void Up()
        {
            CreateTable(
                "dbo.Businesses",
                c => new
                    {
                        BusinessId = c.Int(nullable: false, identity: true),
                        BusinessCode = c.String(unicode: false),
                        BusinessName = c.String(unicode: false),
                        LogoMID = c.String(unicode: false),
                        Address1 = c.String(unicode: false),
                        Address2 = c.String(unicode: false),
                        City = c.String(unicode: false),
                        State = c.String(unicode: false),
                        ZipCode = c.String(unicode: false),
                        Country = c.String(unicode: false),
                        GeoLat = c.String(unicode: false),
                        GeoLon = c.String(unicode: false),
                        ApprovalStatusLookupID = c.Int(),
                        IsActive = c.Boolean(nullable: false),
                        ContactName = c.String(unicode: false),
                        Phone = c.String(unicode: false),
                        Email = c.String(unicode: false),
                        Website = c.String(unicode: false),
                        CreatedBy = c.Long(),
                        CreatedDate = c.DateTime(nullable: false, precision: 0),
                        ModifiedBy = c.Long(),
                        ModifiedDate = c.DateTime(nullable: false, precision: 0),
                    })
                .PrimaryKey(t => t.BusinessId);
            
            CreateTable(
                "dbo.Companies",
                c => new
                    {
                        CompanyId = c.Long(nullable: false, identity: true),
                        DomainName = c.String(unicode: false),
                        CreatedBy = c.Long(),
                        CreatedDate = c.DateTime(nullable: false, precision: 0),
                        ModifiedBy = c.Long(),
                        ModifiedDate = c.DateTime(nullable: false, precision: 0),
                    })
                .PrimaryKey(t => t.CompanyId);
            
            CreateTable(
                "dbo.UserProfiles",
                c => new
                    {
                        UserProfileId = c.Long(nullable: false, identity: true),
                        FirstName = c.String(unicode: false),
                        MiddleName = c.String(unicode: false),
                        LastName = c.String(unicode: false),
                        CreatedBy = c.Long(),
                        CreatedDate = c.DateTime(nullable: false, precision: 0),
                        ModifiedBy = c.Long(),
                        ModifiedDate = c.DateTime(nullable: false, precision: 0),
                        Company_CompanyId = c.Long(),
                    })
                .PrimaryKey(t => t.UserProfileId)
                .ForeignKey("dbo.Companies", t => t.Company_CompanyId)
                .Index(t => t.Company_CompanyId);
            
            CreateTable(
                "dbo.CompanyLicenses",
                c => new
                    {
                        CompanyLicenseId = c.Long(nullable: false, identity: true),
                        CompanyId = c.Long(nullable: false),
                        LicenseId = c.Long(nullable: false),
                        StartDate = c.DateTime(nullable: false, precision: 0),
                        EndDate = c.DateTime(precision: 0),
                        CreatedBy = c.Long(),
                        CreatedDate = c.DateTime(nullable: false, precision: 0),
                        ModifiedBy = c.Long(),
                        ModifiedDate = c.DateTime(nullable: false, precision: 0),
                    })
                .PrimaryKey(t => t.CompanyLicenseId)
                .ForeignKey("dbo.Companies", t => t.CompanyId, cascadeDelete: true)
                .ForeignKey("dbo.Licenses", t => t.LicenseId, cascadeDelete: true)
                .Index(t => t.CompanyId)
                .Index(t => t.LicenseId);
            
            CreateTable(
                "dbo.Licenses",
                c => new
                    {
                        LicenseId = c.Long(nullable: false, identity: true),
                        LicenseTypeId = c.Int(nullable: false),
                        MaxAdmins = c.Long(nullable: false),
                        MaxManagers = c.Long(nullable: false),
                        MaxSalesWorkers = c.Long(nullable: false),
                        CreatedBy = c.Long(),
                        CreatedDate = c.DateTime(nullable: false, precision: 0),
                        ModifiedBy = c.Long(),
                        ModifiedDate = c.DateTime(nullable: false, precision: 0),
                    })
                .PrimaryKey(t => t.LicenseId);
            
            CreateTable(
                "dbo.Discounts",
                c => new
                    {
                        DiscountId = c.Long(nullable: false, identity: true),
                        DiscountType = c.Int(nullable: false),
                        DiscountScope = c.Int(nullable: false),
                        MinimumPurchase = c.Single(nullable: false),
                        MaximumDiscountAmount = c.Single(nullable: false),
                        BeginDate = c.DateTime(nullable: false, precision: 0),
                        ExpiryDate = c.DateTime(nullable: false, precision: 0),
                        Order_OrderId = c.Long(),
                    })
                .PrimaryKey(t => t.DiscountId)
                .ForeignKey("dbo.Orders", t => t.Order_OrderId)
                .Index(t => t.Order_OrderId);
            
            CreateTable(
                "dbo.Goals",
                c => new
                    {
                        GoalId = c.Long(nullable: false, identity: true),
                        CreatedBy = c.Long(),
                        CreatedDate = c.DateTime(nullable: false, precision: 0),
                        ModifiedBy = c.Long(),
                        ModifiedDate = c.DateTime(nullable: false, precision: 0),
                        SalespersonDayGoal_SalespersonDayGoalId = c.Long(),
                    })
                .PrimaryKey(t => t.GoalId)
                .ForeignKey("dbo.SalespersonDayGoals", t => t.SalespersonDayGoal_SalespersonDayGoalId)
                .Index(t => t.SalespersonDayGoal_SalespersonDayGoalId);
            
            CreateTable(
                "dbo.OrderDetails",
                c => new
                    {
                        OrderDetailId = c.Long(nullable: false, identity: true),
                        ProductId = c.Long(nullable: false),
                        Quantity = c.Decimal(nullable: false, precision: 18, scale: 2),
                        IsDelivered = c.Boolean(nullable: false),
                        DeliveryDate = c.DateTime(precision: 0),
                        OrderId = c.Long(nullable: false),
                        CreatedBy = c.Long(),
                        CreatedDate = c.DateTime(nullable: false, precision: 0),
                        ModifiedBy = c.Long(),
                        ModifiedDate = c.DateTime(nullable: false, precision: 0),
                    })
                .PrimaryKey(t => t.OrderDetailId)
                .ForeignKey("dbo.Orders", t => t.OrderId, cascadeDelete: true)
                .Index(t => t.OrderId);
            
            CreateTable(
                "dbo.Orders",
                c => new
                    {
                        OrderId = c.Long(nullable: false, identity: true),
                        UserId = c.Long(nullable: false),
                        ClientId = c.Long(nullable: false),
                        OrderDate = c.DateTime(nullable: false, precision: 0),
                        OrderStatus = c.Int(nullable: false),
                        CreatedBy = c.Long(),
                        CreatedDate = c.DateTime(nullable: false, precision: 0),
                        ModifiedBy = c.Long(),
                        ModifiedDate = c.DateTime(nullable: false, precision: 0),
                    })
                .PrimaryKey(t => t.OrderId);
            
            CreateTable(
                "dbo.Payments",
                c => new
                    {
                        PaymentId = c.Long(nullable: false, identity: true),
                        PaymentType = c.Int(nullable: false),
                        Amount = c.Single(nullable: false),
                        PaymentDate = c.DateTime(nullable: false, precision: 0),
                        PaymentRecievedDate = c.DateTime(nullable: false, precision: 0),
                        Note = c.String(unicode: false),
                        CreatedBy = c.Long(),
                        CreatedDate = c.DateTime(nullable: false, precision: 0),
                        ModifiedBy = c.Long(),
                        ModifiedDate = c.DateTime(nullable: false, precision: 0),
                        Statement_StatementId = c.Long(),
                    })
                .PrimaryKey(t => t.PaymentId)
                .ForeignKey("dbo.Statements", t => t.Statement_StatementId)
                .Index(t => t.Statement_StatementId);
            
            CreateTable(
                "dbo.ProductCategories",
                c => new
                    {
                        ProductCategoryId = c.Long(nullable: false, identity: true),
                        Name = c.String(unicode: false),
                        CompanyId = c.Long(nullable: false),
                        CreatedBy = c.Long(),
                        CreatedDate = c.DateTime(nullable: false, precision: 0),
                        ModifiedBy = c.Long(),
                        ModifiedDate = c.DateTime(nullable: false, precision: 0),
                    })
                .PrimaryKey(t => t.ProductCategoryId)
                .ForeignKey("dbo.Companies", t => t.CompanyId, cascadeDelete: true)
                .Index(t => t.CompanyId);
            
            CreateTable(
                "dbo.Products",
                c => new
                    {
                        ProductId = c.Long(nullable: false, identity: true),
                        Code = c.String(unicode: false),
                        Name = c.String(unicode: false),
                        Title = c.String(unicode: false),
                        UPC = c.String(unicode: false),
                        SKU = c.String(unicode: false),
                        UnitPrice = c.Decimal(nullable: false, precision: 18, scale: 2),
                        ProductCategoryId = c.Long(nullable: false),
                        CreatedBy = c.Long(),
                        CreatedDate = c.DateTime(nullable: false, precision: 0),
                        ModifiedBy = c.Long(),
                        ModifiedDate = c.DateTime(nullable: false, precision: 0),
                    })
                .PrimaryKey(t => t.ProductId)
                .ForeignKey("dbo.ProductCategories", t => t.ProductCategoryId, cascadeDelete: true)
                .Index(t => t.ProductCategoryId);
            
            CreateTable(
                "dbo.Roles",
                c => new
                    {
                        Id = c.String(nullable: false, maxLength: 128, storeType: "nvarchar"),
                        Name = c.String(nullable: false, maxLength: 100, storeType: "nvarchar"),
                    })
                .PrimaryKey(t => t.Id)
                .Index(t => t.Name, unique: true, name: "RoleNameIndex");
            
            CreateTable(
                "dbo.UserRoles",
                c => new
                    {
                        UserId = c.String(nullable: false, maxLength: 128, storeType: "nvarchar"),
                        RoleId = c.String(nullable: false, maxLength: 128, storeType: "nvarchar"),
                        IdentityUser_Id = c.String(maxLength: 128, storeType: "nvarchar"),
                    })
                .PrimaryKey(t => new { t.UserId, t.RoleId })
                .ForeignKey("dbo.Roles", t => t.RoleId, cascadeDelete: true)
                .ForeignKey("dbo.Users", t => t.IdentityUser_Id)
                .Index(t => t.RoleId)
                .Index(t => t.IdentityUser_Id);
            
            CreateTable(
                "dbo.SalespersonDayGoals",
                c => new
                    {
                        SalespersonDayGoalId = c.Long(nullable: false, identity: true),
                        Day = c.DateTime(nullable: false, precision: 0),
                        CreatedBy = c.Long(),
                        CreatedDate = c.DateTime(nullable: false, precision: 0),
                        ModifiedBy = c.Long(),
                        ModifiedDate = c.DateTime(nullable: false, precision: 0),
                        SalespersonGoalHistory_SalespersonGoalHistoryId = c.Long(),
                        SalespersonGoal_SalespersonGoalId = c.Long(),
                    })
                .PrimaryKey(t => t.SalespersonDayGoalId)
                .ForeignKey("dbo.SalespersonGoalHistories", t => t.SalespersonGoalHistory_SalespersonGoalHistoryId)
                .ForeignKey("dbo.SalespersonGoals", t => t.SalespersonGoal_SalespersonGoalId)
                .Index(t => t.SalespersonGoalHistory_SalespersonGoalHistoryId)
                .Index(t => t.SalespersonGoal_SalespersonGoalId);
            
            CreateTable(
                "dbo.SalespersonGoalHistories",
                c => new
                    {
                        SalespersonGoalHistoryId = c.Long(nullable: false, identity: true),
                        CreatedBy = c.Long(),
                        CreatedDate = c.DateTime(nullable: false, precision: 0),
                        ModifiedBy = c.Long(),
                        ModifiedDate = c.DateTime(nullable: false, precision: 0),
                    })
                .PrimaryKey(t => t.SalespersonGoalHistoryId);
            
            CreateTable(
                "dbo.SalespersonGoals",
                c => new
                    {
                        SalespersonGoalId = c.Long(nullable: false, identity: true),
                        CreatedBy = c.Long(),
                        CreatedDate = c.DateTime(nullable: false, precision: 0),
                        ModifiedBy = c.Long(),
                        ModifiedDate = c.DateTime(nullable: false, precision: 0),
                    })
                .PrimaryKey(t => t.SalespersonGoalId);
            
            CreateTable(
                "dbo.StatementItems",
                c => new
                    {
                        StatementItemId = c.Long(nullable: false, identity: true),
                        ItemDescription = c.String(unicode: false),
                        Quantity = c.Single(nullable: false),
                        UnitRate = c.Single(nullable: false),
                        StatementId = c.Long(nullable: false),
                        CreatedBy = c.Long(),
                        CreatedDate = c.DateTime(nullable: false, precision: 0),
                        ModifiedBy = c.Long(),
                        ModifiedDate = c.DateTime(nullable: false, precision: 0),
                    })
                .PrimaryKey(t => t.StatementItemId)
                .ForeignKey("dbo.Statements", t => t.StatementId, cascadeDelete: true)
                .Index(t => t.StatementId);
            
            CreateTable(
                "dbo.Statements",
                c => new
                    {
                        StatementId = c.Long(nullable: false, identity: true),
                        StatementDate = c.DateTime(nullable: false, precision: 0),
                        StatementMonth = c.DateTime(nullable: false, precision: 0),
                        StatementNote = c.String(unicode: false),
                        BillAmount = c.Single(nullable: false),
                        BalanceAmount = c.Single(nullable: false),
                        CreatedBy = c.Long(),
                        CreatedDate = c.DateTime(nullable: false, precision: 0),
                        ModifiedBy = c.Long(),
                        ModifiedDate = c.DateTime(nullable: false, precision: 0),
                    })
                .PrimaryKey(t => t.StatementId);
            
            CreateTable(
                "dbo.UserCheckIns",
                c => new
                    {
                        UserCheckInId = c.Long(nullable: false, identity: true),
                        ClientId = c.Long(nullable: false),
                        Latitude = c.Single(nullable: false),
                        Longitude = c.Single(nullable: false),
                        CheckInDate = c.DateTime(nullable: false, precision: 0),
                        UserProfileId = c.Long(nullable: false),
                        CreatedBy = c.Long(),
                        CreatedDate = c.DateTime(nullable: false, precision: 0),
                        ModifiedBy = c.Long(),
                        ModifiedDate = c.DateTime(nullable: false, precision: 0),
                    })
                .PrimaryKey(t => t.UserCheckInId)
                .ForeignKey("dbo.UserProfiles", t => t.UserProfileId, cascadeDelete: true)
                .Index(t => t.UserProfileId);
            
            CreateTable(
                "dbo.Users",
                c => new
                    {
                        UserId = c.String(nullable: false, maxLength: 128, storeType: "nvarchar"),
                        Email = c.String(maxLength: 256, storeType: "nvarchar"),
                        EmailConfirmed = c.Boolean(nullable: false),
                        PasswordHash = c.String(unicode: false),
                        SecurityStamp = c.String(unicode: false),
                        PhoneNumber = c.String(unicode: false),
                        PhoneNumberConfirmed = c.Boolean(nullable: false),
                        TwoFactorEnabled = c.Boolean(nullable: false),
                        LockoutEndDateUtc = c.DateTime(precision: 0),
                        LockoutEnabled = c.Boolean(nullable: false),
                        AccessFailedCount = c.Int(nullable: false),
                        UserName = c.String(nullable: false, maxLength: 32, storeType: "nvarchar"),
                        CompanyId = c.Long(),
                        EmailId = c.String(unicode: false),
                        Password = c.String(unicode: false),
                        PasswordSalt = c.String(unicode: false),
                        RoleId = c.Long(),
                        Discriminator = c.String(nullable: false, maxLength: 128, storeType: "nvarchar"),
                        Detail_UserProfileId = c.Long(),
                    })
                .PrimaryKey(t => t.UserId)
                .ForeignKey("dbo.UserProfiles", t => t.Detail_UserProfileId)
                .Index(t => t.UserName, unique: true, name: "UserNameIndex")
                .Index(t => t.Detail_UserProfileId);
            
            CreateTable(
                "dbo.UserClaims",
                c => new
                    {
                        Id = c.Int(nullable: false, identity: true),
                        UserId = c.String(unicode: false),
                        ClaimType = c.String(unicode: false),
                        ClaimValue = c.String(unicode: false),
                        IdentityUser_Id = c.String(maxLength: 128, storeType: "nvarchar"),
                    })
                .PrimaryKey(t => t.Id)
                .ForeignKey("dbo.Users", t => t.IdentityUser_Id)
                .Index(t => t.IdentityUser_Id);
            
            CreateTable(
                "dbo.UserLogins",
                c => new
                    {
                        LoginProvider = c.String(nullable: false, maxLength: 128, storeType: "nvarchar"),
                        ProviderKey = c.String(nullable: false, maxLength: 128, storeType: "nvarchar"),
                        UserId = c.String(nullable: false, maxLength: 128, storeType: "nvarchar"),
                        IdentityUser_Id = c.String(maxLength: 128, storeType: "nvarchar"),
                    })
                .PrimaryKey(t => new { t.LoginProvider, t.ProviderKey, t.UserId })
                .ForeignKey("dbo.Users", t => t.IdentityUser_Id)
                .Index(t => t.IdentityUser_Id);
            
        }
        
        public override void Down()
        {
            DropForeignKey("dbo.UserRoles", "IdentityUser_Id", "dbo.Users");
            DropForeignKey("dbo.UserLogins", "IdentityUser_Id", "dbo.Users");
            DropForeignKey("dbo.UserClaims", "IdentityUser_Id", "dbo.Users");
            DropForeignKey("dbo.Users", "Detail_UserProfileId", "dbo.UserProfiles");
            DropForeignKey("dbo.UserCheckIns", "UserProfileId", "dbo.UserProfiles");
            DropForeignKey("dbo.StatementItems", "StatementId", "dbo.Statements");
            DropForeignKey("dbo.Payments", "Statement_StatementId", "dbo.Statements");
            DropForeignKey("dbo.SalespersonDayGoals", "SalespersonGoal_SalespersonGoalId", "dbo.SalespersonGoals");
            DropForeignKey("dbo.SalespersonDayGoals", "SalespersonGoalHistory_SalespersonGoalHistoryId", "dbo.SalespersonGoalHistories");
            DropForeignKey("dbo.Goals", "SalespersonDayGoal_SalespersonDayGoalId", "dbo.SalespersonDayGoals");
            DropForeignKey("dbo.UserRoles", "RoleId", "dbo.Roles");
            DropForeignKey("dbo.Products", "ProductCategoryId", "dbo.ProductCategories");
            DropForeignKey("dbo.ProductCategories", "CompanyId", "dbo.Companies");
            DropForeignKey("dbo.OrderDetails", "OrderId", "dbo.Orders");
            DropForeignKey("dbo.Discounts", "Order_OrderId", "dbo.Orders");
            DropForeignKey("dbo.CompanyLicenses", "LicenseId", "dbo.Licenses");
            DropForeignKey("dbo.CompanyLicenses", "CompanyId", "dbo.Companies");
            DropForeignKey("dbo.UserProfiles", "Company_CompanyId", "dbo.Companies");
            DropIndex("dbo.UserLogins", new[] { "IdentityUser_Id" });
            DropIndex("dbo.UserClaims", new[] { "IdentityUser_Id" });
            DropIndex("dbo.Users", new[] { "Detail_UserProfileId" });
            DropIndex("dbo.Users", "UserNameIndex");
            DropIndex("dbo.UserCheckIns", new[] { "UserProfileId" });
            DropIndex("dbo.StatementItems", new[] { "StatementId" });
            DropIndex("dbo.SalespersonDayGoals", new[] { "SalespersonGoal_SalespersonGoalId" });
            DropIndex("dbo.SalespersonDayGoals", new[] { "SalespersonGoalHistory_SalespersonGoalHistoryId" });
            DropIndex("dbo.UserRoles", new[] { "IdentityUser_Id" });
            DropIndex("dbo.UserRoles", new[] { "RoleId" });
            DropIndex("dbo.Roles", "RoleNameIndex");
            DropIndex("dbo.Products", new[] { "ProductCategoryId" });
            DropIndex("dbo.ProductCategories", new[] { "CompanyId" });
            DropIndex("dbo.Payments", new[] { "Statement_StatementId" });
            DropIndex("dbo.OrderDetails", new[] { "OrderId" });
            DropIndex("dbo.Goals", new[] { "SalespersonDayGoal_SalespersonDayGoalId" });
            DropIndex("dbo.Discounts", new[] { "Order_OrderId" });
            DropIndex("dbo.CompanyLicenses", new[] { "LicenseId" });
            DropIndex("dbo.CompanyLicenses", new[] { "CompanyId" });
            DropIndex("dbo.UserProfiles", new[] { "Company_CompanyId" });
            DropTable("dbo.UserLogins");
            DropTable("dbo.UserClaims");
            DropTable("dbo.Users");
            DropTable("dbo.UserCheckIns");
            DropTable("dbo.Statements");
            DropTable("dbo.StatementItems");
            DropTable("dbo.SalespersonGoals");
            DropTable("dbo.SalespersonGoalHistories");
            DropTable("dbo.SalespersonDayGoals");
            DropTable("dbo.UserRoles");
            DropTable("dbo.Roles");
            DropTable("dbo.Products");
            DropTable("dbo.ProductCategories");
            DropTable("dbo.Payments");
            DropTable("dbo.Orders");
            DropTable("dbo.OrderDetails");
            DropTable("dbo.Goals");
            DropTable("dbo.Discounts");
            DropTable("dbo.Licenses");
            DropTable("dbo.CompanyLicenses");
            DropTable("dbo.UserProfiles");
            DropTable("dbo.Companies");
            DropTable("dbo.Businesses");
        }
    }
}
