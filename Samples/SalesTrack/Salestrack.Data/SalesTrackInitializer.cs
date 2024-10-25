using Salestrack.Entity;
using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Data.Entity.Infrastructure;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Salestrack.Data
{
    class SalesTrackInitializer : DropCreateDatabaseAlways<SalesTrackDBContext>
    {

        // Use override id inherited from IdentityDBContext
        public override void InitializeDatabase(SalesTrackDBContext context)
        //public /*override*/ new void InitializeDatabase(SalesTrackDBContext context)
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
                  "salestrackdb"));

                // if MigrationHistory table is not there (which is the case first time we run) - create it
                if (migrationHistoryTableExists.FirstOrDefault() == 0)
                {
                    context.Database.Delete();
                    context.Database.Create();
                }
            }
        }

        protected override void Seed(SalesTrackDBContext context)
        {
            #region Users
            // add a few users
            //List<UserProfile> userList = new List<UserProfile>
            //{
            //    new UserProfile
            //    {
            //        FirstName = "System",
            //        LastName = "Admin",
            //    },
            //};

            //userList.ForEach(u => context.UserProfiles.Add(u));
            //context.SaveChanges();
            #endregion

            base.Seed(context);
        }


    }
}
