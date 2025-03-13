package com.s0xzwasd.go.version.plugin

import com.goide.sdk.GoSdk
import com.goide.sdk.GoSdkService
import com.intellij.openapi.project.Project
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

class GoVersionServiceTest {

    private lateinit var goVersionService: GoVersionService
    private lateinit var project: Project
    private lateinit var goSdkService: GoSdkService
    private lateinit var goSdk: GoSdk

    @BeforeEach
    fun setUp() {
        project = mockk()
        goSdkService = mockk()
        goSdk = mockk()

        mockkStatic(GoSdkService::class)
        every { GoSdkService.getInstance(any()) } returns goSdkService

        goVersionService = GoVersionService(project)
    }

    @Test
    fun testGetGoVersionWithValidVersion() {
        every { goSdkService.getSdk(null) } returns goSdk
        every { goSdk.version } returns "1.24.0"

        val result = goVersionService.getGoVersion()

        assertEquals("Go 1.24.0", result)
    }

    @Test
    fun testGetGoVersionWithNullVersion() {
        every { goSdkService.getSdk(null) } returns goSdk
        every { goSdk.version } returns null

        val result = goVersionService.getGoVersion()

        assertEquals("No Go", result)
    }

    @Test
    fun testGetGoVersionWithBlankVersion() {
        every { goSdkService.getSdk(null) } returns goSdk
        every { goSdk.version } returns ""

        val result = goVersionService.getGoVersion()

        assertEquals("No Go", result)
    }

    @Test
    fun testGetGoVersionWithException() {
        every { goSdkService.getSdk(null) } throws RuntimeException("Test exception")

        val result = goVersionService.getGoVersion()

        assertEquals("No Go", result)
    }
}
