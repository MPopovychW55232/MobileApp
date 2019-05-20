package com.makki.languageapp.fragments.main.announcement

import androidx.lifecycle.ViewModel
import com.makki.languageapp.utils.SafeLiveData
import com.makki.services.api.managers.AnnouncementManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * @author Maksym.Popovych
 */
class AnnouncementViewModel : ViewModel(), KoinComponent {

    private val manager: AnnouncementManager by inject()
    private var disposable = Disposables.disposed()
    val adapter = SafeLiveData(AnnouncementAdapter())

    init {
        disposable = manager.getAll()
            .observeOn(AndroidSchedulers.mainThread())
            .toList()
            .subscribe { list ->
                adapter.value.setAll(list)
            }
    }

    override fun onCleared() {
        super.onCleared()

        disposable.dispose()
    }
}
