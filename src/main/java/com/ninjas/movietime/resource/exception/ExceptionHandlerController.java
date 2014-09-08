package com.ninjas.movietime.resource.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ninjas.movietime.core.util.ExceptionManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @author ayassinov on 05/09/2014.
 */
@Slf4j
@ControllerAdvice
public class ExceptionHandlerController {

    @Autowired
    private ObjectMapper mapper;

    @ExceptionHandler(value = {Exception.class, RuntimeException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String defaultErrorHandler(HttpServletRequest request, Exception ex) {
        final String errorCode = UUID.randomUUID().toString();
        ExceptionManager.log(ex, "Exception on request=%s code=%s", request.getServletPath(), errorCode);
        final ObjectNode node = mapper.createObjectNode();
        node.put("code", errorCode);
        node.put("class", ex.getClass().getCanonicalName());
        return node.toString();
    }
}
