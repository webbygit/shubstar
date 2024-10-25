using Microsoft.Owin.Security;
using System.Web;
using System.Web.Mvc;

using System.Security.Claims;
using Microsoft.AspNet.Identity;
using SalesTrackAll.Models;

namespace SalesTrackAll.Controllers
{

    // https://github.com/khalidabuhakmeh/SimplestAuthMvc5
    // http://www.khalidabuhakmeh.com/asp-net-mvc-5-authentication-breakdown-part-deux
    // https://weblog.west-wind.com/posts/2015/Apr/29/Adding-minimal-OWIN-Identity-Authentication-to-an-Existing-ASPNET-MVC-Application

    // This is very importnat to implement page level security
    //https://ovaismehboob.wordpress.com/2014/04/21/extending-and-customizing-authentication-mechanism-in-web-applications-using-asp-net-identity/
    public class AuthenticationController : Controller
    {
        IAuthenticationManager Authentication
        {
            get { return HttpContext.GetOwinContext().Authentication; }
        }

        [HttpGet]
        [Route(("login"))]
        public ActionResult Show()
        {
            return View();
        }

        [HttpPost]
        [Route("login")]
        [ValidateAntiForgeryToken]
        public ActionResult Login(LoginModel input)
        {
            if (ModelState.IsValid)
            {
                if (input.HasValidUsernameAndPassword)
                {
                    var identity = new ClaimsIdentity(new[] {
                            new Claim(ClaimTypes.Name, input.Username),
                        },
                        DefaultAuthenticationTypes.ApplicationCookie,
                        ClaimTypes.Name, ClaimTypes.Role);

                    // if you want roles, just add as many as you want here (for loop maybe?)
                    identity.AddClaim(new Claim(ClaimTypes.Role, "guest"));
                    // tell OWIN the identity provider, optional
                    // identity.AddClaim(new Claim(IdentityProvider, "Simplest Auth"));

                    Authentication.SignIn(new AuthenticationProperties
                    {
                        IsPersistent = input.RememberMe
                    }, identity);

                    return RedirectToAction("index", "home");
                }
            }

            return View("show", input);
        }

        [HttpGet]
        [Route("logout")]
        public ActionResult Logout()
        {
            Authentication.SignOut(DefaultAuthenticationTypes.ApplicationCookie);
            return RedirectToAction("login");
        }

    }
}