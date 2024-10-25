using Newtonsoft.Json;
using SalesTracker.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Web;
using System.Web.Mvc;

namespace SalesTracker.Web.Controllers
{
    public class CompanyController : Controller
    {
        [Authorize]
        public ActionResult Create()
        {
            return View();
        }

        [HttpPost]
        public ActionResult Create(Company company)
        {
            if (Session["Users"] != null)
            {
                Users users = (Users)Session["Users"];
                company.CreatedBy = users.CreatedBy;
                company.UpdatedBy = users.UpdatedBy;
                HttpClient client = new HttpClient();
                client.BaseAddress = new Uri("http://localhost:49479/");
                client.DefaultRequestHeaders.Accept.Clear();
                client.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));
                HttpResponseMessage response = client.PostAsJsonAsync("Company/AddCompany", company).Result;
                var ResponseContent = response.Content;
                var ResponseResult = ResponseContent.ReadAsStringAsync().Result;

                dynamic _company = JsonConvert.DeserializeObject(ResponseResult);
                Company objCompany = JsonConvert.DeserializeObject<Company>(_company.Value.ToString());

                company.CompanyAdmin.CompanyId = ((SalesTracker.Model.BaseProperties)(objCompany)).ID;
                response = client.PostAsJsonAsync("Accounts/Register", company.CompanyAdmin).Result;
            }
            else
                return RedirectToAction("Login", "User");
            return RedirectToAction("Info",company);
        }

        [Authorize]
        public ActionResult Info(Company company)
        {
            return View(company);
        }

    }
}
