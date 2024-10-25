namespace SalesTracker.Model
{
    public class Users : BaseProperties
    {
        public long CompanyId { get; set; }

        public string EmailId { get; set; }

        public string Password { get; set; }

        public string PasswordSalt { get; set; }

        public long RoleId { get; set; }

        public UserDetails Detail { get; set; }
    }

    public class UserDetails
    {
        public long ID { get; set; }

        public long UserId { get; set; }

        public string FirstName { get; set; }

        public string MiddleName { get; set; }

        public string LastName { get; set; }
    }
}