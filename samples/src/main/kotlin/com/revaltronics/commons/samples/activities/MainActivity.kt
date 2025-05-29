package com.revaltronics.commons.samples.activities

import android.app.DatePickerDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.compose.setContent
import androidx.appcompat.app.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.revaltronics.commons.activities.BaseSimpleActivity
import com.revaltronics.commons.activities.ManageBlockedNumbersActivity
import com.revaltronics.commons.compose.alert_dialog.AlertDialogState
import com.revaltronics.commons.compose.alert_dialog.rememberAlertDialogState
import com.revaltronics.commons.compose.extensions.*
import com.revaltronics.commons.compose.theme.AppThemeSurface
import com.revaltronics.commons.dialogs.*
import com.revaltronics.commons.extensions.*
import com.revaltronics.commons.helpers.LICENSE_AUTOFITTEXTVIEW
import com.revaltronics.commons.helpers.SHOW_ALL_TABS
import com.revaltronics.commons.models.FAQItem
import com.revaltronics.commons.samples.BuildConfig
import com.revaltronics.commons.samples.R
import com.revaltronics.commons.samples.screens.MainScreen
import com.revaltronics.strings.R as stringsR

class MainActivity : BaseSimpleActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        isMaterialActivity = true
        super.onCreate(savedInstanceState)
        appLaunched(BuildConfig.APPLICATION_ID)
        enableEdgeToEdgeSimple()
        setContent {
            val isTopAppBarColorIcon by config.isTopAppBarColorIcon.collectAsStateWithLifecycle(initialValue = config.topAppBarColorIcon)
            AppThemeSurface {
                val showMoreApps = onEventValue { !resources.getBoolean(com.revaltronics.commons.R.bool.hide_google_relations) }

                MainScreen(
                    openColorCustomization = ::startCustomizationActivity,
                    manageBlockedNumbers = {
                        startActivity(Intent(this@MainActivity, ManageBlockedNumbersActivity::class.java))
                    },
                    showComposeDialogs = {
                        startActivity(Intent(this@MainActivity, TestDialogActivity::class.java))
                    },
                    openTestButton = ::setupStartDate,
                    showMoreApps = showMoreApps,
                    openAbout = ::launchAbout,
                    moreAppsFromUs = ::launchMoreAppsFromUsIntent,
                    startPurchaseActivity = ::launchPurchase,
                    isTopAppBarColorIcon = isTopAppBarColorIcon,
                )
                AppLaunched()
            }
        }
    }

    @Composable
    private fun AppLaunched(
        rateStarsAlertDialogState: AlertDialogState = getRateStarsAlertDialogState(),
    ) {
        LaunchedEffect(Unit) {
            appLaunchedCompose(
                appId = BuildConfig.APPLICATION_ID,
                showRateUsDialog = rateStarsAlertDialogState::show,
            )
        }
    }

    @Composable
    private fun getRateStarsAlertDialogState() = rememberAlertDialogState().apply {
        DialogMember {
            RateStarsAlertDialog(alertDialogState = this, onRating = ::rateStarsRedirectAndThankYou)
        }
    }

    private fun startCustomizationActivity() {
        startCustomizationActivity(
            showAccentColor = true,
            isCollection = false,
            productIdList = arrayListOf("", "", ""),
            productIdListRu = arrayListOf("", "", ""),
            subscriptionIdList = arrayListOf("", "", ""),
            subscriptionIdListRu = arrayListOf("", "", ""),
            subscriptionYearIdList = arrayListOf("", "", ""),
            subscriptionYearIdListRu = arrayListOf("", "", ""),
            playStoreInstalled = isPlayStoreInstalled(),
            ruStoreInstalled = isRuStoreInstalled(),
            showAppIconColor = true
        )
    }

    private fun launchPurchase() {
        // Use the same tip jar functionality instead of purchase activity
        onTipJarClick()
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
                message = getString(com.revaltronics.commons.R.string.play_store_not_found_tip_jar),
                positive = com.revaltronics.commons.R.string.ok,
                negative = 0
            ) {
                // User acknowledged, no further action needed
            }
            return
        }

        // Show a dialog with the Binance QR code for tipping with cryptocurrency
        val dialogView = layoutInflater.inflate(com.revaltronics.commons.R.layout.dialog_binance_qr, null)
        val dialog = AlertDialog.Builder(this)
            .setTitle(getString(stringsR.string.tip_jar))
            .setView(dialogView)
            .setPositiveButton(com.revaltronics.commons.R.string.ok, null)
            .create()
            
        // Set up the copy button functionality
        dialogView.findViewById<Button>(com.revaltronics.commons.R.id.btn_copy_wallet).setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val binanceUsername = "User-c80c0"
            val clip = ClipData.newPlainText("Binance Username", binanceUsername)
            clipboard.setPrimaryClip(clip)
            toast(com.revaltronics.commons.R.string.username_copied)
        }
        
        dialog.show()
    }

    private fun launchAbout() {
        val licenses = LICENSE_AUTOFITTEXTVIEW

        val faqItems = arrayListOf(
            FAQItem(com.revaltronics.commons.R.string.faq_1_title_commons, com.revaltronics.commons.R.string.faq_1_text_commons),
            FAQItem(com.revaltronics.commons.R.string.faq_4_title_commons, com.revaltronics.commons.R.string.faq_4_text_commons)
        )

        if (!resources.getBoolean(com.revaltronics.commons.R.bool.hide_google_relations)) {
            faqItems.add(FAQItem(com.revaltronics.commons.R.string.faq_2_title_commons, com.revaltronics.commons.R.string.faq_2_text_commons))
            faqItems.add(FAQItem(com.revaltronics.commons.R.string.faq_6_title_commons, com.revaltronics.commons.R.string.faq_6_text_commons))
        }

        startAboutActivity(
            R.string.app_name_g,
            licenses,
            BuildConfig.VERSION_NAME,
            faqItems,
            true,
            arrayListOf("", "", ""), arrayListOf("", "", ""),
            arrayListOf("", "", ""), arrayListOf("", "", ""),
            arrayListOf("", "", ""), arrayListOf("", "", ""),
            playStoreInstalled = isPlayStoreInstalled(),
            ruStoreInstalled = isRuStoreInstalled())
    }

    private fun securityDialog() {
        val tabToShow = if (config.isAppPasswordProtectionOn) config.appProtectionType else SHOW_ALL_TABS
        SecurityDialog(this@MainActivity, config.appPasswordHash, tabToShow) { hash, type, success ->
            if (success) {
                val hasPasswordProtection = config.isAppPasswordProtectionOn
                config.isAppPasswordProtectionOn = !hasPasswordProtection
                config.appPasswordHash = if (hasPasswordProtection) "" else hash
                config.appProtectionType = type
            }
        }
    }

    private fun setupStartDate() {
        hideKeyboard()
        val datePicker = DatePickerDialog(
            this, getDatePickerDialogTheme(), startDateSetListener, 2024, 12, 30
        )

        datePicker.show()
        
//        FilePickerDialog(this) {
//        }
    }

    private val startDateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
    }

    override fun getAppLauncherName() = getString(R.string.commons_app_name)

    override fun getAppIconIDs() = arrayListOf(
        R.mipmap.ic_launcher,
        R.mipmap.ic_launcher_one,
        R.mipmap.ic_launcher_two,
        R.mipmap.ic_launcher_three,
        R.mipmap.ic_launcher_four,
        R.mipmap.ic_launcher_five,
        R.mipmap.ic_launcher_six,
        R.mipmap.ic_launcher_seven,
        R.mipmap.ic_launcher_eight,
        R.mipmap.ic_launcher_nine,
        R.mipmap.ic_launcher_ten,
        R.mipmap.ic_launcher_eleven
    )

    override fun getRepositoryName() = "Gallery"
}
