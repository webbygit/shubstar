using System.Web;
using System.Web.Mvc;

namespace SalesTracker.Security
{
    public class SalesTrackerAuthorizeAttribute : AuthorizeAttribute
    {
        protected override bool AuthorizeCore(HttpContextBase httpContext)
        {
            return base.AuthorizeCore(httpContext);
        }
    }
}
