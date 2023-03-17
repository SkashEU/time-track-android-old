package com.skash.timetrack.feature.auth.login

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.CreateCredentialResponse
import androidx.credentials.CreatePasswordRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.GetPasswordOption
import androidx.credentials.PasswordCredential
import com.jakewharton.rxbinding4.view.clicks
import com.skash.timetrack.MainActivity
import com.skash.timetrack.core.helper.state.handle
import com.skash.timetrack.core.helper.state.loading.DefaultLoadingDialog
import com.skash.timetrack.core.helper.state.loading.LoadingDialog
import com.skash.timetrack.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import kotlinx.coroutines.rx3.rxObservable


@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val viewModel: LoginViewModel by viewModels()

    private val credentialManager: CredentialManager by lazy {
        CredentialManager.create(this)
    }

    private val loadingDialog: LoadingDialog by lazy {
        DefaultLoadingDialog(this)
    }

    private val subscriptions = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bindActions()
        fetchUserCredentials()

        viewModel.authStateLiveData.observe(this) { authState ->
            authState.handle(this, loadingDialog, onSuccess = {
                // TODO: Integrate CredentialManager
                registerPassword()
                launchMainActivity()
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

    private fun fetchUserCredentials() {

        val getPasswordOption = GetPasswordOption()

        val getCredentialRequest = GetCredentialRequest(
            listOf(getPasswordOption)
        )

        rxObservable<GetCredentialResponse> {
            credentialManager.getCredential(getCredentialRequest, this@LoginActivity)
        }.subscribeBy(
            onNext = { response ->
                handleSignIn(response)
            },
            onError = { error ->
                Log.e(javaClass.name, "Error fetching credenials", error)
            }
        ).addTo(subscriptions)
    }

    private fun handleSignIn(result: GetCredentialResponse) {
        when (val credentials = result.credential) {
            is PasswordCredential -> {
                val username = credentials.id
                val password = credentials.password
                binding.emailEditText.setText(username)
                binding.passwordEditText.setText(password)
                viewModel.authenticateUser(username, password)
                Log.d(javaClass.name, "Got credentials :: name: $username, password: $password")
            }

            else -> {
                Log.e(javaClass.name, "Unexpected type of credential")
            }
        }
    }

    private fun registerPassword() {
        // TODO: Implement Result handling
        val createPasswordRequest = CreatePasswordRequest(
            binding.emailEditText.text.toString(),
            binding.passwordEditText.text.toString()
        )

        rxObservable<CreateCredentialResponse> {
            credentialManager.createCredential(createPasswordRequest, this@LoginActivity)
        }.subscribeBy(
            onNext = {
                launchMainActivity()
            },
            onError = { error ->
                Log.d(javaClass.name, "Failed to save credentials", error)
                launchMainActivity()
            }
        ).addTo(subscriptions)
    }

    private fun launchMainActivity() {
        MainActivity.launch(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        subscriptions.clear()
    }
}