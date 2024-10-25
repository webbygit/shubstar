using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Salestrack.Common.Core;

using System;
using System.Collections.Generic;

namespace Salestrack.Entity
{
    // Goals for more than a day
    // Intended to be used for Monthly Goals And Yearly Goals
    public class SalespersonGoal : BaseEntity
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public long SalespersonGoalId { get; set; }

        public ICollection<SalespersonDayGoal> Goals{ get; set; }
    }

}
