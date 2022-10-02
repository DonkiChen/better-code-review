package com.github.donkichen.bettercodereview.ui.comment.render

import com.intellij.codeHighlighting.EditorBoundHighlightingPass
import com.intellij.codeHighlighting.TextEditorHighlightingPass
import com.intellij.codeInsight.documentation.render.DocRenderDummyLineMarkerProvider
import com.intellij.codeInsight.documentation.render.DocRenderItem
import com.intellij.codeInsight.documentation.render.DocRenderPassFactory
import com.intellij.codeInsight.documentation.render.ItemsToRenderCalculator
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiModificationTracker

class CommentRenderPassFactory : DocRenderPassFactory() {

    companion object {
        private val MODIFICATION_STAMP = Key.create<Long>("mr.comment.render.modification.stamp")
        private val RESET_TO_DEFAULT = Key.create<Boolean>("mr.comment.render.reset.to.default")
        private val ICONS_ENABLED = Key.create<Boolean>("mr.comment.render.icons.enabled")

        fun applyItemsToRender(
            editor: Editor,
            project: Project,
            items: Items,
            collapseNewRegions: Boolean
        ) {
            editor.putUserData(MODIFICATION_STAMP, PsiModificationTracker.SERVICE.getInstance(project).modificationCount)
            editor.putUserData(ICONS_ENABLED, true)
            DocRenderItem.setItemsToEditor(editor, items, collapseNewRegions)
        }

    }

    override fun createHighlightingPass(file: PsiFile, editor: Editor): TextEditorHighlightingPass? {
        editor.project ?: return null
        return CommentRenderPass(editor, file)
    }

    private class CommentRenderPass(editor: Editor, psiFile: PsiFile) :
        EditorBoundHighlightingPass(editor, psiFile, false), DumbAware {

        private lateinit var items: Items

        override fun doCollectInformation(progress: ProgressIndicator) {
            items = ItemsToRenderCalculator.calculateItems(myEditor, myFile)
        }

        override fun doApplyInformationToEditor() {
            applyItemsToRender(myEditor, myProject, items, false)
        }
    }
}