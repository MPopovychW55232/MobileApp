package com.makki.languageapp.fragments.main.grades

import androidx.lifecycle.ViewModel
import com.makki.languageapp.utils.SafeLiveData
import com.makki.services.api.managers.GradesManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * @author Maksym.Popovych
 */
class GradesViewModel : ViewModel(), KoinComponent {

    private val manager: GradesManager by inject()
    private var disposable = Disposables.disposed()
    val adapter = SafeLiveData(GradesAdapter())

    init {
        disposable = manager.getAll()
            .observeOn(AndroidSchedulers.mainThread())
            .toList()
            .subscribe { list ->
                adapter.value.setAll(list)
            }
    }

}