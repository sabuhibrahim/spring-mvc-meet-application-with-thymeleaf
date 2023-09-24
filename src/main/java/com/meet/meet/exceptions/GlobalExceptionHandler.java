package com.meet.meet.exceptions;

import java.util.Date;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handleNotFoundException(NotFoundException ex, HttpServletRequest req) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("exc", getObject(ex));
        mav.addObject("url", req.getRequestURL());
        mav.setViewName("error/404");
        return mav;
    }


    @ExceptionHandler(ForbiddenException.class)
    public ModelAndView handleNotAllowedException(ForbiddenException ex, HttpServletRequest req) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("exc", getObject(ex));
        mav.addObject("url", req.getRequestURL());
        mav.setViewName("error/403");
        return mav;
    }


    // @ExceptionHandler(Exception.class)
    // public ModelAndView handleException(Exception ex, HttpServletRequest req) {
    //     ModelAndView mav = new ModelAndView();
    //     mav.addObject("exc", getObject(ex));
    //     mav.addObject("url", req.getRequestURL());
    //     mav.setViewName("error/500");
    //     return mav;
    // }

    private ErrorObject getObject(Exception ex) {
        ErrorObject errorObject = new ErrorObject();
        errorObject.setMessage(ex.getMessage());
        errorObject.setTimestamp(new Date());
        return errorObject;
    }
    
}
