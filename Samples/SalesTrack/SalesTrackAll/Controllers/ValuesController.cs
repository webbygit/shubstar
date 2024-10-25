﻿using Salestrack.Entity;
using Salestrack.Service.Interfaces;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace SalesTrackAll.Controllers
{
 //   [Authorize]
    public class ValuesController : ApiController
    {
        IProductService _service;

        public ValuesController(IProductService service)
        {
            _service = service;
        }
   
        // GET api/values
        public IEnumerable<Product> Get()
        {
            return _service.GetAll();
        }

        // GET api/values/5
        public string Get(int id)
        {
            return "value";
        }

        // POST api/values
        public void Post([FromBody]string value)
        {
        }

        // PUT api/values/5
        public void Put(int id, [FromBody]string value)
        {
        }

        // DELETE api/values/5
        public void Delete(int id)
        {
        }
    }
}
