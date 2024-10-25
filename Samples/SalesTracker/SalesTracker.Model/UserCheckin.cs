using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SalesTracker.Model
{
    public class UserCheckin
    {
        public long ID { get; set; }

        public long UserId { get; set; }

        public long ClientId { get; set; }

        public float Latitude { get; set; }

        public float Longitude { get;    set; }

        public DateTime CheckInDate { get; set; }
    }
}
