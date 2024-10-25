using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SalesTrackAll.ViewModel
{
    public class CompanyViewModel
    {
        [Key]
        public long CompanyID { get; set; }

        [Required]
        public string CompanyName { get; set; }
        public string CompanyTitle { get; set; }

        [Url]
        public string Website { get; set; }

        [EmailAddress]
        public string Email { get; set; }

        [Phone]
        public string PhoneNumber { get; set; }

        [Required]
        public string ContactFirstName { get; set; }
        [Required]
        public string ContactLastName { get; set; }
        [Required]
        public string ContactPhoneNumber { get; set; }
        public string City { get; set; }
        public string ZipCode { get; set; }
    }

    public class CompanyDetailsViewModel
    {
        public CompanyViewModel Company { get; set; }
        public LicenseViewModel License { get; set; }
    }

}
