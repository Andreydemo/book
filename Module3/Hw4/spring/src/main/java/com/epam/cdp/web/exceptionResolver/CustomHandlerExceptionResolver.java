package com.epam.cdp.web.exceptionResolver;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handles all the exceptions of the application.
 */
@Component
public class CustomHandlerExceptionResolver implements HandlerExceptionResolver {
    private static final Logger logger = Logger.getLogger(CustomHandlerExceptionResolver.class);

    @Override
    public ModelAndView resolveException(
            HttpServletRequest request, HttpServletResponse response,
            Object handler, Exception exception) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        ModelAndView modelAndView = new ModelAndView();
        logger.error("Exception occurred with message: " + exception.getMessage());
        modelAndView.addObject("message", exception.getMessage());
        modelAndView.setViewName("error");
        return modelAndView;
    }
}