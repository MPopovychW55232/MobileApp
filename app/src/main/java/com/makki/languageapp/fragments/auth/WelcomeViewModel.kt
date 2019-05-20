package com.makki.languageapp.fragments.auth

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.makki.languageapp.ContextProvider
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * @author Maksym.Popovych
 */
class WelcomeViewModel : ViewModel() {

    private val job: Job = GlobalScope.launch {
        delay(1000)
        nextFragment.postValue(LoginFragment.newInstance())
    }

    val spinnerVisibility = MutableLiveData<Boolean>().also {
        it.postValue(true)
    }

    var nextFragment = MutableLiveData<Fragment>()

    override fun onCleared() {
        super.onCleared()

        job.cancel()
    }
}
