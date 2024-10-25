using SalesTracker.Api.Common;
using SalesTracker.Common.Helpers;
using SalesTracker.Model;
using System.Collections.Generic;
using System.Threading.Tasks;
using System.Web.Http;

namespace SalesTracker.Api.Controllers
{
    public class CompanyController : ApiController
    {
        private readonly DatabaseWrapper _dbWrapper = new DatabaseWrapper();

        #region Client Section

        [HttpPost]
        public async Task<OperationResult<long>> AddClient(Client client)
        {
            var result = await _dbWrapper.AddClient(client);

            return result;
        }

        [HttpPost]
        public async Task<OperationResult<long>> UpdateClient(Client client)
        {
            var result = await _dbWrapper.UpdateClient(client);

            return result;
        }

        [HttpPost]
        public async Task<OperationResult<List<Client>>> GetClients()
        {
            var result = await _dbWrapper.GetClients();

            return result;
        }

        [HttpPost]
        public async Task<OperationResult<Company>> AddCompany(Company company)
        {
            var result = await _dbWrapper.AddCompany(company);

            return result;
        }
        #endregion

        #region Product Section



        #endregion

    }
}
