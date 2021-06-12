package ru.study.shop.adapters.controllers.utils.dto_validation.constraint_impl;

import ru.study.shop.adapters.controllers.utils.dto_validation.custom_constraints.NotEmptyObject;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class NotEmptyObjectValidator implements ConstraintValidator<NotEmptyObject, Object> {
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (isNull(value)) {
            return true;
        }

        Field[] fields = value.getClass().getDeclaredFields();

        for (Field field : fields) {
            String fieldName = field.getName();

            try {
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, value.getClass());
                Object propertyValue = propertyDescriptor.getReadMethod().invoke(value);

                if (nonNull(propertyValue)) {
                    return true;
                }
            } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                return false;
            }
        }

        return false;
    }
}
