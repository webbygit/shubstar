using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Data.Entity.Infrastructure;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Salestrack.Data
{

    /// <summary>
    /// create a custom Entity Framework initializer for MySQL
    /// The MySQL provider that is featured in this tutorial does not currently support 
    /// Entity Framework migrations, so you will need to use model initializers in order 
    /// to connect to the database. Because this tutorial is using a MySQL instance on Azure, 
    /// you will need need to create a custom Entity Framework initializer.
    /// 
    /// See commented code below : :
    /// 
    /// Rupesh:: Not sure if it is needed now.. 
    /// This is needed when DB Migrations were not supported by MYSQL PRovider
    /// I have moved InitializeDatabase function to RecommndedDBInitize file
    /// becuase the seed method is there and supposed to be called when databse 
    /// initilizes.. 
    /// So for now this MySQLInitializer is redundant. will keep it for now but I think 
    /// it is no more needed. 
    /// </summary>
    public class MySqlInitializer : IDatabaseInitializer<SalesTrackDBContext>
    {
        public void InitializeDatabase(SalesTrackDBContext context)
        {
            if (!context.Database.Exists())
            {
                // if database did not exist before - create it
                context.Database.Create();
            }
            else
            {
                // query to check if MigrationHistory table is present in the database 
                var migrationHistoryTableExists = ((IObjectContextAdapter)context).ObjectContext.ExecuteStoreQuery<int>(
                string.Format(
                  "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = '{0}' AND table_name = '__MigrationHistory'",
                  "nboundsdb"));

                // if MigrationHistory table is not there (which is the case first time we run) - create it
                if (migrationHistoryTableExists.FirstOrDefault() == 0)
                {
                    context.Database.Delete();
                    context.Database.Create();
                }
            }
        }
    }

}
