using Salestrack.Common.Core;
using Salestrack.Data;
using Salestrack.Entity;
using Salestrack.Repository.Interfaces;
using System.Collections.Generic;
using System.Linq;

namespace Salestrack.Repository.Repositories
{

    public class ProductRepository : RepositoryBase<Product, SalesTrackDBContext>, IProductRepository
    {
    }

}