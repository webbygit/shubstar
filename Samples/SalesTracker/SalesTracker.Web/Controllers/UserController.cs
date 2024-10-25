using System;
using System.Web.Mvc;
using SalesTracker.Model;
using System.Net.Http;
using System.Net.Http.Headers;
using Newtonsoft.Json;
using System.Web.Security;

namespace SalesTracker.Web.Controllers
{
    public class UserController : Controller
    {
        public ActionResult Register()
        {
            return View();
        }

        [HttpPost]
        public ActionResult Register(Users user)
        {
            // if logged in as company admin
            if (Session["Users"] != null)
            {
                Users loggedinUser = Session["Users"] as SalesTracker.Model.Users;
                user.CompanyId = loggedinUser.CompanyId;
            }
            

            HttpClient client = new HttpClient();
            client.BaseAddress = new Uri("http://localhost:49479/");
            client.DefaultRequestHeaders.Accept.Clear();
            client.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));
            HttpResponseMessage response = client.PostAsJsonAsync("Accounts/Register", user).Result;
            

            return View();
        }

        public ActionResult Login()
        {
            return View();
        }

        [HttpPost]
        public ActionResult Login(Users users)
        {
            HttpClient client = new HttpClient();
            client.BaseAddress = new Uri("http://localhost:49479/");
            client.DefaultRequestHeaders.Accept.Clear();
            client.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));
            HttpResponseMessage response = client.PostAsJsonAsync("Accounts/Login", users).Result;

            var ResponseContent = response.Content;
            var ResponseResult = ResponseContent.ReadAsStringAsync().Result;
            dynamic _users = JsonConvert.DeserializeObject(ResponseResult);
            //var d = _users.Value;
            Users objusers = JsonConvert.DeserializeObject<Users>(_users.Value.ToString());
            if (objusers != null)
            {
                Session["Users"] = objusers;
                string returnUrl = Request.QueryString["ReturnUrl"];
                //return RedirectToAction("Index","Dashboard");
                string decodedUrl = "";
                if (!string.IsNullOrEmpty(returnUrl))
                    decodedUrl = Server.UrlDecode(returnUrl);

                //Login logic...
                FormsAuthentication.SetAuthCookie("Sandip", true);
                if (Url.IsLocalUrl(decodedUrl))
                {
                    return Redirect(decodedUrl);
                }
                else
                    return RedirectToAction("Index", "Dashboard");
            }
            else
                return RedirectToAction("Index","Dashboard");

        }
    }
}
