package cn.notemi.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.FeatureDescriptor;
import java.util.stream.Stream;

public class ObjectUtils {
    public static <T> void mergeChanges(T source, T target) {
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    public static <T> void mergeChanges(T source, T target, String[] excludeProperties) {
        if (excludeProperties == null || excludeProperties.length == 0) {
            mergeChanges(source, target);
            return;
        }

        String[] excluded = Stream
                .concat(Stream.of(getNullPropertyNames(source)),
                        Stream.of(excludeProperties))
                .toArray(String[]::new);
        BeanUtils.copyProperties(source, target, excluded);
    }

    private static <T> String[] getNullPropertyNames(T source) {
        BeanWrapper wrappedSource = new BeanWrapperImpl(source);
        return Stream.of(wrappedSource.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(propertyName -> wrappedSource.getPropertyValue(propertyName) == null)
                .toArray(String[]::new);
    }
}

