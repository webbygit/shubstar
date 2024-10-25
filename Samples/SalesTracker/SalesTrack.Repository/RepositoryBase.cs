using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SalesTrack.Repository
{
    public class RepositoryBase<TModel, TContext>
        where TModel : class
        where TContext : DbContext, new()
    {
        private TContext _context = new TContext();

        protected DbSet<TModel> _DbSet { get; set; }

        public RepositoryBase()
        {
            _DbSet = _context.Set<TModel>();

        }

        public List<TModel> GetAll()
        {
            if (_DbSet.Count() > 0)
                return _DbSet.ToList();

            return new List<TModel>();
        }

        public TModel Get(int id)
        {
            return _DbSet.Find(id);
        }

        public void Add(TModel entity)
        {
            _DbSet.Add(entity);
        }

        //3. Mark entity as modified
        public void Modified(TModel entity)
        {
            _context.Entry<TModel>(entity).State = EntityState.Modified;
        }

        // Update/Save Entity in database
        public void Update(TModel entity)
        {
            _context.Entry<TModel>(entity).State = EntityState.Modified;
            _context.SaveChanges();
        }

        public void Delete(TModel entity)
        {
            _DbSet.Remove(entity);
        }

        public void SaveChanges()
        {
            _context.SaveChanges();
        }

    }
}
