using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Salestrack.Common.Core;
using System;
using System.Collections.Generic;

namespace Salestrack.Entity
{

    public class SalespersonGoalHistory : BaseEntity
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public long SalespersonGoalHistoryId { get; set; }

        public ICollection<SalespersonDayGoal> Goals { get; set; }
    }

}