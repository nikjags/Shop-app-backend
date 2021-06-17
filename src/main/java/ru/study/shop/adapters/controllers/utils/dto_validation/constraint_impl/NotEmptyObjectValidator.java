package ru.study.shop.adapters.controllers.utils.dto_validation.constraint_impl;

import ru.study.shop.adapters.controllers.utils.dto_validation.custom_constraints.NotEmptyObject;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class NotEmptyObjectValidator implements ConstraintValidator<NotEmptyObject, Object> {
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (isNull(value)) {
            return true;
        }

        List<Field> nonSFFields = getNonStaticNonFinalFields(value.getClass().getDeclaredFields());

        for (Field field : nonSFFields) {
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

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private List<Field> getNonStaticNonFinalFields(Field[] fields) {
        return Arrays.stream(fields).filter(field -> {
            int modifiers = field.getModifiers();
            return Modifier.isFinal(modifiers) || Modifier.isStatic(modifiers);
        }).collect(Collectors.toList());
    }
}
