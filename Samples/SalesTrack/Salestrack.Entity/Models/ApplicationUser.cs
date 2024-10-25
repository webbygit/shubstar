using Microsoft.AspNet.Identity;
using Microsoft.AspNet.Identity.EntityFramework;
using Salestrack.Common;
using System.ComponentModel.DataAnnotations.Schema;
using System.Security.Claims;
using System.Threading.Tasks;

namespace Salestrack.Entity
{
    // You can add profile data for the user by adding more properties
    //to your ApplicationUser class, 
    //please visit http://go.microsoft.com/fwlink/?LinkID=317594 to learn more.
    public class ApplicationUser : IdentityUser
    {
        public long CompanyId { get; set; }

        public string EmailId { get; set; }

        public string Password { get; set; }

        public string PasswordSalt { get; set; }

        public long RoleId { get; set; }

        public virtual UserProfile Detail { get; set; }

        public async Task<ClaimsIdentity> GenerateUserIdentityAsync(UserManager<ApplicationUser> manager, string authenticationType)
        {
            // Note the authenticationType must match the one defined 
            // in CookieAuthenticationOptions.AuthenticationType
            var userIdentity = await manager.CreateIdentityAsync(this, authenticationType);

            // Add custom user claims here

            return userIdentity;
        }
    }

}