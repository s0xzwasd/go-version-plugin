package com.s0xzwasd.go.version.plugin

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetFactory
import com.intellij.util.Consumer
import java.awt.event.MouseEvent
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class GoVersionWidget(private val project: Project) : StatusBarWidget {
    companion object {
        const val ID = "GoVersionWidget"
    }

    private var statusBar: StatusBar? = null
    private var currentVersion: String? = null
    private val executor = Executors.newSingleThreadScheduledExecutor()

    override fun ID(): String = ID

    override fun getPresentation(): StatusBarWidget.WidgetPresentation {
        return object : StatusBarWidget.TextPresentation {
            override fun getText(): String {
                return GoVersionService.getInstance(project).getGoVersion()
            }

            override fun getTooltipText(): String {
                return "Go version (inherited from the IDE settings)"
            }

            override fun getClickConsumer(): Consumer<MouseEvent>? = null

            override fun getAlignment(): Float = 0f
        }
    }

    override fun install(statusBar: StatusBar) {
        this.statusBar = statusBar
        currentVersion = GoVersionService.getInstance(project).getGoVersion()

        executor.scheduleAtFixedRate({
            val newVersion = GoVersionService.getInstance(project).getGoVersion()
            if (newVersion != currentVersion) {
                currentVersion = newVersion
                statusBar.updateWidget(ID)
            }
        }, 1, 1, TimeUnit.SECONDS)
    }

    override fun dispose() {
        executor.shutdown()
        statusBar = null
    }
}

class GoVersionWidgetFactory : StatusBarWidgetFactory {
    override fun getId(): String = GoVersionWidget.ID

    override fun getDisplayName(): String = "Go Version"

    override fun isAvailable(project: Project): Boolean = true

    override fun createWidget(project: Project): StatusBarWidget {
        return GoVersionWidget(project)
    }

    override fun disposeWidget(widget: StatusBarWidget) {
        widget.dispose()
    }

    override fun canBeEnabledOn(statusBar: StatusBar): Boolean = true
}
