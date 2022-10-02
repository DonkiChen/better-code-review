package com.github.donkichen.bettercodereview.ui.comment

import com.github.donkichen.bettercodereview.bean.comment.CommentInfo
import com.intellij.openapi.editor.*
import com.intellij.openapi.editor.markup.HighlighterLayer
import com.intellij.openapi.editor.markup.HighlighterTargetArea
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.openapi.project.Project
import java.awt.Color
import java.awt.Graphics
import java.awt.Rectangle
import javax.swing.JEditorPane

class MRCommentViewImpl(
    private val project: Project,
    private val editor: Editor
) : IMRCommentView {


    override fun showComments(comments: List<CommentInfo>) {
        comments.forEach {
            val message = "${it.authorName}: ${it.comment}"
            val start = LogicalPosition(it.start.row, it.start.column)
            val end = LogicalPosition(it.end.row, it.end.column)
//            showCommentByMarkup(message, start, end)
            showCommentByInlay(message, start, end)
        }
    }

    private fun showCommentByInlay(message: String, start: LogicalPosition, end: LogicalPosition) {
        val model = editor.inlayModel
        val startOffset = editor.logicalPositionToOffset(start)
        val endOffset = editor.logicalPositionToOffset(end)
        model.addBlockElement(endOffset, true, true, 1, object : EditorCustomElementRenderer {
            override fun calcWidthInPixels(inlay: Inlay<*>): Int {
                return editor.scrollingModel.visibleArea.width
            }

            override fun paint(inlay: Inlay<*>, g: Graphics, targetRegion: Rectangle, textAttributes: TextAttributes) {
                g.color = Color.WHITE
                val startX = editor.insets.left
                val endX = startX + targetRegion.width

                val pane = JEditorPane()
                pane.isEditable = false
                pane.text = message
                pane.foreground = Color.CYAN
                pane.selectedTextColor = Color.CYAN
                pane.font = pane.font.deriveFont(14F)
                pane.setSize(
                    targetRegion.width,
                    10000000 /* Arbitrary large value, that doesn't lead to overflows and precision loss */
                )

                val dg = g.create(startX, targetRegion.y, targetRegion.width, targetRegion.height + 100)
                pane.paint(dg)
                dg.dispose()
            }
        })
    }

    private fun showCommentByMarkup(message: String, start: LogicalPosition, end: LogicalPosition) {
        val model = editor.markupModel
        val startOffset = editor.logicalPositionToOffset(start)
        val endOffset = editor.logicalPositionToOffset(end)
        val textAttributeKey = DefaultLanguageHighlighterColors.MARKUP_ATTRIBUTE

        model.addRangeHighlighter(
            textAttributeKey,
            startOffset,
            endOffset,
            HighlighterLayer.ERROR,
            HighlighterTargetArea.EXACT_RANGE
        )

    }
}