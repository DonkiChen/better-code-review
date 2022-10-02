package com.github.donkichen.bettercodereview

import com.intellij.psi.PsiFile

object CommentManager {
    fun from(file: PsiFile): CommentElementProvider {
        return CommentElementProvider(file)
    }
}