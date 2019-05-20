package com.makki.languageapp.fragments.auth

import android.util.Log
import androidx.lifecycle.MutableLiveData
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
class LoginViewModel(private val repo: ContextProvider) : ViewModel(), KoinComponent {

    enum class Action { SignUp, Login }

    val manager: AuthManager by inject()

    val login = SafeLiveData("studstud")
    val password = SafeLiveData("studstud")
    val loginError = SafeLiveData("")
    val passwordError = SafeLiveData("")
    val errorMessage = SafeLiveData("")

    val enableButton = SafeLiveData(false)
    val loading = SafeLiveData(false)

    var action = MutableLiveData<Action>()

    private var disposable = Disposables.disposed()

    private fun loginValidation(): Boolean {
        return login.value.length in 8..15
    }

    private fun passwordValidation(): Boolean {
        return password.value.length in 8..15
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

    fun onTextChanged() {
        enableButton.postValue(loginValidation() && passwordValidation())
    }

    fun onLogin() {
        if (!loginValidation() || !passwordValidation()) return
        errorMessage.postValue("")

        disposable.dispose()
        disposable = manager.login(login.value, password.value)
            .doOnSubscribe {
                loading.postValue(true)
                if (!loginValidation() || !passwordValidation()) return@doOnSubscribe
                enableButton.postValue(false)
            }
            .doFinally {
                loading.postValue(false)
                if (!loginValidation() || !passwordValidation()) return@doFinally
                enableButton.postValue(true)
            }
            .subscribe { resp, throwable ->
                Log.e("TEST", "client -> $resp")
                if (throwable != null) {
                    errorMessage.postValue(throwable.message ?: "Unknown error")
                    return@subscribe
                }
                action.postValue(Action.Login)
            }
    }

    fun onSignUp() {
        action.postValue(Action.SignUp)
    }

    override fun onCleared() {
        super.onCleared()

        disposable.dispose()
    }
}
