package com.linzijun.similarity.converter.compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import java.util.Optional;
import java.util.stream.Stream;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;

/**
 * Generate the convert Method
 * Created by zijun.lzj on
 */

public class MethodGenerator {

    private static final String METHOD_NAME_FORMAT = "parseTo%s";
    private static final String INPUT_PARAMETER = "target";
    private static final String NEW_INSTANCE_STATEMENT = "$T source=new $T()";
    private static final String FIELD_FROM_FIELD_STATEMENT = "source.$N=target.$N";
    private static final String FIELD_FROM_FIELD_CUSTOM_STATEMENT = "source.$N=$T.$N(target.$N)";
    private static final String FIELD_FROM_GETTER_STATEMENT = "source.$N=target.$N()";
    private static final String FIELD_FROM_GETTER_CUSTOM_STATEMENT = "source.$N=$T.$N(target.$N())";
    private static final String SETTER_FROM_FIELD_STATEMENT = "source.$N(target.$N)";
    private static final String SETTER_FROM_FIELD_CUSTOM_STATEMENT = "source.$N($T.$N(target.$N))";
    private static final String SETTER_FROM_GETTER_STATEMENT = "source.$N(target.$N())";
    private static final String SETTER_FROM_GETTER_CUSTOM_STATEMENT = "source.$N($T.$N(target.$N()))";
    private static final String RETURN_STATEMENT = "return source";


    private ElementTree sourceTree;
    private ElementTree targetTree;
    private MethodSpec.Builder builder;

    private CustomConverterHandler customHandler;

    public MethodGenerator(Context context) {
        customHandler = new CustomConverterHandler(context);
    }


    public MethodSpec buildConvertMethod(ElementTree sourceTree, ElementTree targetTree) {
        this.sourceTree = sourceTree;
        this.targetTree = targetTree;
        builder = buildMethodDeclare();
        buildMethodBody();
        return builder.build();
    }

    private MethodSpec.Builder buildMethodDeclare() {
        ClassName sourceName = ClassName.get(sourceTree.getRoot());
        String methodName = String.format(METHOD_NAME_FORMAT, sourceName.simpleName());
        return MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                .returns(TypeName.get(sourceTree.getRoot().asType()))
                .addParameter(TypeName.get(targetTree.getRoot().asType()), INPUT_PARAMETER);

    }

    private void buildMethodBody() {
        ClassName sourceName = ClassName.get(sourceTree.getRoot());
        builder.addStatement(NEW_INSTANCE_STATEMENT, sourceName, sourceName);
        addAssignmentExpression(sourceTree.getSettableFields().stream(),
                FIELD_FROM_FIELD_STATEMENT,
                FIELD_FROM_FIELD_CUSTOM_STATEMENT,
                FIELD_FROM_GETTER_STATEMENT,
                FIELD_FROM_GETTER_CUSTOM_STATEMENT);
        addAssignmentExpression(sourceTree.getSetters().stream(),
                SETTER_FROM_FIELD_STATEMENT,
                SETTER_FROM_FIELD_CUSTOM_STATEMENT,
                SETTER_FROM_GETTER_STATEMENT,
                SETTER_FROM_GETTER_CUSTOM_STATEMENT);
        builder.addStatement(RETURN_STATEMENT);
    }


    private void addAssignmentExpression(Stream<Element> stream,
                                         String fieldStatement,
                                         String fieldCustomStatement,
                                         String getterStatement,
                                         String getterCustomStatement) {
        stream
                .filter(targetTree::containSimilarFieldOrGetter)
                .forEach(element -> {
                    Optional<Element> field = targetTree.findSimilarField(element);
                    Optional<Element> getter = targetTree.findSimilarGetter(element);
                    if (field.isPresent()) {
                        addStatement(element, field.get(), fieldCustomStatement, fieldStatement);
                    } else {
                        getter.ifPresent(target -> addStatement(element, target, getterCustomStatement, getterStatement));
                    }
                });
    }


    private void addStatement(Element source,
                              Element target,
                              String customStatement,
                              String defaultStatement) {
        Name name = source.getSimpleName();
        Optional<TypeElement> convert = customHandler.getCustomConverter(source, target);
        if (convert.isPresent()) {
            ClassName convertClassName = ClassName.get(convert.get());
            Optional<Name> convertMethod = customHandler.findConverterMethod(source, convert.get());
            convertMethod.ifPresent(value -> builder.addStatement(customStatement, name, convertClassName, value, target.getSimpleName()));
        } else {
            builder.addStatement(defaultStatement, name, target.getSimpleName());
        }
    }

}
