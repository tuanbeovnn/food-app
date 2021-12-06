package com.foodapp.backend.utils;

import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class FormUtil {

    /**
     * toModel
     *
     * @param clazz
     * @param model
     * @param <T>
     * @return
     */
    public static <T> T toModel(Class<T> clazz, Map<String , String> model) {
        T object = null;
        try {
            object = clazz.newInstance();
            BeanUtils.populate(object, model);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            System.out.print(e.getMessage());
        }
        return object;
    }

}
