using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace SalesTracker.Model
{
    public class Business : BaseProperties
    {
        [Key]
        [Column(Order = 1)]
        public int BusinessID { get; set; }
        public string BusinessCode { get; set; }
        public string BusinessName { get; set; }
        public string LogoMID { get; set; }
        public string Address1 { get; set; }
        public string Address2 { get; set; }
        public string City { get; set; }
        public string State { get; set; }
        public string ZipCode { get; set; }
        public string Country { get; set; }
        public string GeoLat { get; set; }
        public string GeoLon { get; set; }
        public int? ApprovalStatusLookupID { get; set; }
        public bool IsActive { get; set; }
        public string ContactName { get; set; }
        public string Phone { get; set; }
        public string Email { get; set; }
        public string Website { get; set; }
    }
}
