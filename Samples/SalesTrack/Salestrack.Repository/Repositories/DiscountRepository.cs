using System;
using System.Linq;
using System.Collections.Generic;
using Salestrack.Common.Core;
using Salestrack.Data;
using Salestrack.Entity;
using Salestrack.Repository.Interfaces;

namespace Salestrack.Repository.Repositories
{

    public class DiscountRepository : RepositoryBase<Discount, SalesTrackDBContext>, IDiscountRepository
    {
    }

}