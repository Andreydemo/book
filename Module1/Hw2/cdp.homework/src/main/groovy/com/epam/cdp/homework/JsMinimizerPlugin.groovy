package com.epam.cdp.homework

import org.gradle.api.Plugin
import org.gradle.api.Project

class JsMinimizerPlugin implements Plugin<Project> {
    void apply(Project project) {
        project.task("minifyJs", type:JsMinimizerTask)
    }
}

