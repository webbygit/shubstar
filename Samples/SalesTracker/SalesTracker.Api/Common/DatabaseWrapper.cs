using System;
using System.Linq;
using System.Threading.Tasks;
using SalesTracker.Database;
using SalesTracker.Common.Constant;
using SalesTracker.Common.Enum;
using SalesTracker.Common.Helpers;
using SalesTracker.Model;
using System.Collections.Generic;

namespace SalesTracker.Api.Common
{
    public class DatabaseWrapper
    {
        #region Private ReadOnly Static Objects

        private readonly static salestrackdbEntities Entities = new salestrackdbEntities();

        #endregion

        #region Accounts API Controller

        public Task<OperationResult<long>> RegisterUser(Users user)
        {
            var results = new OperationResult<long>(0, ReturnCode.Failure);

            try
            {
                if (user != null)
                {
                    var dbUserData = AutoMapperWrapper.MapEntities<Users, t_users>(user);
                    Entities.t_users.Add(dbUserData);
                    Entities.SaveChanges();
                    results.StatusCode = ReturnCode.Success;
                    results.Value = dbUserData.ID;
                }
                else
                {
                    results.ErrorMessage = ErrorMessage.CouldNotRegisterUserAsDetailsFoundToBeEmpty;
                }
            }
            catch (Exception ex)
            {
                results.ErrorMessage = ex.Message;
            }

            return Task.FromResult(results);
        }

        public Task<OperationResult<Users>> Login(Users user)
        {
            var results = new OperationResult<Users>(null, ReturnCode.Failure);

            try
            {
                if (user != null)
                {
                    var dbUser =
                        Entities.t_users.FirstOrDefault(
                            x =>
                                x.EmailId.ToLower().Equals(user.EmailId.ToLower()) && x.Password.Equals(user.Password));

                    if (dbUser == null)
                    {
                        results.ErrorMessage = ErrorMessage.EitherUserNameOrPasswordFieldDoesNotMatch;
                    }
                    else
                    {
                        var entityUser = AutoMapperWrapper.MapEntities<t_users, Users>(dbUser);
                        var dbUserDetail = Entities.t_user_details.FirstOrDefault(x => x.ID == entityUser.ID);
                        entityUser.Detail = AutoMapperWrapper.MapEntities<t_user_details, UserDetails>(dbUserDetail);

                        results.Value = entityUser;
                        results.StatusCode = ReturnCode.Success;
                    }                    
                }
                else
                {
                    results.ErrorMessage = ErrorMessage.CouldNotLoginUserAsDetailsFoundToBeEmpty;
                }
            }
            catch (Exception ex)
            {
                results.ErrorMessage = ex.Message;
            }

            return Task.FromResult(results);
        }

        public Task<OperationResult<long>> ChangePassword(string emailId, string oldPassword, string newPassword)
        {
            var result = new OperationResult<long>(0, ReturnCode.Failure);

            try
            {
                if (String.IsNullOrWhiteSpace(emailId))
                {
                    result.ErrorMessage = ErrorMessage.EmailFoundToBeEmpty;
                }
                else if (String.IsNullOrWhiteSpace(oldPassword))
                {
                    result.ErrorMessage = ErrorMessage.OldPasswordFoundToBeEmpty;
                }
                else if (String.IsNullOrWhiteSpace(newPassword))
                {
                    result.ErrorMessage = ErrorMessage.NewPasswordFoundToBeEmpty;
                }
                else
                {
                    var dbUser =
                        Entities.t_users.First(
                            x => x.EmailId.ToLower().Equals(emailId.ToLower()) && x.Password.Equals(oldPassword));

                    if (dbUser != null)
                    {
                        dbUser.Password = newPassword;
                        Entities.SaveChanges();
                        result.Value = dbUser.ID;
                        result.StatusCode = ReturnCode.Success;
                    }
                    else
                    {
                        result.ErrorMessage = ErrorMessage.OldPasswordIsIncorrect;
                    }
                }
            }
            catch (Exception ex)
            {
                result.ErrorMessage = ex.Message;
            }

            return Task.FromResult(result);
        }

        #endregion

        #region Admin API Controller

        public Task<OperationResult<long>> CreateCompany(Company company)
        {
            var results = new OperationResult<long>(0, ReturnCode.Failure);

            try
            {
                if (company != null)
                {
                    company.IsActive = true;
                    company.CreatedBy = 0;
                    company.CreatedDate = DateTime.Now;
                    var dbCompanyData = AutoMapperWrapper.MapEntities<Company, t_x_companies>(company);
                    Entities.t_x_companies.Add(dbCompanyData);
                    Entities.SaveChanges();
                    results.StatusCode = ReturnCode.Success;
                    results.Value = dbCompanyData.ID;
                }
                else
                {
                    results.ErrorMessage = ErrorMessage.CouldNotRegisterUserAsDetailsFoundToBeEmpty;
                }
            }
            catch (Exception ex)
            {
                results.ErrorMessage = ex.Message;
            }

            return Task.FromResult(results);
        }

        public Task<OperationResult<bool>> UpdateCompanyRoles(List<CompanyRole> companyRoles)
        {
            var results = new OperationResult<bool>(true, ReturnCode.Failure);

            try
            {
                if (companyRoles != null && companyRoles.Any())
                {
                    foreach (var companyRole in companyRoles)
                    {
                        var dbCompanyRoleData = AutoMapperWrapper.MapEntities<CompanyRole, t_x_company_roles>(companyRole);
                        Entities.t_x_company_roles.Add(dbCompanyRoleData);
                        Entities.SaveChanges();
                        results.Value = true;
                    }
                }
                else
                {
                    results.ErrorMessage = ErrorMessage.CouldNotRegisterUserAsDetailsFoundToBeEmpty;
                }
            }
            catch (Exception ex)
            {
                results.ErrorMessage = ex.Message;
            }

            return Task.FromResult(results);
        }

        public Task<OperationResult<bool>> UpdateCompanyPermissions(List<CompanyPermission> companyPermissions)
        {
            var results = new OperationResult<bool>(true, ReturnCode.Failure);

            try
            {
                if (companyPermissions != null && companyPermissions.Any())
                {
                    foreach (var companyPermission in companyPermissions)
                    {
                        var dbCompanyPermissionData = AutoMapperWrapper.MapEntities<CompanyPermission, t_x_company_permissions>(companyPermission);
                        Entities.t_x_company_permissions.Add(dbCompanyPermissionData);
                        Entities.SaveChanges();
                        results.Value = results.Value && (dbCompanyPermissionData.ID > 0);
                    }
                }
                else
                {
                    results.ErrorMessage = ErrorMessage.CouldNotRegisterUserAsDetailsFoundToBeEmpty;
                }
            }
            catch (Exception ex)
            {
                results.ErrorMessage = ex.Message;
            }

            return Task.FromResult(results);
        }

        #endregion

        #region Company API Controller

        public Task<OperationResult<long>> AddClient(Client client)
        {
            var results = new OperationResult<long>(0, ReturnCode.Failure);

            try
            {
                if (client != null)
                {
                    client.IsActive = true;
                    client.CreatedBy = 0;
                    client.CreatedDate = DateTime.Now;
                    var dbClientData = AutoMapperWrapper.MapEntities<Client, t_clients>(client);
                    Entities.t_clients.Add(dbClientData);
                    Entities.SaveChanges();
                    results.StatusCode = ReturnCode.Success;
                    results.Value = dbClientData.ID;
                }
                else
                {
                    results.ErrorMessage = ErrorMessage.CouldNotRegisterUserAsDetailsFoundToBeEmpty;
                }
            }
            catch (Exception ex)
            {
                results.ErrorMessage = ex.Message;
            }

            return Task.FromResult(results);
        }

        public Task<OperationResult<Company>> AddCompany(Company company)
        {
            var results = new OperationResult<Company>(null, ReturnCode.Failure);

            try
            {
                if (company != null)
                {
                    company.CreatedDate = DateTime.Now;
                    company.UpdatedDate = DateTime.Now;
                    var dbClientData = AutoMapperWrapper.MapEntities<Company, t_x_companies>(company);
                    Entities.t_x_companies.Add(dbClientData);
                    Entities.SaveChanges();

                    var dbCompany = Entities.t_x_companies.FirstOrDefault(x => x.DomainName.ToLower().Equals(company.DomainName.ToLower()));
                    var entityCompany = AutoMapperWrapper.MapEntities<t_x_companies, Company>(dbCompany);

                    results.StatusCode = ReturnCode.Success;
                    results.Value = entityCompany;
                }
                else
                {
                    results.ErrorMessage = ErrorMessage.CouldNotRegisterCompany;
                }
            }
            catch (Exception ex)
            {
                results.ErrorMessage = ex.Message;
            }

            return Task.FromResult(results);
        }

        public Task<OperationResult<Business>> AddBusiness(Business business)
        {
            var results = new OperationResult<Business>(null, ReturnCode.Failure);

            try
            {
                if (business != null)
                {
                    business.CreatedDate = DateTime.Now;
                    business.UpdatedDate = DateTime.Now;
                    var dbClientData = AutoMapperWrapper.MapEntities<Business, t_x_business>(business);
                    Entities.t_x_business.Add(dbClientData);
                    Entities.SaveChanges();

                    var dbBusiness = Entities.t_x_business.FirstOrDefault(x => x.BusinessCode.ToLower().Equals(business.BusinessCode.ToLower()));
                    var entityBusiness = AutoMapperWrapper.MapEntities<t_x_business, Business>(dbBusiness);

                    results.StatusCode = ReturnCode.Success;
                    results.Value = entityBusiness;
                }
                else
                {
                    results.ErrorMessage = ErrorMessage.CouldNotRegisterCompany;
                }
            }
            catch (Exception ex)
            {
                results.ErrorMessage = ex.Message;
            }

            return Task.FromResult(results);
        }

        public Task<OperationResult<long>> UpdateClient(Client client)
        {
            var results = new OperationResult<long>(0, ReturnCode.Failure);

            try
            {
                if (client != null && client.ID > 0)
                {
                    var dbClientData = Entities.t_clients.FirstOrDefault(x => x.ID == client.ID);

                    if (dbClientData != null)
                    {
                        dbClientData.IsActive = client.IsActive;
                        dbClientData.Latitude = client.Latitude;
                        dbClientData.Longitude = client.Longitude;
                        dbClientData.Name = client.Name;
                        dbClientData.UpdatedBy = 0;
                        dbClientData.UpdatedDate = DateTime.Now;

                        Entities.SaveChanges();
                        results.StatusCode = ReturnCode.Success;
                        results.Value = dbClientData.ID;
                    }
                }
                else
                {
                    results.ErrorMessage = ErrorMessage.CouldNotRegisterUserAsDetailsFoundToBeEmpty;
                }
            }
            catch (Exception ex)
            {
                results.ErrorMessage = ex.Message;
            }

            return Task.FromResult(results);
        }

        public Task<OperationResult<List<Client>>> GetClients()
        {
            var results = new OperationResult<List<Client>>(null, ReturnCode.Failure);

            try
            {
                var clients = Entities.t_clients.Where(x => x.IsActive).Select(x => AutoMapperWrapper.MapEntities<t_clients, Client>(x)).ToList();

                results.Value = clients;
                results.StatusCode = ReturnCode.Success;
            }
            catch (Exception ex)
            {

            }

            return Task.FromResult(results);
        }

        #endregion

        #region Private Helper Methods



        #endregion
    }
}