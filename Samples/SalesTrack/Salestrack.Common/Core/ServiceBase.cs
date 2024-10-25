using Salestrack.Common.Interfaces;
using System;
using System.Collections.Generic;
using System.Linq.Expressions;

namespace Salestrack.Common.Core
{
    public abstract class ServiceBase<TEntity> : IService<TEntity> where TEntity : class
    {
        private IRepository<TEntity> _repository;

        public IRepository<TEntity> Context { get; private set; }

        public ServiceBase(IRepository<TEntity> repository)
        {
            this._repository = repository;
            Context = repository;
        }

        public IEnumerable<TEntity> GetAll()
        {
            IEnumerable<TEntity> result = _repository.GetAll();
            return result;
        }

        public TEntity Get(int id)
        {
            return _repository.Get(id);
        }

        public void Add(TEntity model)
        {
            _repository.Add(model);

        }

        public void Delete(int id)
        {
            _repository.Delete(id);
        }
        public void Delete(TEntity model)
        {
            _repository.Delete(model);
        }

        public void Edit(TEntity model)
        {
            _repository.Edit(model);
        }

        public void MarkModified(TEntity model)
        {
            _repository.MarkModified(model);
        }

        public void Save()
        {
            _repository.Save();

        }

        public IEnumerable<TEntity> FindBy(Expression<Func<TEntity, bool>> predicate)
        {
            return _repository.FindBy(predicate);
        }

        public void AddRange(IEnumerable<TEntity> entities)
        {
            _repository.AddRange(entities);
        }

        public void DeleteRange(IEnumerable<TEntity> entities)
        {
            _repository.DeleteRange(entities);
        }

        public void EditRange(IEnumerable<TEntity> entities)
        {
            _repository.EditRange(entities);
        }
    }
}
