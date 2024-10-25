using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Http;
using System.Web.Http;
using Microsoft.Owin.Security.OAuth;
using Newtonsoft.Json.Serialization;
using SalesTrack.Common.DI;
using Microsoft.Practices.Unity;
using Salestrack.Data;
using Salestrack.Repository.Interfaces;
using Salestrack.Repository.Repositories;
using Salestrack.Service.Interfaces;
using Salestrack.Service.Services;
using System.Web.Mvc;

namespace SalesTrackAll
{
    public static class WebApiConfig
    {
        public static void Register(HttpConfiguration config)
        {
            // This DI is for Wen APIs
            // Dependency Resolver
            var container = new UnityContainer();
            config.DependencyResolver = new UnityResolver(container);
            
            // Register DBcontext
            container.RegisterType<SalesTrackDBContext>();
            container.RegisterType<IBusinessRepository, BusinessRepository>(new HierarchicalLifetimeManager());
            container.RegisterType<IBusinessService, BusinessService>(new HierarchicalLifetimeManager());

            container.RegisterType<ICompanyRepository,CompanyRepository >(new HierarchicalLifetimeManager());
            container.RegisterType<ICompanyService, CompanyService >(new HierarchicalLifetimeManager());

            container.RegisterType<ICompanyLicenseRepository, CompanyLicenseRepository>(new HierarchicalLifetimeManager());
            container.RegisterType<ICompanyLicenseService, CompanyLicenseService>(new HierarchicalLifetimeManager());

            container.RegisterType<IDiscountRepository, DiscountRepository >(new HierarchicalLifetimeManager());
            container.RegisterType<IDiscountService, DiscountService >(new HierarchicalLifetimeManager());

            container.RegisterType<IGoalRepository, GoalRepository >(new HierarchicalLifetimeManager());
            container.RegisterType<IGoalService, GoalService >(new HierarchicalLifetimeManager());

            container.RegisterType<ILicenseRepository, LicenseRepository>(new HierarchicalLifetimeManager());
            container.RegisterType<ILicenseService, LicenseService>(new HierarchicalLifetimeManager());

            container.RegisterType<IOrderRepository, OrderRepository >(new HierarchicalLifetimeManager());
            container.RegisterType<IOrderService, OrderService >(new HierarchicalLifetimeManager());
            container.RegisterType<IOrderDetailRepository, OrderDetailRepository >(new HierarchicalLifetimeManager());
            container.RegisterType<IOrderDetailService, OrderDetailService>(new HierarchicalLifetimeManager());

            container.RegisterType<IPaymentRepository, PaymentRepository >(new HierarchicalLifetimeManager());
            container.RegisterType<IPaymentService, PaymentService>(new HierarchicalLifetimeManager());

            container.RegisterType<IProductCategoryRepository, ProductCategoryRepository>(new HierarchicalLifetimeManager());
            container.RegisterType<IProductCategoryService, ProductCategoryService >(new HierarchicalLifetimeManager());

            container.RegisterType<IProductRepository, ProductRepository >(new HierarchicalLifetimeManager());
            container.RegisterType<IProductService, ProductService >(new HierarchicalLifetimeManager());

            container.RegisterType<ISalespersonDayGoalRepository, SalespersonDayGoalRepository >(new HierarchicalLifetimeManager());
            container.RegisterType<ISalespersonDayGoalService, SalespersonDayGoalService >(new HierarchicalLifetimeManager());

            container.RegisterType<ISalespersonGoalRepository, SalespersonGoalRepository >(new HierarchicalLifetimeManager());
            container.RegisterType<ISalespersonGoalService, SalespersonGoalService >(new HierarchicalLifetimeManager());

            container.RegisterType<ISalespersonGoalHistoryRepository, SalespersonGoalHistoryRepository >(new HierarchicalLifetimeManager());
            container.RegisterType<ISalespersonGoalHistoryService, SalespersonGoalHistoryService>(new HierarchicalLifetimeManager());

            container.RegisterType<IStatementRepository, StatementRepository >(new HierarchicalLifetimeManager());
            container.RegisterType<IStatementService, StatementService >(new HierarchicalLifetimeManager());

            container.RegisterType<IStatementItemRepository, StatementItemRepository >(new HierarchicalLifetimeManager());
            container.RegisterType<IStatementItemService, StatementItemService>(new HierarchicalLifetimeManager());

            container.RegisterType<IUserCheckInRepository, UserCheckInRepository >(new HierarchicalLifetimeManager());
            container.RegisterType<IUserCheckInService, UserCheckInService >(new HierarchicalLifetimeManager());

            container.RegisterType<IUserProfileRepository, UserProfileRepository >(new HierarchicalLifetimeManager());
            container.RegisterType<IUserProfileService, UserProfileService>(new HierarchicalLifetimeManager());

            // Web API configuration and services   
            // Configure Web API to use only bearer token authentication.
            config.SuppressDefaultHostAuthentication();
            config.Filters.Add(new HostAuthenticationFilter(OAuthDefaults.AuthenticationType));

            // Web API routes
            config.MapHttpAttributeRoutes();

            config.Routes.MapHttpRoute(
                name: "DefaultApi",
                routeTemplate: "api/{controller}/{id}",
                defaults: new { id = RouteParameter.Optional }
            );
        }
    }
}
