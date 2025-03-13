package com.s0xzwasd.go.version.plugin

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.StatusBar
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class GoVersionWidgetFactoryTest {

    private lateinit var factory: GoVersionWidgetFactory
    private lateinit var project: Project
    private lateinit var statusBar: StatusBar
    private lateinit var widget: GoVersionWidget

    @BeforeEach
    fun setUp() {
        factory = GoVersionWidgetFactory()
        project = mockk()
        statusBar = mockk()
        widget = mockk(relaxed = true)
    }

    @Test
    fun testGetId() {
        assertEquals("GoVersionWidget", factory.getId())
    }

    @Test
    fun testGetDisplayName() {
        assertEquals("Go Version", factory.getDisplayName())
    }

    @Test
    fun testIsAvailable() {
        assertEquals(true, factory.isAvailable(project))
    }

    @Test
    fun testCreateWidget() {
        mockkConstructor(GoVersionWidget::class)
        every { anyConstructed<GoVersionWidget>().ID() } returns "GoVersionWidget"

        val result = factory.createWidget(project)

        assertEquals("GoVersionWidget", result.ID())
    }

    @Test
    fun testDisposeWidget() {
        factory.disposeWidget(widget)

        verify { widget.dispose() }
    }

    @Test
    fun testCanBeEnabledOn() {
        assertEquals(true, factory.canBeEnabledOn(statusBar))
    }
}
