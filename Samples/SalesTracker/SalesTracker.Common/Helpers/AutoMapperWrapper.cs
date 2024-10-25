using AutoMapper;

namespace SalesTracker.Common.Helpers
{
    public static class AutoMapperWrapper
    {
        public static TResponse MapEntities<TRequest, TResponse>(TRequest request)
        {
            Mapper.CreateMap<TRequest, TResponse>();
            return Mapper.Map<TResponse>(request);
        }
    }
}