package com.github.donkichen.bettercodereview

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFile

class CommentElementProvider(private val file: PsiFile) {
    fun collectElements(action: (range: TextRange, textToRender: String) -> Unit) {

    }
}