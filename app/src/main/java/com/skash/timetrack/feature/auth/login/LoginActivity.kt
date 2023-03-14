package com.skash.timetrack.feature.auth.login

import android.R.attr.password
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.CreateCredentialResponse
import androidx.credentials.CreatePasswordRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CredentialManagerCallback
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.GetPasswordOption
import androidx.credentials.PasswordCredential
import androidx.credentials.exceptions.CreateCredentialException
import androidx.credentials.exceptions.GetCredentialException
import com.skash.timetrack.R
import com.skash.timetrack.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val credentialManager: CredentialManager by lazy {
        CredentialManager.create(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchUserCredentials()

        binding.loginButton.setOnClickListener {
            registerPassword()
        }
    }

    private fun fetchUserCredentials() {
        val getPasswordOption = GetPasswordOption()

        val getCredentialRequest = GetCredentialRequest(
            listOf(getPasswordOption)
        )

        credentialManager.getCredentialAsync(
            getCredentialRequest,
            this,
            cancellationSignal = null,
            executor = mainExecutor,
            callback = object :
                CredentialManagerCallback<GetCredentialResponse, GetCredentialException> {
                override fun onError(e: GetCredentialException) {
                    Log.e(javaClass.name, "Error fetching credenials", e)
                }

                override fun onResult(result: GetCredentialResponse) {
                    handleSignIn(result)
                }

            })
    }

    private fun handleSignIn(result: GetCredentialResponse) {
        when (val credentials = result.credential) {
            is PasswordCredential -> {
                val username = credentials.id
                val password = credentials.password
                Log.d(javaClass.name, "Got credentials :: name: $username, password: $password")
            }

            else -> {
                Log.e(javaClass.name, "Unexpected type of credential")
            }
        }
    }

    private fun registerPassword() {
        val createPasswordRequest = CreatePasswordRequest(
            binding.emailEditText.text.toString(),
            binding.passwordEditText.text.toString()
        )

        credentialManager.createCredentialAsync(
            createPasswordRequest,
            this,
            null,
            mainExecutor,
            object :
                CredentialManagerCallback<CreateCredentialResponse, CreateCredentialException> {
                override fun onError(e: CreateCredentialException) {
                    Log.d(javaClass.name, "Saved creds error", e)
                }

                override fun onResult(result: CreateCredentialResponse) {
                    Log.d(javaClass.name, "Saved creds")
                }
            }

        )


    }
}