using SalesTracker.Api.Common;
using SalesTracker.Common.Helpers;
using SalesTracker.Model;
using System.Threading.Tasks;
using System.Web.Http;
using System.Collections.Generic;

namespace SalesTracker.Api.Controllers
{
    public class AdminController : ApiController
    {
        private readonly DatabaseWrapper _dbWrapper = new DatabaseWrapper();

        [HttpPost]
        public async Task<OperationResult<long>> CreateCompany(Company company)
        {
            var result = await _dbWrapper.CreateCompany(company);

            return result;
        }

        [HttpPost]
        public async Task<OperationResult<bool>> UpdateCompanyRoles(List<CompanyRole> companyRoles)
        {
            var result = await _dbWrapper.UpdateCompanyRoles(companyRoles);

            return result;
        }

        [HttpPost]
        public async Task<OperationResult<bool>> UpdateCompanyPermissions(List<CompanyPermission> companyPermissions)
        {
            var result = await _dbWrapper.UpdateCompanyPermissions(companyPermissions);

            return result;
        }
    }
}
