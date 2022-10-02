package com.github.donkichen.bettercodereview

import com.github.donkichen.bettercodereview.bean.comment.CommentInfo
import com.github.donkichen.bettercodereview.ui.comment.MRCommentViewImpl
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys

class TestAction : AnAction() {
    private val demoCommentInfo = CommentInfo(
        "", "", "陈樊林", "", "hello world",
        CommentInfo.Coordination(0, 0), CommentInfo.Coordination(1, 0)
    )

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project!!
        val editor = e.getData(CommonDataKeys.EDITOR)!!

        val view = MRCommentViewImpl(project, editor)
        view.showComments(listOf(demoCommentInfo))
    }

    override fun update(e: AnActionEvent) {
        val project = e.project
        val editor = e.getData(CommonDataKeys.EDITOR)

        val enable = project?.isOpen == true && editor != null
        e.presentation.isEnabled = enable
    }
}