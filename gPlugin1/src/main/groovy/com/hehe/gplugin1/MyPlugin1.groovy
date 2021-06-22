package com.hehe.gplugin1

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

public class MyPlugin1 implements Plugin<Project> {
    @Override
    void apply(Project project) {
        println("----------- this is my plugin1 ----------")

        def android = project.extensions.getByType(AppExtension)
        println '----------- registering AutoTrackTransform  -----------'
        LifeCycleTransform transform = new LifeCycleTransform()
        android.registerTransform(transform)

    }
}


