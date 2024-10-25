using System;
using System.Collections.Generic;
using System.Data.Entity.Migrations;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Salestrack.Data.Helper
{
    /// <summary>
    /// This would become tedious and error prone as we start to add new entities though. 
    /// A more efficient approach would be to create a custom 
    /// SqlServerMigrationSqlGenerator and 
    /// update our migration configuration to use this.
    /// </summary>
    //public partial class InitCreate : DbMigration
    //{
    //    public override void Up()
    //    {
    //        CreateTable(
    //            "dbo.Products",
    //            c => new
    //            {
    //                ProductId = c.Int(nullable: false, identity: true),
    //                Name = c.String(),
    //                CreatedUtc = c.DateTime(nullable: false, defaultValueSql: "GETUTCDATE()"),
    //            })
    //            .PrimaryKey(t => t.ProductId);

    //    }

    //    public override void Down()
    //    {
    //        DropTable("dbo.Products");
    //    }
    //}


}
