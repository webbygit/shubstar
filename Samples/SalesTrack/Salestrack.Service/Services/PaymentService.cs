using Salestrack.Common.Core;
using Salestrack.Entity;
using Salestrack.Service.Interfaces;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Salestrack.Common.Interfaces;
using Salestrack.Repository.Interfaces;

namespace Salestrack.Service.Services
{

    public class PaymentService : ServiceBase<Payment>, IPaymentService
    {
        public PaymentService(IPaymentRepository repository) : base(repository)
        {
        }
    }

}