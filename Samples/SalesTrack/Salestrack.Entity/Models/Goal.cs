using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using Salestrack.Common.Core;

using System;
using System.Collections.Generic;

namespace Salestrack.Entity
{
    public class Goal : BaseEntity
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public long GoalId { get; set; }


    }
}
