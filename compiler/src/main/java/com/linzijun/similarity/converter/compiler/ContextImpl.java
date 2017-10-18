package com.linzijun.similarity.converter.compiler;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * Created by zijun.lzj
 */

public class ContextImpl implements Context {

    public ContextImpl(ProcessingEnvironment processingEnvironment) {
        this.elementUtils = processingEnvironment.getElementUtils();
        this.filer = processingEnvironment.getFiler();
        this.typesUtils = processingEnvironment.getTypeUtils();
        this.messager = processingEnvironment.getMessager();

    }

    private final Elements elementUtils;

    @Override
    public Elements getElementUtils() {
        return elementUtils;
    }

    @Override
    public Filer getFiler() {
        return filer;
    }

    @Override
    public Types getTypesUtils() {
        return typesUtils;
    }

    @Override
    public Messager getMessager() {
        return messager;
    }

    private final Filer filer;

    private final Types typesUtils;
    private final Messager messager;

}
