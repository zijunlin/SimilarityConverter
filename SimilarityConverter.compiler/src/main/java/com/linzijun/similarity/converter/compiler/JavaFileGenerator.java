package com.linzijun.similarity.converter.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Arrays;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

/**
 * Generator a class java file
 * Created by zijun.lzj
 */

public class JavaFileGenerator {

    private final static String PACKAGE_NAME_FORMAT = "%s.converter";
    private final static String CONVERTER_NAME_FORMAT = "%s_%s_Converter";
    private Context context;
    private MethodGenerator methodGenerator;

    public JavaFileGenerator(Context context) {
        this.context = context;
        methodGenerator = new MethodGenerator(context);
    }

    public void generate(TypeElement sourceElement, TypeElement targetElement) throws IOException {
        ElementTree source = ElementTree.newInstance(context, sourceElement);
        ElementTree target = ElementTree.newInstance(context, targetElement);

        MethodSpec targetToSource = methodGenerator.buildConvertMethod(source, target);
        MethodSpec sourceToTarget = methodGenerator.buildConvertMethod(target, source);
        ClassName sourceName = ClassName.get(sourceElement);
        ClassName targetName = ClassName.get(targetElement);
        generateJavaFile(sourceName, targetName, targetToSource, sourceToTarget);
    }


    private void generateJavaFile(ClassName sourceName,
                                  ClassName targetName,
                                  MethodSpec... method) throws IOException {

        String className = String.format(CONVERTER_NAME_FORMAT, sourceName.simpleName(), targetName.simpleName());
        TypeSpec converterSpec = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethods(Arrays.asList(method))
                .build();
        String packageName =String.format(PACKAGE_NAME_FORMAT,sourceName.packageName());
        JavaFile javaFile = JavaFile.builder(packageName, converterSpec)
                .build();

        javaFile.writeTo(context.getFiler());
    }
}
