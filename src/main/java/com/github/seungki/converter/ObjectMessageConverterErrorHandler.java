package com.github.seungki.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ErrorHandler;

public class ObjectMessageConverterErrorHandler implements ErrorHandler {

    private static final Logger logger = LoggerFactory.getLogger(ObjectMessageConverterErrorHandler.class);

    @Override
    public void handleError(Throwable throwable) {
        logger.error(throwable.getMessage());
    }
}
