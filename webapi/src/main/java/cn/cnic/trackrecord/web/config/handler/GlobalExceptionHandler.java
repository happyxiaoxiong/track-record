package cn.cnic.trackrecord.web.config.handler;

import cn.cnic.trackrecord.common.http.HttpRes;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

    @ExceptionHandler(value = DataAccessException.class)
    public HttpRes<String> dataAccessExceptionHandler(HttpServletRequest req, DataAccessException e) throws Exception {
        return HttpRes.fail(e.getCause().getMessage(), null);
    }
}
