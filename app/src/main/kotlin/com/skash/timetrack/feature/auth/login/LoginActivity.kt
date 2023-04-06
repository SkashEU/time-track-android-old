package com.skash.timetrack.feature.auth.login

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.jakewharton.rxbinding4.view.clicks
import com.skash.timetrack.MainActivity
import com.skash.timetrack.core.helper.sharedprefs.getPrefs
import com.skash.timetrack.core.helper.sharedprefs.saveAuthData
import com.skash.timetrack.core.helper.state.ErrorType
import com.skash.timetrack.core.helper.state.handle
import com.skash.timetrack.core.helper.state.loading.DefaultLoadingDialog
import com.skash.timetrack.core.helper.state.loading.LoadingDialog
import com.skash.timetrack.databinding.ActivityLoginBinding
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

    private val subscriptions = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
    }

    private fun launchMainActivity() {
        MainActivity.launch(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        subscriptions.clear()
    }
}