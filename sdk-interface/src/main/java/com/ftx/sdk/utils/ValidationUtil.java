package com.ftx.sdk.utils;

import com.google.common.collect.Maps;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotBlank;
import javax.validation.metadata.ConstraintDescriptor;
import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * 描述：验证信息工具
 *
 * @auth:xiaojun.yin
 * @createTime 2019-04-21 14:37
 */
public class ValidationUtil {
    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    public static <T> void validateClass(T t) {
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<T>> validateResult = validator.validate(t,  new Class[0]);
        LinkedHashMap errors = Maps.newLinkedHashMap();
        Iterator iterator = validateResult.iterator();
        while (iterator.hasNext()) {
            ConstraintViolation violation = (ConstraintViolation)iterator.next();
            ConstraintDescriptor cds =  violation.getConstraintDescriptor();
            Annotation annotation = cds.getAnnotation();
            System.out.println(annotation instanceof NotBlank);
            errors.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        System.out.println(errors);
    }
}
