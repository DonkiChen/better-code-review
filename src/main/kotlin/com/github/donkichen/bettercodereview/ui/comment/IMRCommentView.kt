package com.github.donkichen.bettercodereview.ui.comment

import com.github.donkichen.bettercodereview.bean.comment.CommentInfo

interface IMRCommentView {
    fun showComments(comments: List<CommentInfo>)
}