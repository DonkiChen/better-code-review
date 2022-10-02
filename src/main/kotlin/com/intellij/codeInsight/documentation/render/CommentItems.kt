package com.intellij.codeInsight.documentation.render

import com.intellij.codeInsight.documentation.render.DocRenderPassFactory.Item
import com.intellij.codeInsight.documentation.render.DocRenderPassFactory.Items

class CommentItems : Items() {
    private companion object {
        private val addItemMethod by lazy {
            val method = Items::class.java.getDeclaredMethod("addItem", Item::class.java)
            method.isAccessible = true
            return@lazy method
        }
    }

    internal fun addItem(item: Item) {
        addItemMethod.invoke(this, item)
    }
}