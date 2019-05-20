package com.makki.languageapp.fragments.auth

import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.makki.languageapp.ContextProvider
import com.makki.languageapp.R
import com.makki.services.api.managers.AuthManager
import com.makki.languageapp.lib.utils.asResourceString
import com.makki.languageapp.utils.SafeLiveData
import io.reactivex.disposables.Disposables
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * @author Maksym.Popovych
 */
class SignUpViewModel(private val repo: ContextProvider) : ViewModel(), KoinComponent {

    val manager: AuthManager by inject()

    val login = SafeLiveData("12345678")
    val email = SafeLiveData("test@example.com")
    val password = SafeLiveData("123565336")
    val passwordConfirm = SafeLiveData("123565336")
    val loginError = SafeLiveData("")
    val emailError = SafeLiveData("")
    val passwordError = SafeLiveData("")
    val passwordConfirmError = SafeLiveData("")
    val errorMessage = SafeLiveData("")

    val enableButton = SafeLiveData(false)
    val loading = SafeLiveData(false)

    private var disposable = Disposables.disposed()

    private fun loginValidation(): Boolean {
        return login.value.length in 8..15
    }

    private fun passwordValidation(): Boolean {
        return password.value.length in 8..15
    }

    private fun confirmPassValidation(): Boolean {
        return passwordConfirm.value.length in 8..15
    }

    private fun passwordMatches(): Boolean {
        return passwordConfirm.value == password.value
    }

    private fun emailValidation(): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email.value).matches()
    }


    fun loginFocusChange(isFocus: Boolean) {
        if (isFocus) return

        if (loginValidation()) {
            loginError.postValue("")
        } else {
            loginError.postValue(R.string.login_validation_error.asResourceString(repo.context))
        }
    }

    fun passwordFocusChange(isFocus: Boolean) {
        if (isFocus) return

        if (passwordValidation()) {
            passwordError.postValue("")
        } else {
            passwordError.postValue(R.string.password_validation_error.asResourceString(repo.context))
        }
    }

    fun confirmPassFocusChange(isFocus: Boolean) {
        if (isFocus) return

        val valid = confirmPassValidation()
        val matches = passwordMatches()
        if (valid && matches) {
            passwordConfirmError.postValue("")
        } else if (!matches){
            passwordConfirmError.postValue(R.string.password_mismatch_error.asResourceString(repo.context))
        } else {
            passwordConfirmError.postValue(R.string.password_validation_error.asResourceString(repo.context))
        }
    }

    fun emailFocusChange(isFocus: Boolean) {
        if (isFocus) return

        if (emailValidation()) {
            emailError.postValue("")
        } else {
            emailError.postValue(R.string.email_validation_error.asResourceString(repo.context))
        }
    }

    fun onTextChanged() {
        enableButton.postValue(loginValidation()
                && passwordValidation()
                && confirmPassValidation()
                && passwordMatches()
                && emailValidation())
    }

    fun onSignUp() {

    }

}