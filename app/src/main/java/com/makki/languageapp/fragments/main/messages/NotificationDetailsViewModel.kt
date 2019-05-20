package com.makki.languageapp.fragments.main.messages

import android.text.format.DateUtils
import androidx.lifecycle.ViewModel
import com.makki.basic.model.NotificationAsset
import com.makki.basic.model.PersonAsset
import com.makki.languageapp.utils.SafeLiveData
import com.makki.services.api.managers.PersonManager
import io.reactivex.disposables.Disposables
import org.koin.core.KoinComponent
import org.koin.core.inject

/**
 * @author Maksym.Popovych
 */
class NotificationDetailsViewModel : ViewModel(), KoinComponent {

    var disposable = Disposables.disposed()
    val manager: PersonManager by inject()

    var sender = SafeLiveData(PersonAsset.Null)
    var notification = SafeLiveData(NotificationAsset.Null)

    var notificationSenderName = SafeLiveData("")
    var notificationTitle = SafeLiveData("")
    var notificationText = SafeLiveData("")
    var notificationTime = SafeLiveData("")

    var senderFullName = SafeLiveData("")
    var senderDescription = SafeLiveData("")
    var senderContacts = SafeLiveData("")
    var senderAvatarUrl = SafeLiveData("")

    init {
        notification.observeForever {
            onNotificationChanged()
        }
        sender.observeForever {
            onSenderLoaded()
        }
    }

    private fun onNotificationChanged() {
        val n = notification.value

        if (n == NotificationAsset.Null) return

        notificationSenderName.postValue(n.senderDisplayName)
        notificationTitle.postValue(n.title)
        notificationText.postValue(n.message)

        val dateString = DateUtils.getRelativeTimeSpanString(n.timestamp, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS)
        notificationTime.postValue("\u2022 $dateString")

        disposable.dispose()
        disposable = manager.getById(notification.value.senderId)
            .subscribe {
                sender.postValue(it)
            }
    }

    fun setNotification(n: NotificationAsset) {
        notification.postValue(n)
    }

    private fun onSenderLoaded() {
        val s = sender.value

        if (s == PersonAsset.Null) return

        senderFullName.postValue(s.name)
        senderContacts.postValue(s.email)
        senderDescription.postValue(s.extra)
        senderAvatarUrl.postValue(s.photo)
    }

}