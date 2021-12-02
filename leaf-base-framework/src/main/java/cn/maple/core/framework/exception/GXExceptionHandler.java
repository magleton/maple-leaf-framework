package cn.maple.core.framework.exception;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.http.HttpStatus;
import cn.maple.core.framework.code.GXResultCode;
import cn.maple.core.framework.util.GXResultUtils;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.ValidationException;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.HashMap;
import java.util.Map;

/**
 * 异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GXExceptionHandler {
    @ExceptionHandler(MismatchedInputException.class)
    public GXResultUtils<String> handleMismatchedInputException(MismatchedInputException e) {
        log.error(e.getMessage(), e);
        return GXResultUtils.ok(HttpStatus.HTTP_INTERNAL_ERROR, "参数错误!");
    }

    @ExceptionHandler(GXBeanValidateException.class)
    public GXResultUtils<Dict> handleBeanValidateException(GXBeanValidateException e) {
        log.error(e.getMessage(), e);
        return GXResultUtils.error(e.getCode(), e.getMsg(), e.getDict());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public GXResultUtils<String> handlerNoFoundException(NoHandlerFoundException e) {
        log.error(e.getMessage(), e);
        return GXResultUtils.error(404, "路径不存在，请检查路径是否正确");
    }

    @ExceptionHandler(BindException.class)
    public GXResultUtils<Map<String, Object>> handleBindException(BindException e) {
        log.error(e.getMessage(), e);
        Map<String, Object> errors = new HashMap<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return GXResultUtils.error(GXResultCode.COMMON_ERROR, errors);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public GXResultUtils<Map<String, Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, Object> errors = new HashMap<>();
        String firstErrorKey = null;
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
            if (CharSequenceUtil.isEmpty(firstErrorKey)) {
                firstErrorKey = error.getField();
            }
        }
        log.error(e.getMessage(), e);
        Object orDefault = errors.getOrDefault(firstErrorKey, "");
        return GXResultUtils.error(GXResultCode.PARAMETER_VALIDATION_ERROR.getCode(), GXResultCode.PARAMETER_VALIDATION_ERROR.getMsg() + ":" + firstErrorKey + "->" + orDefault);
    }

    @ExceptionHandler(ValidationException.class)
    public GXResultUtils<Map<String, Object>> handleValidationException(ValidationException e) {
        log.error(e.getMessage(), e);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("msg", e.getCause().getMessage());
        return GXResultUtils.error(GXResultCode.COMMON_ERROR, hashMap);
    }

    @ExceptionHandler(MultipartException.class)
    public GXResultUtils<String> handleMultipartException(MultipartException e, RedirectAttributes redirectAttributes) {
        log.error(e.getMessage(), e);
        return GXResultUtils.error(HttpStatus.HTTP_INTERNAL_ERROR, e.getCause().getMessage());
    }

    @ExceptionHandler(java.net.BindException.class)
    public GXResultUtils<String> handleBindException(MultipartException e, RedirectAttributes redirectAttributes) {
        log.error(e.getMessage(), e);
        return GXResultUtils.error(HttpStatus.HTTP_INTERNAL_ERROR, e.getMessage());
    }

    @ExceptionHandler(GXTokenEmptyException.class)
    public GXResultUtils<String> handleGXTokenEmptyException(GXTokenEmptyException e, RedirectAttributes redirectAttributes) {
        log.error(e.getMessage(), e);
        return GXResultUtils.error(HttpStatus.HTTP_INTERNAL_ERROR, e.getMessage());
    }

    @ExceptionHandler(SQLException.class)
    public GXResultUtils<String> handleSQLException(SQLException e, RedirectAttributes redirectAttributes) {
        log.error(e.getMessage(), e);
        return GXResultUtils.error(HttpStatus.HTTP_INTERNAL_ERROR, "查询语句异常");
    }

    @ExceptionHandler(SQLSyntaxErrorException.class)
    public GXResultUtils<String> handleSQLSyntaxErrorException(SQLSyntaxErrorException e, RedirectAttributes redirectAttributes) {
        log.error(e.getMessage(), e);
        return GXResultUtils.error(HttpStatus.HTTP_INTERNAL_ERROR, "查询语句异常");
    }

    @ExceptionHandler(GXDBNotExistsException.class)
    public GXResultUtils<String> handleDBNotExistsException(GXDBNotExistsException e, RedirectAttributes redirectAttributes) {
        log.error(e.getMessage(), e);
        return GXResultUtils.error(HttpStatus.HTTP_INTERNAL_ERROR, e.getMessage());
    }

    @ExceptionHandler(GXBusinessException.class)
    public GXResultUtils<Dict> handleBusinessException(GXBusinessException e) {
        log.error(e.getMessage(), e);
        return GXResultUtils.error(e.getCode(), e.getMsg(), e.getData());
    }
}
