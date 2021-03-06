package com.foodapp.backend.utils;

import com.foodapp.backend.annotation.ValidateCustom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtils {
    private static final Logger logger = LoggerFactory.getLogger(ValidationUtils.class);

    /**
     * validateRequired
     *
     * @param object
     * @return
     */
    public static Map<String, Object> validateRequired(Object object) {
        Map<String, Object> result = new HashMap<>();
        try {
            Field[] fields = object.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(ValidateCustom.class) && (field.get(object) == null || field.get(object).toString().equals(""))) {
                    result.put(field.getName(), field.getAnnotation(ValidateCustom.class).message());
                }
                if (field.get(object) != null && (field.get(object).getClass().equals(ArrayList.class) || field.get(object).getClass().equals(List.class))) {
                    if (field.isAnnotationPresent(ValidateCustom.class) && ((ArrayList) field.get(object)).isEmpty()) {
                        result.put(field.getName(), field.getAnnotation(ValidateCustom.class).message());
                    }
                }
            }
        } catch (IllegalAccessException e) {
            logger.info("The field is required {}", e.getMessage());
        }
        return result;
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[a-z][a-z0-9_\\.]{5,32}@[a-z0-9]{2,}(\\.[a-z0-9]{2,4}){1,2}$", Pattern.CASE_INSENSITIVE);

    /**
     * validate format string email
     *
     * @param emailStr : email string using validate
     * @return boolean
     */
    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }
}
