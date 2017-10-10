package com.linzijun.similarity.converter.compiler;

/**
 * Created by zijun.lzj on
 */

public class SimilarNameImpl implements SimilarName {
    @Override
    public boolean isSimilarity(String source, String target) {
        source = removePrefix(source);
        target = removePrefix(target);
        String sName = source.replaceAll("_", "").toUpperCase().trim();
        String sTarget = target.replaceAll("_", "").toUpperCase().trim();
        return sName.equals(sTarget);
    }


    private String removePrefix(String name) {
        if (name.startsWith("set")) {
            name = name.replace("set", "");
        } else if (name.startsWith("get")) {
            name = name.replace("get", "");
        } else if (name.startsWith("is")) {
            name = name.replace("is", "");
        }
        return name;
    }
}
