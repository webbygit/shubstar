using Salestrack.Common;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Salestrack.Common.Core;


namespace Salestrack.Entity
{
    public class UserCheckIn : BaseEntity
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public long UserCheckInId { get; set; }

        public long ClientId { get; set; }

        public float Latitude { get; set; }

        public float Longitude { get; set; }

        public DateTime CheckInDate { get; set; }

        public long UserProfileId { get; set; }
        public UserProfile UserProfile { get; set; }
    }
}
