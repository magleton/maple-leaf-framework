package com.geoxus.order.service.impl;

import com.geoxus.core.framework.validator.GXValidateExtDataService;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintValidatorContext;

@Service
public class MyGXValidateExtDataService implements GXValidateExtDataService {
    @Override
    public boolean validateExtData(Object o, String modelIdentification, String tableField, boolean isFullMatchAttribute, ConstraintValidatorContext context) throws UnsupportedOperationException {
        System.out.println("Hello ");
        return false;
    }
}
