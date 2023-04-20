package com.skash.timetrack.feature.auth.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding4.view.clicks
import com.skash.timetrack.MainActivity
import com.skash.timetrack.core.helper.sharedprefs.getAuthData
import com.skash.timetrack.core.helper.sharedprefs.getPrefs
import com.skash.timetrack.core.helper.sharedprefs.saveAuthData
import com.skash.timetrack.core.helper.state.ErrorType
import com.skash.timetrack.core.helper.state.handle
import com.skash.timetrack.core.helper.state.loading.DefaultLoadingDialog
import com.skash.timetrack.core.helper.state.loading.LoadingDialog
import com.skash.timetrack.databinding.ActivityLoginBinding
import com.skash.timetrack.feature.auth.registration.RegistrationActivity
import com.skash.timetrack.feature.auth.reset.PasswordResetBottomSheet
import com.skash.timetrack.feature.auth.twofa.TwoFactorAuthBottomSheet
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo


@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val viewModel: LoginViewModel by viewModels()

    private val loadingDialog: LoadingDialog by lazy {
        DefaultLoadingDialog(this)
    }

    private val twoFactorBottomSheet = TwoFactorAuthBottomSheet(onCodeEntered = { twoFACode ->
        viewModel.authenticateUser(
            binding.emailEditText.text.toString(),
            binding.passwordEditText.text.toString(),
            twoFACode
        )
    })

    private val passwordResetBottomSheet = PasswordResetBottomSheet(onPasswordResetRequested = {
        Snackbar.make(
            binding.root,
            "If an account with this email exists, a password reset link will be sent.",
            Snackbar.LENGTH_SHORT
        ).show()
    })

    private val subscriptions = CompositeDisposable()

    companion object {
        fun launch(context: Context) {
            context.startActivity(Intent(context, LoginActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (getPrefs().getAuthData().bearer.isNotEmpty()) {
            launchMainActivity()
            return
        }

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bindActions()

        viewModel.authStateLiveData.observe(this) { authState ->
            authState.handle(this, loadingDialog, onSuccess = {
                getPrefs().saveAuthData(it)
                launchMainActivity()
            }, handleError = { error ->
                if (error is ErrorType.TwoFAMissing) {
                    twoFactorBottomSheet.show(supportFragmentManager, null)
                    return@handle true
                }
                false
            })
        }
    }

    private fun bindActions() {
        binding.loginButton.clicks()
            .subscribe {
                viewModel.authenticateUser(
                    binding.emailEditText.text.toString(),
                    binding.passwordEditText.text.toString()
                )
            }
            .addTo(subscriptions)

        binding.registerButton.clicks()
            .subscribe {
                RegistrationActivity.launch(this)
            }
            .addTo(subscriptions)

        binding.forgotPasswordTextView.setOnClickListener {
            passwordResetBottomSheet.show(supportFragmentManager, null)
        }
    }

    private fun launchMainActivity() {
        MainActivity.launch(this)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        subscriptions.clear()
    }
}