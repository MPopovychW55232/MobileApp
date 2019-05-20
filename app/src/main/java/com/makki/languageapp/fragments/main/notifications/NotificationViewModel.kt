package com.makki.languageapp.fragments.main.notifications

import androidx.lifecycle.ViewModel
import com.makki.basic.model.NotificationAsset
import com.makki.languageapp.utils.SafeLiveData
import com.makki.services.api.managers.NotificationManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * @author Maksym.Popovych
 */
class NotificationViewModel : ViewModel(), KoinComponent {

    val manager: NotificationManager by inject()

    private val innerAdapter = NotificationAdapter()
    var adapter = SafeLiveData(innerAdapter)
    var selectAction = SafeLiveData(NotificationAsset.Null)

    private var disposable = Disposables.disposed()
    private var clickDisposable = Disposables.disposed()

    init {
        disposable = manager.getAll()
            .observeOn(AndroidSchedulers.mainThread())
            .toList()
            .subscribe { list ->
                adapter.value.setAll(list)
            }

        clickDisposable = innerAdapter.onClickSubject
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { selectAction.postValue(it) }
    }

    override fun onCleared() {
        super.onCleared()

        disposable.dispose()
        clickDisposable.dispose()
    }
}
