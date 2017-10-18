package com.linzijun.similarity.converter.compiler;

import com.google.auto.common.MoreElements;
import com.linzijun.similarity.converter.annotation.ConverterMethod;
import com.linzijun.similarity.converter.annotation.CustomConverter;

import java.util.Arrays;
import java.util.Optional;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

/**
 * Created by zijun.lzj
 */

public class CustomConverterHandler {
    private Context context;

    public CustomConverterHandler(Context context) {
        this.context = context;
    }

    public Optional<TypeElement> getCustomConverter(Element source, Element target) {

        Optional<Element> annotation = findCustomConverterElement(source, target);
        return annotation.flatMap(element -> AnnotationUtil
                .getAnnotationValueType(element, CustomConverter.class));
    }

    private Optional<Element> findCustomConverterElement(Element... elements) {
        return Arrays.stream(elements)
                .filter(e -> AnnotationUtil.hasAnnotation(e, CustomConverter.class))
                .findFirst();
    }


    public Optional<Name> findConverterMethod(Element source, TypeElement converter) {
        Optional<TypeMirror> returnType = Optional.empty();
        if (source.getKind() == ElementKind.FIELD) {
            returnType =Optional.of(source.asType());
        } else if (source.getKind() == ElementKind.METHOD) {
            returnType = MoreElements.asExecutable(source).getParameters()
                    .stream()
                    .map(Element::asType)
                    .findFirst();
        }

        if (!returnType.isPresent()) {
            return Optional.empty();
        }

        final TypeMirror finalReturnType = returnType.get();
        return converter.getEnclosedElements()
                .stream()
                .filter(element -> element.getKind() == ElementKind.METHOD)
                .filter(element -> AnnotationUtil.hasAnnotation(element, ConverterMethod.class))
                .map(MoreElements::asExecutable)
                .filter(method -> context.getTypesUtils().isAssignable(method.getReturnType(), finalReturnType))
                .map(ExecutableElement::getSimpleName)
                .findFirst();
    }

}
