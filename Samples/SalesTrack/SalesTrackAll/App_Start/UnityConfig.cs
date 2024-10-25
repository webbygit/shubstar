using Microsoft.Practices.Unity;
using Salestrack.Data;
using Salestrack.Entity;
using Salestrack.Repository.Interfaces;
using Salestrack.Repository.Repositories;
using Salestrack.Service;
using Salestrack.Service.Interfaces;
using Salestrack.Service.Services;
using System;

namespace SalesTrackAll.App_Start
{
    /// <summary>
    /// Specifies the Unity configuration for the main container.
    /// This is used for MVC Web
    /// </summary>
    public class UnityConfig
    {
        #region Unity Container
        private static Lazy<IUnityContainer> container = new Lazy<IUnityContainer>(() =>
        {
            var container = new UnityContainer();
            RegisterTypes(container);
            return container;
        });

        /// <summary>
        /// Gets the configured Unity container.
        /// </summary>
        public static IUnityContainer GetConfiguredContainer()
        {
            return container.Value;
        }
        #endregion

        /// <summary>Registers the type mappings with the Unity container.</summary>
        /// <param name="container">The unity container to configure.</param>
        /// <remarks>There is no need to register concrete types such as controllers or API controllers (unless you want to 
        /// change the defaults), as Unity allows resolving a concrete type even if it was not previously registered.</remarks>
        public static void RegisterTypes(IUnityContainer container)
        {
            // NOTE: To load from web.config uncomment the line below. Make sure to add a Microsoft.Practices.Unity.Configuration to the using statements.
            // container.LoadConfiguration();

            // TODO: Register your types here
            container.RegisterType<SalesTrackDBContext>(new HierarchicalLifetimeManager());

            container.RegisterType<IBusinessRepository, BusinessRepository>(new HierarchicalLifetimeManager());
            container.RegisterType<IBusinessService, BusinessService>(new HierarchicalLifetimeManager());

            container.RegisterType<ICompanyRepository, CompanyRepository>(new HierarchicalLifetimeManager());
            container.RegisterType<ICompanyService, CompanyService>(new HierarchicalLifetimeManager());

            container.RegisterType<IDiscountRepository, DiscountRepository>(new HierarchicalLifetimeManager());
            container.RegisterType<IDiscountService, DiscountService>(new HierarchicalLifetimeManager());

            container.RegisterType<IGoalRepository, GoalRepository>(new HierarchicalLifetimeManager());
            container.RegisterType<IGoalService, GoalService>(new HierarchicalLifetimeManager());

            container.RegisterType<ILicenseRepository, LicenseRepository>(new HierarchicalLifetimeManager());
            container.RegisterType<ILicenseService, LicenseService>(new HierarchicalLifetimeManager());

            container.RegisterType<IOrderRepository, OrderRepository>(new HierarchicalLifetimeManager());
            container.RegisterType<IOrderService, OrderService>(new HierarchicalLifetimeManager());
            container.RegisterType<IOrderDetailRepository, OrderDetailRepository>(new HierarchicalLifetimeManager());
            container.RegisterType<IOrderDetailService, OrderDetailService>(new HierarchicalLifetimeManager());

            container.RegisterType<IPaymentRepository, PaymentRepository>(new HierarchicalLifetimeManager());
            container.RegisterType<IPaymentService, PaymentService>(new HierarchicalLifetimeManager());

            container.RegisterType<IProductCategoryRepository, ProductCategoryRepository>(new HierarchicalLifetimeManager());
            container.RegisterType<IProductCategoryService, ProductCategoryService>(new HierarchicalLifetimeManager());

            container.RegisterType<IProductRepository, ProductRepository>(new HierarchicalLifetimeManager());
            container.RegisterType<IProductService, ProductService>(new HierarchicalLifetimeManager());

            container.RegisterType<ISalespersonDayGoalRepository, SalespersonDayGoalRepository>(new HierarchicalLifetimeManager());
            container.RegisterType<ISalespersonDayGoalService, SalespersonDayGoalService>(new HierarchicalLifetimeManager());

            container.RegisterType<ISalespersonGoalRepository, SalespersonGoalRepository>(new HierarchicalLifetimeManager());
            container.RegisterType<ISalespersonGoalService, SalespersonGoalService>(new HierarchicalLifetimeManager());

            container.RegisterType<ISalespersonGoalHistoryRepository, SalespersonGoalHistoryRepository>(new HierarchicalLifetimeManager());
            container.RegisterType<ISalespersonGoalHistoryService, SalespersonGoalHistoryService>(new HierarchicalLifetimeManager());

            container.RegisterType<IStatementRepository, StatementRepository>(new HierarchicalLifetimeManager());
            container.RegisterType<IStatementService, StatementService>(new HierarchicalLifetimeManager());

            container.RegisterType<IStatementItemRepository, StatementItemRepository>(new HierarchicalLifetimeManager());
            container.RegisterType<IStatementItemService, StatementItemService>(new HierarchicalLifetimeManager());

            container.RegisterType<IUserCheckInRepository, UserCheckInRepository>(new HierarchicalLifetimeManager());
            container.RegisterType<IUserCheckInService, UserCheckInService>(new HierarchicalLifetimeManager());

            container.RegisterType<IUserProfileRepository, UserProfileRepository>(new HierarchicalLifetimeManager());
            container.RegisterType<IUserProfileService, UserProfileService>(new HierarchicalLifetimeManager());

        }
    }
}
