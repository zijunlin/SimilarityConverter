package com.linzijun.similarity.converter.compiler;

import com.google.auto.service.AutoService;
import com.linzijun.similarity.converter.annotation.AutoConverter;

import java.util.Optional;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
@SupportedAnnotationTypes({
        "com.linzijun.similarity.converter.annotation.AutoConverter",
        "com.linzijun.similarity.converter.annotation.ConverterMethod"
})
public class ConverterApt extends AbstractProcessor {


    private Context context;
    private JavaFileGenerator generator;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        context = new ContextImpl(processingEnvironment);
        generator = new JavaFileGenerator(context);
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        roundEnvironment.getElementsAnnotatedWith(AutoConverter.class)
                .stream()
                .filter(element -> element.getKind() == ElementKind.CLASS)
                .filter(element -> AnnotationUtil.isAnnotationPresent(element, AutoConverter.class))
                .map(element -> (TypeElement) element)
                .forEach(this::analysisAnnotated);
        return false;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }


    private void analysisAnnotated(TypeElement sourceElement) {
        try {
            Optional<TypeElement> targetElement = AnnotationUtil
                    .getAnnotationValueType(sourceElement, AutoConverter.class);
            if (!targetElement.isPresent()) {
                return;
            }
            generator.generate(sourceElement, targetElement.get());
        } catch (Throwable e) {
            context.getMessager().printMessage(Diagnostic.Kind.ERROR,
                    "analysisAnnotated error"+e.toString(),
                    sourceElement);
        }
    }


}
