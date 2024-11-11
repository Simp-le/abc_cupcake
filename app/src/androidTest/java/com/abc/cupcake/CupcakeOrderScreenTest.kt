package com.abc.cupcake

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.abc.cupcake.data.DataSource
import com.abc.cupcake.data.OrderUiState
import com.abc.cupcake.ui.OrderSummaryScreen
import com.abc.cupcake.ui.SelectOptionScreen
import com.abc.cupcake.ui.StartOrderScreen
import org.junit.Rule
import org.junit.Test

class CupcakeOrderScreenTest {

    /**
     * Note: To access to an empty activity, the code uses ComponentActivity instead of
     * MainActivity.
     */
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val mockOrderUiState = OrderUiState(
        quantity = 6,
        flavor = "Vanilla",
        date = "Wed Jul 21",
        price = "$100",
        pickupOptions = listOf()
    )


    /**
     * When quantity options are provided to StartOrderScreen, the options are displayed on the
     * screen.
     */
    @Test
    fun startOrderScreen_verifyContent() {
        // When StartOrderScreen is loaded
        composeTestRule.setContent {
            StartOrderScreen(
                quantityOptions = DataSource.quantityOptions,
                onNextButtonClicked = {})
        }

        // Then all the options are displayed on the screen.
        DataSource.quantityOptions.forEach { option ->
            composeTestRule.onNodeWithStringId(option.first).assertIsDisplayed()
        }
    }

    /**
     * When list of options and subtotal are provided to SelectOptionScreen,the options and subtotal
     * are displayed on the screen and the next button is disabled.
     */
    @Test
    fun selectOptionScreen_verifyContent() {
        // Given list of options
        val flavors = listOf("Vanilla", "Chocolate", "Hazelnut", "Cookie", "Mango")
        // And subtotal
        val subtotal = "$100"

        // When SelectOptionScreen is loaded
        composeTestRule.setContent { SelectOptionScreen(subtotal = subtotal, options = flavors) }

        // Then all the options are displayed on the screen.
        flavors.forEach { flavor ->
            composeTestRule.onNodeWithText(flavor).assertIsDisplayed()
        }

        // And then the subtotal is displayed correctly.
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.subtotal_price, subtotal)
        ).assertIsDisplayed()

        // And then the next button is disabled
        composeTestRule.onNodeWithStringId(R.string.next).assertIsNotEnabled()
    }

    /**
     * When list of options and subtotal are provided to SelectOptionScreen, and one of the option
     * is selected, then the next button is enabled.
     */
    @Test
    fun selectOptionScreen_optionSelected_NextButtonEnabled() {
        // Given list of options
        val flavors = listOf("Vanilla", "Chocolate", "Hazelnut", "Cookie", "Mango")
        // And subtotal
        val subtotal = "$100"

        // When SelectOptionScreen is loaded
        composeTestRule.setContent { SelectOptionScreen(subtotal = subtotal, options = flavors) }

        // And one option is selected
        composeTestRule.onNodeWithText(flavors[0]).performClick()

        // Then the next button is disabled
        composeTestRule.onNodeWithStringId(R.string.next).assertIsEnabled()
    }

    /**
     * When a OrderUiState is provided to Summary Screen, then the flavor, date and subtotal is
     * displayed on the screen.
     */
    @Test
    fun summaryScreen_verifyContentDisplay() {
        // When Summary Screen is loaded
        composeTestRule.setContent {
            OrderSummaryScreen(
                orderUiState = mockOrderUiState,
                onSendButtonClicked = { _, _ -> },
                onCancelButtonClicked = {})
        }

        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(
                R.string.six_cupcakes,
                mockOrderUiState.quantity
            )
        ).assertIsDisplayed()
        composeTestRule.onNodeWithText(mockOrderUiState.flavor).assertIsDisplayed()
        composeTestRule.onNodeWithText(mockOrderUiState.date).assertIsDisplayed()
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(
                R.string.subtotal_price,
                mockOrderUiState.price
            )
        ).assertIsDisplayed()
    }
}