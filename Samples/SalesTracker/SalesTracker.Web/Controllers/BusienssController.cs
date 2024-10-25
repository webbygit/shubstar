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
    public class BusienssController : Controller
    {
        //
        // GET: /Busienss/

        public ActionResult Create()
        {
            return View();
        }

        [HttpPost]
        public ActionResult Create(Business business)
        {
            if (Session["Users"] != null)
            {
                Users users = (Users)Session["Users"];
                business.CreatedBy = users.CreatedBy;
                business.UpdatedBy = users.UpdatedBy;
                business.CreatedDate = users.CreatedDate;
                business.UpdatedDate = users.UpdatedDate;

                HttpClient client = new HttpClient();
                client.BaseAddress = new Uri("http://localhost:49479/");
                client.DefaultRequestHeaders.Accept.Clear();
                client.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));
                HttpResponseMessage response = client.PostAsJsonAsync("Busienss/AddBusiness", business).Result;
                var ResponseContent = response.Content;
                var ResponseResult = ResponseContent.ReadAsStringAsync().Result;

            }
            return View();
        }

    }
}
