using System;
using System.Linq;
using System.Collections.Generic;
using Salestrack.Common.Core;
using Salestrack.Entity;
using Salestrack.Service.Interfaces;
using System.Text;
using System.Threading.Tasks;
using Salestrack.Common.Interfaces;
using Salestrack.Repository.Interfaces;

namespace Salestrack.Service.Services
{

    public class BusinessService : ServiceBase<Business>, IBusinessService
    {
        public BusinessService(IBusinessRepository repository) : base(repository)
        {
        }
    }

}