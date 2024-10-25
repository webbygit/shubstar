using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Salestrack.Common.Core;
using System;
using System.Collections.Generic;

namespace Salestrack.Entity
{

    public class SalespersonDayGoal : BaseEntity
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public long SalespersonDayGoalId { get; set; }
        public DateTime Day { get; set; }

        public ICollection<Goal> Goals { get; set; }
    }

   
}