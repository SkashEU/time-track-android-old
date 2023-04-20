package com.skash.timetrack.feature.auth.registration

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.skash.timetrack.core.helper.sharedprefs.getPrefs
import com.skash.timetrack.core.helper.sharedprefs.saveAuthData
import com.skash.timetrack.core.helper.state.handle
import com.skash.timetrack.core.helper.state.loading.DefaultLoadingDialog
import com.skash.timetrack.databinding.ActvityRegistrationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActvityRegistrationBinding

    private val viewModel by viewModels<RegistrationViewModel>()

    private val loadingDialog by lazy {
        DefaultLoadingDialog(this)
    }

    companion object {
        fun launch(context: Context) {
            context.startActivity(Intent(context, RegistrationActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActvityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bindActions()

        viewModel.registrationStateLiveData.observe(this) { state ->
            state.handle(this, loadingDialog, onSuccess = {
                getPrefs().saveAuthData(it)
                showOrganizationCreationAlert()
            })
        }
    }

    private fun bindActions() {
        binding.signUpButton.setOnClickListener {
            viewModel.registerUser(
                binding.nameEditText.text.toString(),
                binding.emailEditText.text.toString(),
                binding.passwordEditText.text.toString()
            )
        }
    }

    private fun showOrganizationCreationAlert() {
        MaterialAlertDialogBuilder(this)
            .setTitle("Do you want to create a organization?")
            .setMessage("You can skip this step if you want to become a team member of a organization")
            .setPositiveButton("Create") { _, _ ->

            }
            .setNegativeButton("Skip") { _, _ ->

            }
            .show()
    }
}