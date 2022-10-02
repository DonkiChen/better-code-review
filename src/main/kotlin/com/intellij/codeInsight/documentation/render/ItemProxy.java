package com.intellij.codeInsight.documentation.render;

import com.intellij.openapi.util.TextRange;

public class ItemProxy {
    private final DocRenderPassFactory.Item item;
    public final TextRange textRange;

    public ItemProxy(DocRenderPassFactory.Item item) {
        this.item = item;
        this.textRange = item.textRange;
    }
}
