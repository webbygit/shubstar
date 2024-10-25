using MySql.Data.Entity;
using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Salestrack.Data
{
    public class MySqlConfiguration : DbConfiguration
    {
        /// <summary>
        /// configure Entity Framework to use the modified HistoryContext, rather than default one. 
        /// This can be done by leveraging code-based configuration features. 
        /// </summary>
        public MySqlConfiguration()
        {
            SetHistoryContext(
            "MySql.Data.MySqlClient", (conn, schema) => new MySqlHistoryContext(conn, schema));
        }
    }
}
