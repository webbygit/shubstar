using Salestrack.Common.Core;
using Salestrack.Data;
using Salestrack.Entity;
using Salestrack.Repository.Interfaces;

using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;


namespace Salestrack.Repository.Repositories
{
    public class UserProfileRepository : RepositoryBase<UserProfile, SalesTrackDBContext>, IUserProfileRepository
    {
    }
}