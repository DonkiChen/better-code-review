package com.intellij.codeInsight.documentation.render

import com.github.donkichen.bettercodereview.CommentManager
import com.intellij.codeInsight.documentation.render.DocRenderPassFactory.Items
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFile

/**
 * 由于 [DocRenderPassFactory.Item] 是 default 权限, 所以将相关的方法放到同包名下文件
 */
object ItemsToRenderCalculator {
    private val itemConstructor by lazy {
        val constructor =
            DocRenderPassFactory.Item::class.java.getDeclaredConstructor(TextRange::class.java, String::class.java)
        constructor.isAccessible = true
        return@lazy constructor
    }

    fun calculateItems(editor: Editor, file: PsiFile): Items {
        val items = CommentItems()
        CommentManager.from(file).collectElements { range, textToRender ->
            items.addItem(newItem(range, textToRender))
        }
        return items
    }

    private fun newItem(range: TextRange, textToRender: String): DocRenderPassFactory.Item {
        return itemConstructor.newInstance(range, textToRender)
    }
}