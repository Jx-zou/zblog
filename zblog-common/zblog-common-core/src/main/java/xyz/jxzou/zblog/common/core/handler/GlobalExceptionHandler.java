package xyz.jxzou.zblog.common.core.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import xyz.jxzou.zblog.common.exception.enums.CommonResponseEnum;
import xyz.jxzou.zblog.common.exception.enums.ServletResponseEnum;
import xyz.jxzou.zblog.common.exception.message.UnifiedMessageSource;
import xyz.jxzou.zblog.common.exception.model.exception.*;
import xyz.jxzou.zblog.common.util.pojo.ResponseResult;

import javax.validation.ValidationException;

/**
 * GlobalExceptionHandler
 *
 * @author jx
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static final String ENV_PROD = "prod";

    private final UnifiedMessageSource unifiedMessageSource;

    @Value("${spring.profiles.active}")
    private String profile;

    /**
     * Instantiates a new Global exception handler.
     *
     * @param unifiedMessageSource the unified message source
     */
    @Autowired
    public GlobalExceptionHandler(UnifiedMessageSource unifiedMessageSource) {
        this.unifiedMessageSource = unifiedMessageSource;
    }

    private String getMessage(Integer code){
        String message = unifiedMessageSource.getMessage(code);
        if (message == null || message.isEmpty()) {
            return CommonResponseEnum.SERVER_ERROR.getMessage();
        }
        return message;
    }

    private String getMessage(BaseException e) {
        String message = unifiedMessageSource.getMessage(e.getResult().getStatus(), e.getArgs());
        if (message == null || message.isEmpty()) {
            return e.getMessage();
        }
        return message;
    }

    private final boolean isProd = ENV_PROD.equals(profile);

    /**
     * Handle argument exception response result.
     *
     * @param e the e
     * @return the response result
     */
    @ExceptionHandler(ArgumentException.class)
    public ResponseResult<Void> handleArgumentException(ArgumentException e) {
        log.error(e.getMessage(), e);
        return new ResponseResult<Void>(e.getResult().getStatus(), getMessage(e));
    }

    /**
     * Handle common exception response result.
     *
     * @param e the e
     * @return the response result
     */
    @ExceptionHandler(CommonException.class)
    public ResponseResult<Void> handleCommonException(CommonException e) {
        log.error(e.getMessage(), e);
        return new ResponseResult<Void>(e.getResult().getStatus(), getMessage(e));
    }

    /**
     * Handle servlet exception response result.
     *
     * @param e the e
     * @return the response result
     */
    @ExceptionHandler(ServletException.class)
    public ResponseResult<Void> handleServletException(ServletException e) {
        log.error(e.getMessage(), e);
        return new ResponseResult<Void>(e.getResult().getStatus(), getMessage(e));
    }

    /**
     * Handle business exception response result.
     *
     * @param e the e
     * @return the response result
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseResult<Void> handleBusinessException(BusinessException e) {
        log.error(e.getMessage(), e);
        return new ResponseResult<Void>(e.getResult().getStatus(), getMessage(e));
    }

    /**
     * Handle base exception response result.
     *
     * @param e the e
     * @return the response result
     */
    @ExceptionHandler(BaseException.class)
    public ResponseResult<Void> handleBaseException(BaseException e) {
        log.error(e.getMessage(), e);
        return new ResponseResult<Void>(e.getResult().getStatus(), getMessage(e));
    }

    /**
     * Handle base exception response result.
     *
     * @param e the e
     * @return the response result
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseResult<Void> handleValidationException(ValidationException e) {
        log.error(e.getMessage(), e);
        return CommonResponseEnum.ERROR.getResult();
    }

    /**
     * Handle servlet exception response result.
     *
     * @param e the e
     * @return the response result
     */
    @ExceptionHandler({
            NoHandlerFoundException.class,
            HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeNotSupportedException.class,
            HttpMediaTypeNotAcceptableException.class,
            HttpMessageNotReadableException.class,
            HttpMessageNotWritableException.class,
            MissingPathVariableException.class,
            MissingServletRequestParameterException.class,
            TypeMismatchException.class,
            BindException.class,
            MethodArgumentNotValidException.class,
            ServletRequestBindingException.class,
            ConversionNotSupportedException.class,
            MissingServletRequestPartException.class,
            AsyncRequestTimeoutException.class
    })
    public ResponseResult<Void> handleServletException(Exception e) {
        log.error(e.getMessage(), e);
        Integer status = CommonResponseEnum.SERVER_ERROR.getStatus();

        if (isProd){
            String message = getMessage(status);
            return new ResponseResult<Void>(status, message);
        }

        try {
            ServletResponseEnum servletExceptionEnum = ServletResponseEnum.valueOf(e.getClass().getSimpleName());
            status = servletExceptionEnum.getStatus();
        } catch (IllegalArgumentException exception){
            log.error("class [{}] not defined in enum {}", e.getClass().getName(), ServletResponseEnum.class.getName());
        }

        return new ResponseResult<Void>(status, e.getMessage());
    }

    /**
     * Handle exception response result.
     *
     * @param e the e
     * @return the response result
     */
    @ExceptionHandler(Exception.class)
    public ResponseResult<Void> handleException(Exception e){
        log.error(e.getMessage(), e);
        int code = CommonResponseEnum.SERVER_ERROR.getStatus();
        if (isProd){
            return new ResponseResult<Void>(code, getMessage(code));
        }
        return new ResponseResult<Void>(code, e.getMessage());
    }
}
