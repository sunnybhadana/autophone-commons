package com.revaltronics.commons.activities

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.revaltronics.commons.R
import com.revaltronics.commons.extensions.toast
import com.revaltronics.strings.R as stringsR
import com.revaltronics.commons.compose.alert_dialog.rememberAlertDialogState
import com.revaltronics.commons.compose.extensions.config
import com.revaltronics.commons.compose.extensions.enableEdgeToEdgeSimple
import com.revaltronics.commons.compose.extensions.rateStarsRedirectAndThankYou
import com.revaltronics.commons.compose.screens.*
import com.revaltronics.commons.compose.theme.AppThemeSurface
import com.revaltronics.commons.dialogs.AppSideloadedDialog
import com.revaltronics.commons.dialogs.ConfirmationAdvancedAlertDialog
import com.revaltronics.commons.dialogs.ConfirmationDialog
import com.revaltronics.commons.dialogs.RateStarsAlertDialog
import com.revaltronics.commons.extensions.*
import com.revaltronics.commons.helpers.*
import com.revaltronics.commons.models.FAQItem

class AboutActivity : BaseComposeActivity() {
    private val appName get() = intent.getStringExtra(APP_NAME) ?: ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdgeSimple()
        setContent {
            val isTopAppBarColorIcon by config.isTopAppBarColorIcon.collectAsStateWithLifecycle(initialValue = config.topAppBarColorIcon)
            val isTopAppBarColorTitle by config.isTopAppBarColorTitle.collectAsStateWithLifecycle(initialValue = config.topAppBarColorTitle)
            AppThemeSurface {
                val rateStarsAlertDialogState = getRateStarsAlertDialogState()
                val onRateUsClickAlertDialogState = getOnRateUsClickAlertDialogState(rateStarsAlertDialogState::show)
                AboutScreen(
                    goBack = ::finish,
                    aboutSection = {
                        AboutNewSection(
                            appName = appName,
                            appVersion = intent.getStringExtra(APP_VERSION_NAME) ?: "",
                            onRateUsClick = {
                                onRateUsClick(
                                    showConfirmationAdvancedDialog = onRateUsClickAlertDialogState::show,
                                    showRateStarsDialog = rateStarsAlertDialogState::show
                                )
                            },
                            onMoreAppsClick = ::launchMoreAppsFromUsIntent,
                            onPrivacyPolicyClick = ::onPrivacyPolicyClick,
                            onFAQClick = ::launchFAQActivity,
                            onTipJarClick = ::onTipJarClick,
                            onGithubClick = ::onGithubClick,
                            showGithub = showGithub(),
                        )
                    },
                    isTopAppBarColorIcon = isTopAppBarColorIcon,
                    isTopAppBarColorTitle = isTopAppBarColorTitle,
                )
            }
        }
    }

    @Composable
    private fun getRateStarsAlertDialogState() =
        rememberAlertDialogState().apply {
            DialogMember {
                RateStarsAlertDialog(
                    alertDialogState = this,
                    onRating = ::rateStarsRedirectAndThankYou
                )
            }
        }

    @Composable
    private fun getOnRateUsClickAlertDialogState(showRateStarsDialog: () -> Unit) =
        rememberAlertDialogState().apply {
            DialogMember {
                ConfirmationAdvancedAlertDialog(
                    alertDialogState = this,
                    message = "${getString(R.string.before_asking_question_read_faq)}\n\n${getString(R.string.make_sure_latest)}",
                    messageId = null,
                    positive = R.string.read_faq,
                    negative = R.string.skip
                ) { success ->
                    if (success) {
                        launchFAQActivity()
                    } else {
                        launchRateUsPrompt(showRateStarsDialog)
                    }
                }
            }
        }

    private fun launchFAQActivity() {
        val faqItems = intent.getSerializableExtra(APP_FAQ) as ArrayList<FAQItem>
        Intent(applicationContext, FAQActivity::class.java).apply {
            putExtra(
                APP_ICON_IDS,
                intent.getIntegerArrayListExtra(APP_ICON_IDS) ?: ArrayList<String>()
            )
            putExtra(APP_LAUNCHER_NAME, intent.getStringExtra(APP_LAUNCHER_NAME) ?: "")
            putExtra(APP_FAQ, faqItems)
            startActivity(this)
        }
    }

    private fun onRateUsClick(
        showConfirmationAdvancedDialog: () -> Unit,
        showRateStarsDialog: () -> Unit,
    ) {
        if (baseConfig.wasBeforeRateShown) {
            launchRateUsPrompt(showRateStarsDialog)
        } else {
            baseConfig.wasBeforeRateShown = true
            showConfirmationAdvancedDialog()
        }
    }

    private fun launchRateUsPrompt(
        showRateStarsDialog: () -> Unit,
    ) {
        if (baseConfig.wasAppRated) {
            redirectToRateUs()
        } else {
            showRateStarsDialog()
        }
    }

    private fun onPrivacyPolicyClick() {
        val appId = baseConfig.appId.removeSuffix(".debug")
        val url = when (appId) {
            "com.revaltronics.autophone" -> "https://revaltronics.com/autophone/privacy-policy.html"
            "com.revaltronics.smsmessenger" -> "https://revaltronics.com/autophone/privacy-policy.html"
            "com.revaltronics.contacts" -> "https://revaltronics.com/autophone/privacy-policy.html"
            "com.revaltronics.gallery" -> "https://revaltronics.com/autophone/privacy-policy.html"
            "com.revaltronics.filemanager" -> "https://revaltronics.com/autophone/privacy-policy.html"
            "com.revaltronics.voicerecorder", "com.revaltronics.voicerecorderfree" -> "https://revaltronics.com/autophone/privacy-policy.html"
            "com.revaltronics.calendar" -> "https://revaltronics.com/autophone/privacy-policy.html"
            else -> "https://revaltronics.com/autophone/privacy-policy.html"
        }
        launchViewIntent(url)
    }

    private fun onTipJarClick() {
        // Check if app is installed from Play Store for security
        if (isAppSideloaded()) {
            // Show security warning dialog instead of QR code
            AppSideloadedDialog(this) {
                // User chose to close the dialog, no further action needed
            }
            return
        }

        // Additional check: ensure Play Store is installed (backup verification)
        if (!isPlayStoreInstalled()) {
            // Show error message if Play Store is not available
            ConfirmationDialog(
                activity = this,
                message = getString(R.string.play_store_not_found_tip_jar),
                positive = R.string.ok,
                negative = 0
            ) {
                // User acknowledged, no further action needed
            }
            return
        }

        // Show a dialog with the Binance QR code for tipping with cryptocurrency
        val dialogView = layoutInflater.inflate(R.layout.dialog_binance_qr, null)
        val dialog = AlertDialog.Builder(this)
            .setTitle(getString(stringsR.string.tip_jar))
            .setView(dialogView)
            .setPositiveButton(R.string.ok, null)
            .create()
            
        // Set up the copy button functionality
        dialogView.findViewById<Button>(R.id.btn_copy_wallet).setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val binanceUsername = "User-c80c0"
            val clip = ClipData.newPlainText("Binance Username", binanceUsername)
            clipboard.setPrimaryClip(clip)
            toast(R.string.username_copied)
            
            // Consider using a snackbar instead of toast for better UX
            // Snackbar.make(dialogView, R.string.address_copied, Snackbar.LENGTH_SHORT).show()
        }
        
        dialog.show()
        
        // Keep the original implementation as a fallback or alternative option
        /*
        Intent(applicationContext, PurchaseActivity::class.java).apply {
            putExtra(APP_ICON_IDS, intent.getIntegerArrayListExtra(APP_ICON_IDS) ?: ArrayList<String>())
            putExtra(APP_LAUNCHER_NAME, intent.getStringExtra(APP_LAUNCHER_NAME) ?: "")
            putExtra(APP_NAME, intent.getStringExtra(APP_NAME) ?: "")
            putExtra(PRODUCT_ID_LIST, intent.getStringArrayListExtra(PRODUCT_ID_LIST) ?: arrayListOf("", "", ""))
            putExtra(PRODUCT_ID_LIST_RU, intent.getStringArrayListExtra(PRODUCT_ID_LIST_RU) ?: arrayListOf("", "", ""))
            putExtra(SUBSCRIPTION_ID_LIST, intent.getStringArrayListExtra(SUBSCRIPTION_ID_LIST) ?: arrayListOf("", "", ""))
            putExtra(SUBSCRIPTION_ID_LIST_RU, intent.getStringArrayListExtra(SUBSCRIPTION_ID_LIST_RU) ?: arrayListOf("", "", ""))
            putExtra(SUBSCRIPTION_YEAR_ID_LIST, intent.getStringArrayListExtra(SUBSCRIPTION_YEAR_ID_LIST) ?: arrayListOf("", "", ""))
            putExtra(SUBSCRIPTION_YEAR_ID_LIST_RU, intent.getStringArrayListExtra(SUBSCRIPTION_YEAR_ID_LIST_RU) ?: arrayListOf("", "", ""))
            putExtra(SHOW_LIFEBUOY, resources.getBoolean(R.bool.show_lifebuoy))
            putExtra(PLAY_STORE_INSTALLED, intent.getBooleanExtra(PLAY_STORE_INSTALLED, true))
            putExtra(RU_STORE, intent.getBooleanExtra(RU_STORE, false))
            putExtra(SHOW_COLLECTION, resources.getBoolean(R.bool.show_collection))
            startActivity(this)
        }
        */
    }

    private fun onGithubClick() {
        val repositoryName = intent.getStringExtra(APP_REPOSITORY_NAME) ?: return
        val url = "https://github.com/sunnybhadana/AutoPhone"
        launchViewIntent(url)
    }

    @Composable
    private fun showGithub() =
        remember { !intent.getStringExtra(APP_REPOSITORY_NAME).isNullOrEmpty() }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        changeAutoTheme()
    }

    private fun changeAutoTheme() {
        syncGlobalConfig {
            baseConfig.apply {
                if (isAutoTheme()) {
                    val isUsingSystemDarkTheme = isSystemInDarkMode()
                    textColor = resources.getColor(if (isUsingSystemDarkTheme) R.color.theme_dark_text_color else R.color.theme_light_text_color)
                    backgroundColor =
                        resources.getColor(if (isUsingSystemDarkTheme) R.color.theme_dark_background_color else R.color.theme_light_background_color)
                    finish()
                    startActivity(intent)
                }
            }
        }
        return
    }
}
