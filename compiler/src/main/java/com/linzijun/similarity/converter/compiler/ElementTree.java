package com.linzijun.similarity.converter.compiler;

import com.google.auto.common.MoreElements;
import com.google.common.base.Strings;
import com.linzijun.similarity.converter.annotation.Alias;
import com.linzijun.similarity.converter.annotation.IgnoreConverter;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

/**
 * Created by zijun.lzj on
 */

public class ElementTree {

    public static ElementTree newInstance(Context context, TypeElement root) {
        ElementTree tree = new ElementTree(context, root);
        tree.setSimilarName(new SimilarNameImpl());
        return tree;
    }


    private List<Element> settableFields;
    private List<Element> readableFields;
    private List<Element> setters;
    private List<Element> getters;
    private final Context context;
    private final TypeElement root;


    public ElementTree(Context context, TypeElement root) {
        this.context = context;
        this.root = root;
        build();
    }

    public void setSimilarName(SimilarName similarName) {
        this.similarName = similarName;
    }

    private SimilarName similarName;

    private void build() {

        settableFields = allMembers()
                .filter(e -> e.getKind() == ElementKind.FIELD)
                .filter(this::isSettable)
                .collect(Collectors.toList());

        readableFields = allMembers()
                .filter(e -> e.getKind() == ElementKind.FIELD)
                .filter(this::isReadable)
                .collect(Collectors.toList());

        getters = propertyMap(allMembers(), this::isGetterMethod);
        setters = propertyMap(allMembers(), this::isSetterMethod);
    }

    private Stream<? extends Element> allMembers() {
        return context.getElementUtils()
                .getAllMembers(root)
                .stream()
                .filter(e -> !isIgnore(e));
    }

    private List<Element> propertyMap(Stream<? extends Element> allMoment,
                                      Predicate<? super Element> predicate) {
        return allMoment
                .filter(e -> e.getKind() == ElementKind.METHOD)
                .filter(e -> e.getModifiers().contains(Modifier.PUBLIC))
                .filter(predicate)
                .collect(Collectors.toList());
    }


    private boolean isIgnore(Element e) {
        return MoreElements.getAnnotationMirror(e, IgnoreConverter.class).isPresent();
    }

    private boolean isSettable(Element element) {
        return element.getModifiers()
                .stream()
                .anyMatch(m -> Modifier.PUBLIC.equals(m)
                        && !Modifier.FINAL.equals(m)
                        && !Modifier.NATIVE.equals(m));
    }

    private boolean isReadable(Element element) {
        return element.getModifiers()
                .stream()
                .anyMatch(Modifier.PUBLIC::equals);
    }

    private boolean isGetterMethod(Element element) {
        return element.getSimpleName().toString().startsWith("get") ||
                element.getSimpleName().toString().startsWith("is");
    }

    private boolean isSetterMethod(Element element) {
        return element.getSimpleName().toString().startsWith("set");
    }


    public List<Element> getSettableFields() {
        return settableFields;
    }


    public List<Element> getSetters() {
        return setters;
    }

    public Optional<Element> findSimilarGetter(Element source) {
        return getters.stream()
                .filter(e -> isSimilarity(source, e))
                .findFirst();
    }


    public Optional<Element> findSimilarField(Element source) {
        return readableFields.stream()
                .filter(e -> isSimilarity(source, e))
                .findFirst();
    }

    public boolean containSimilarFieldOrGetter(Element element) {
        return findSimilarField(element).isPresent() ||
                findSimilarGetter(element).isPresent();
    }


    private boolean isSimilarity(Element source, Element target) {

        String sourceName = source.getSimpleName().toString();
        String targetName = target.getSimpleName().toString();

        Alias sourceAlias = source.getAnnotation(Alias.class);
        String sourceAliasName = sourceAlias == null ? "" : sourceAlias.value();

        Alias targetAlias = target.getAnnotation(Alias.class);
        String targetAliasName = targetAlias == null ? "" : targetAlias.value();

        if (similarName == null) {
            return false;
        }
        if (similarName.isSimilarity(sourceName, targetName)) {
            return true;
        } else if (similarName.isSimilarity(sourceName, targetAliasName)) {
            return true;
        } else if (similarName.isSimilarity(sourceAliasName, targetName)) {
            return true;
        } else if (similarName.isSimilarity(sourceAliasName, targetAliasName) &&
                !Strings.isNullOrEmpty(sourceAliasName)) {
            return true;
        }

        return false;
    }


    public TypeElement getRoot() {
        return root;
    }
}
