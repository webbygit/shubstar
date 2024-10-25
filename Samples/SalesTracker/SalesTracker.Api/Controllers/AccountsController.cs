using System.Threading.Tasks;
using System.Web.Http;
using SalesTracker.Api.Common;
using SalesTracker.Common.Helpers;
using SalesTracker.Model;

namespace SalesTracker.Api.Controllers
{
    public class AccountsController : ApiController
    {
        private readonly DatabaseWrapper _dbWrapper = new DatabaseWrapper();

        [HttpPost]
        public async Task<OperationResult<long>> Register(Users user)
        {
            var result = await _dbWrapper.RegisterUser(user);

            return result;
        }

        [HttpPost]
        public async Task<OperationResult<Users>> Login(Users user)
        {
            var result = await _dbWrapper.Login(user);

            return result;
        }

        [HttpPost]
        public async Task<OperationResult<long>> ChangePassword(Users user, string newPassword)
        {
            var result = await _dbWrapper.ChangePassword(user.Name, user.Password, newPassword);

            return result;
        }
    }
}
