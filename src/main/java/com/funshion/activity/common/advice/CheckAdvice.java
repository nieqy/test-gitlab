package com.funshion.activity.common.advice;

import com.funshion.activity.common.constants.Result;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice(annotations = RestController.class)
public class CheckAdvice {

    /**
     * 请求的 JSON 参数在请求体内的参数校验
     *
     * @param e 异常信息
     * @return 返回数据
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<?> handleBindException1(ConstraintViolationException e) {
        try {
            List<ConstraintViolation<?>> cvList = new ArrayList<ConstraintViolation<?>>(e.getConstraintViolations());
            return Result.getFailureResult("502", cvList.get(0).getMessage());
        } catch (Exception ex) {
            return Result.HTTP_PARAMS_NOT_ENOUGH;
        }
    }


    @ExceptionHandler(BindException.class)
    public Result<?> handleBindException(BindException e) {
        try {
            return Result.getFailureResult("502", e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        } catch (Exception ex) {
            return Result.HTTP_PARAMS_NOT_ENOUGH;
        }
    }

}