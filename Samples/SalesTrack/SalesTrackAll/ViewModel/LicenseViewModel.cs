using System;
using System.Linq;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Text;
using System.Threading.Tasks;

namespace SalesTrackAll.ViewModel
{

    public class LicenseViewModel
    {
        public long LicenseId { get; set; }
        public string LicenseTitle { get; set; }
        public DateTime ExpirationDate { get; set; }

        /* Other conditions  */
        public long AllowedUsers { get; set; }
        public long AllowedSalesPeople { get; set; }
        public long AllowedBusinesses { get; set; }
    }

}