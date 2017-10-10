package com.linzijun.similarity.converter.compiler;


import com.google.auto.common.MoreElements;
import com.google.auto.common.MoreTypes;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

/**
 * Created by zijun.lzj on
 */

public class AnnotationUtil {
    public static final String DEFAULT_NAME = "value";

    public static boolean hasAnnotation(Element element, Class<? extends Annotation> annotationClass) {
        return MoreElements.getAnnotationMirror(element, annotationClass).isPresent();
    }

    public static Optional<TypeElement> getAnnotationValueType(Element element, Class<? extends Annotation> annotationClass) {
        return getAnnotationValueType(element, annotationClass, DEFAULT_NAME);
    }


    public static Optional<TypeElement> getAnnotationValueType(Element element, Class<? extends Annotation> annotationClass, String annotationValueName) {
        com.google.common.base.Optional<AnnotationMirror> mirror = MoreElements
                .getAnnotationMirror(element, annotationClass);
        if (mirror.isPresent()) {
            return getAnnotationValue(mirror.get(), annotationValueName)
                    .map(AnnotationValue::getValue)
                    .map(v -> MoreTypes.asDeclared((TypeMirror) v).asElement())
                    .map(MoreElements::asType);
        }
        return Optional.empty();
    }


    public static Optional<? extends AnnotationValue> getAnnotationValue(AnnotationMirror mirror) {
        return getAnnotationValue(mirror, DEFAULT_NAME);
    }

    public static Optional<? extends AnnotationValue> getAnnotationValue(AnnotationMirror mirror, String name) {
        return getAnnotationValues(mirror, name)
                .findFirst();
    }


    public static Stream<? extends AnnotationValue> getAnnotationValues(AnnotationMirror mirror, String name) {
        return mirror
                .getElementValues()
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey().getSimpleName().toString().equals(name))
                .map(Map.Entry::getValue);
    }

    public static boolean isAnnotationPresent(Element element, Class<? extends Annotation> annotationClass) {
        return MoreElements.isAnnotationPresent(element, annotationClass);
    }
}
