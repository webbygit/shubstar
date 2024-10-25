using Salestrack.Data;
using System;
using System.Data.Entity;
using System.Data.Entity.Migrations;
using System.Linq;

using MySql.Data.Entity;
using System.Data.Entity.Migrations.Model;
using System.Collections.Generic;
using Salestrack.Entity;
using System.Data.Entity.Migrations.Sql;

namespace SalesTrackAll.Migrations
{

    internal sealed class Configuration : DbMigrationsConfiguration<SalesTrackDBContext>
    {
        public Configuration()
        {
            AutomaticMigrationsEnabled = true;

            // register mysql code generator
            SetSqlGenerator("MySql.Data.MySqlClient", new MySql.Data.Entity.MySqlMigrationSqlGenerator());

            // Custom my sql generator
            SetSqlGenerator("MySql.Data.MySqlClient", new CustomeMySqlMigrationSqlGenerator());
        }

        protected override void Seed(SalesTrackDBContext context)
        {
            // Create a Default System user. This UserInfoid will be used for all Tables
            context.UserProfiles.AddOrUpdate(
                p => p.FirstName, 
                new UserProfile { FirstName = "System", LastName = "Default", }
                );


            //  This method will be called after migrating to the latest version.

            //  You can use the DbSet<T>.AddOrUpdate() helper extension method 
            //  to avoid creating duplicate seed data. E.g.
            //
            //    context.People.AddOrUpdate(
            //      p => p.FullName,
            //      new Person { FullName = "Andrew Peters" },
            //      new Person { FullName = "Brice Lambson" },
            //      new Person { FullName = "Rowan Miller" }
            //    );
            //
        }
    }

    //DEFAULT CURRENT_TIMESTAMP,
    // ON UPDATE CURRENT_TIMESTAMP
    // Adding this custom Migration code to set Default values to the columns
    internal class CustomeMySqlMigrationSqlGenerator : MySqlMigrationSqlGenerator
    {

        protected override MigrationStatement Generate(AddColumnOperation op)
        {
            SetDefaultDateValueColumn(op.Column);

            return base.Generate(op);
        }

        protected override MigrationStatement Generate(CreateTableOperation op)
        {
            SetDefaultDateValueColumn(op.Columns);

            return base.Generate(op);
        }

        private static void SetDefaultDateValueColumn(IEnumerable<ColumnModel> columns)
        {
            foreach (var columnModel in columns)
            {
                SetDefaultDateValueColumn(columnModel);
            }
        }

        private static void SetDefaultDateValueColumn(ColumnModel column)
        {
            if (column.Name == "CreatedDate")
            {
                column.DefaultValueSql = "CURRENT_TIMESTAMP";
            }

            if (column.Name == "ModifiedDate")
            {
                column.DefaultValueSql = "ON UPDATE CURRENT_TIMESTAMP";
            }
        }
    }
}
