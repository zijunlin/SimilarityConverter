package com.linzijun.similarity.converter.compiler;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/**
 * Created by zijun.lzj on 2017/9/30.
 */

interface Context {
    Elements getElementUtils();

    Filer getFiler();

    Types getTypesUtils();

    Messager getMessager();
}
