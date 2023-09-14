package com.bol.mancala.controller;

import com.bol.mancala.dto.ErrorInfo;
import com.bol.mancala.service.exception.ForbiddenException;
import com.bol.mancala.service.exception.GameLogicException;
import com.bol.mancala.service.exception.GameNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Slf4j
public class GlobalControllerAdvice {

    private static final String STATUS = "status";
    private static final String MESSAGE = "message";
    private static final String ERROR_VIEW = "error";

    @ExceptionHandler(GameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleNotFoundError(Exception ex) {
        ModelAndView mav = new ModelAndView(ERROR_VIEW);
        mav.addObject(MESSAGE, ex.getMessage());
        mav.addObject(STATUS, HttpStatus.NOT_FOUND);
        return mav;
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ModelAndView handleForbiddenError(Exception ex) {
        ModelAndView mav = new ModelAndView(ERROR_VIEW);
        mav.addObject(MESSAGE, ex.getMessage());
        mav.addObject(STATUS, HttpStatus.FORBIDDEN);
        return mav;
    }

    @ExceptionHandler({GameLogicException.class, IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorInfo handleLogicError(HttpServletRequest req, Exception ex) {
        return new ErrorInfo(req.getRequestURL().toString(), ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleError(HttpServletRequest req, Exception ex) {
        log.error("Request: " + req.getRequestURL() + " raised " + ex);

        ModelAndView mav = new ModelAndView(ERROR_VIEW);
        mav.addObject(MESSAGE, ex.getMessage());
        mav.addObject(STATUS, HttpStatus.INTERNAL_SERVER_ERROR);
        return mav;
    }
}
