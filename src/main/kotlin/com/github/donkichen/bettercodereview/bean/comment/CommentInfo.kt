package com.github.donkichen.bettercodereview.bean.comment

data class CommentInfo(
    /**
     * 代码块 id
     */
    val codeBlockId: String,
    val commentId: String,
    val authorName: String,
    val authorId: String,
    val comment: String,
    val start: Coordination,
    val end: Coordination
) {
    data class Coordination(val row: Int, val column: Int)
}