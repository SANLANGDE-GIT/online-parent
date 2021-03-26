package com.atguigu.guli.service.base.handler;

import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.base.result.R;
import com.atguigu.guli.service.base.result.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.ibatis.binding.BindingException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.sql.SQLException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {



    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public R error(HttpRequestMethodNotSupportedException e){
        log.error(ExceptionUtils.getStackTrace(e));
        return R.error().message(e.getMessage());
    }

    @ExceptionHandler(java.sql.SQLException.class)
    @ResponseBody
    public R error(SQLException e){
        log.error(ExceptionUtils.getStackTrace(e));
        return R.error().message(e.getMessage());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseBody
    public R error(MaxUploadSizeExceededException e){
        log.error(ExceptionUtils.getStackTrace(e));
        return R.error().message("上传文件过大！");
    }

    @ExceptionHandler(GuliException.class)
    @ResponseBody
    public R error(GuliException e){
        log.error(ExceptionUtils.getStackTrace(e));
        return R.error().message(e.getMessage()).code(e.getCode());
    }

    @ResponseBody
    @ExceptionHandler(BindingException.class)
    public R error(BindingException e){
        log.error(ExceptionUtils.getStackTrace(e));
        return R.error();
    }

    /**
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public R error(Exception e){
        log.error(ExceptionUtils.getStackTrace(e));
        return R.error();
    }

    /**
     * SQL语法错误
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(BadSqlGrammarException.class)
    public R error(BadSqlGrammarException e){
        log.error(ExceptionUtils.getStackTrace(e));
        return R.setResult(ResultCodeEnum.BAD_SQL_GRAMMAR);
    }

    /**
     * json 解析异常
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public R error(HttpMessageNotReadableException e){
        //e.printStackTrace();
        log.error(ExceptionUtils.getStackTrace(e));
        return R.setResult(ResultCodeEnum.JSON_PARSE_ERROR);
    }

    /**
     * 类型转换错误
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(ClassCastException.class)
    public R error(ClassCastException e){
        log.error(ExceptionUtils.getStackTrace(e));
        return R.setResult(ResultCodeEnum.CLASS_CAST_EXCEPTION);
    }

}
