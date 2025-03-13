package com.s0xzwasd.go.version.plugin

import com.goide.sdk.GoSdkService
import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project

@Service(Service.Level.PROJECT)
class GoVersionService(private val project: Project) {
    private val logger = Logger.getInstance(GoVersionService::class.java)

    /**
     * Gets the Go version directly from the settings.
     */
    fun getGoVersion(): String {
        return fetchGoVersion()
    }

    /**
     * Fetches the Go version using the GoSdkService.
     */
    private fun fetchGoVersion(): String {
        try {
            // Use GoSdkService to get the Go SDK
            val goSdk = GoSdkService.getInstance(project).getSdk(null)

            // Get a version from the SDK
            val version = goSdk.version
            if (version != null && version.isNotBlank()) {
                return "Go $version"
            }

            // If a version is null or blank, return "No Go"
            return "No Go"
        } catch (e: Exception) {
            logger.warn("Error getting Go version", e)
            return "No Go"
        }
    }

    companion object {
        @JvmStatic
        fun getInstance(project: Project): GoVersionService {
            return project.getService(GoVersionService::class.java)
        }
    }
}
