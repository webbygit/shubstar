using SalesTracker.Api.Common;
using SalesTracker.Common.Helpers;
using SalesTracker.Model;
using System.Collections.Generic;
using System.Threading.Tasks;
using System.Web.Http;


namespace SalesTracker.Api.Controllers
{
    public class BusinessController : ApiController
    {
        private readonly DatabaseWrapper _dbWrapper = new DatabaseWrapper();

        [HttpPost]
        public async Task<OperationResult<Business>> AddBusiness(Business business)
        {
            var result = await _dbWrapper.AddBusiness(business);

            return result;
        }
    }
}
