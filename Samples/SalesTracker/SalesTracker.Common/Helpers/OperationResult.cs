using SalesTracker.Common.Enum;

namespace SalesTracker.Common.Helpers
{
    public class OperationResult<T>
    {
        #region Public Properties

        public T Value { get; set; }

        public ReturnCode StatusCode { get; set; }

        public string ErrorMessage { get; set; }

        #endregion

        #region Constructors

        public OperationResult(T value, ReturnCode statusCode)
        {
            Value = value;
            StatusCode = statusCode;
        }

        public OperationResult(T value, ReturnCode statusCode, string errorMessage)
        {
            Value = value;
            StatusCode = statusCode;
            ErrorMessage = errorMessage;
        }

        #endregion

        #region Public Methods

        public bool IsOperationSuccessful()
        {
            return (StatusCode == ReturnCode.Success && Value != null);
        }

        #endregion
    }
}