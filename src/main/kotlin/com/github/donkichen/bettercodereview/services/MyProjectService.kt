package com.github.donkichen.bettercodereview.services

import com.intellij.openapi.project.Project
import com.github.donkichen.bettercodereview.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
