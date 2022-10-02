package com.github.donkichen.bettercodereview.ui.comment.render

import com.intellij.ide.ui.UISettings
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorCustomElementRenderer
import com.intellij.openapi.editor.Inlay
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.openapi.util.Key
import com.intellij.ui.AppUIUtil
import java.awt.Graphics
import java.awt.Rectangle
import javax.swing.JEditorPane
import kotlin.math.max
import kotlin.math.min

private const val MAX_WIDTH = 680
private val CACHED_LOADING_PANE = Key.create<JEditorPane>("comment_loading_pane")

/**
 * 大部分代码来自 [com.intellij.codeInsight.documentation.render.DocRenderer]
 */
class CommentRender(val item: CommentItem) : EditorCustomElementRenderer {

    private var cachedWidth = -1
    private var cachedHeight = -1

    private var component: JEditorPane? = null
    private var componentNeedUpdate = false


    companion object {
        private fun scale(value: Int): Int {
            return (value * UISettings.defFontScale).toInt()
        }

        fun calcInlayWidth(editor: Editor): Int {
            val availableWidth = editor.scrollingModel.visibleArea.width
            return if (availableWidth <= 0) {
                MAX_WIDTH
            } else {
                max(MAX_WIDTH, min(scale(MAX_WIDTH), availableWidth))
            }
        }
    }

    override fun calcWidthInPixels(inlay: Inlay<*>): Int {
        if (cachedWidth < 0) {
            cachedWidth = calcInlayWidth(inlay.editor)
        }
        return cachedWidth
    }

    override fun calcHeightInPixels(inlay: Inlay<*>): Int {
        if (cachedHeight < 0) {
            cachedHeight = 0
        }
        return cachedHeight
    }

    override fun paint(inlay: Inlay<*>, g: Graphics, targetRegion: Rectangle, textAttributes: TextAttributes) {

    }

    private fun findComponent(inlay: Inlay<*>, width: Int): JEditorPane {
        val editor = inlay.editor
        var newInstance = false
        var pane = component
        if (pane == null || componentNeedUpdate) {
            component = null
            componentNeedUpdate = false
            newInstance = true
            val message = item.message
            pane = if (message.isNullOrBlank()) {
                findLoadingComponent(inlay.editor)
            } else {
                createComponent(editor, message, false)
            }
        }

        AppUIUtil.targetToDevice(pane, editor.contentComponent)
        pane.setSize(width, 10_000_000)
        if (newInstance) {
            pane.preferredSize
        }
        return pane
    }


    private fun findLoadingComponent(editor: Editor): JEditorPane {
        var pane = editor.getUserData(CACHED_LOADING_PANE)
        if (pane == null) {
            pane = createComponent(editor, "loading...", true)
            editor.putUserData(CACHED_LOADING_PANE, pane)
        }
        return pane
    }

    private fun createComponent(editor: Editor, message: String, reusable: Boolean): JEditorPane {
        return CommentComponentFactory.newComponent(editor, message, reusable)
    }

    data class CommentItem(var message: String?)
}
