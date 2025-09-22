package com.roshan.music

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

class NavigationTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testSplashScreen_navigatesToOnboarding() {
        // Wait for splash screen to disappear
        Thread.sleep(5000) // This is not ideal, but for a simple test it's ok

        // Check if onboarding screen is displayed
        composeTestRule.onNodeWithText("Discover New Music").assertIsDisplayed()
    }
}
