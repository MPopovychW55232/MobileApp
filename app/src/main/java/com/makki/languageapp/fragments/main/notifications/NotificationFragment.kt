package com.makki.languageapp.fragments.main.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import com.makki.basic.model.NotificationAsset
import com.makki.languageapp.databinding.FragmentNotificationScreenBindingImpl
import com.makki.languageapp.fragments.main.messages.NotificationDetailsFragment
import com.makki.languageapp.lib.base.BaseFragment
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * @author Maksym.Popovych
 */
class NotificationFragment : BaseFragment(), Observer<NotificationAsset> {

    companion object {
        fun newInstance() = NotificationFragment()
    }

    private val model: NotificationViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentNotificationScreenBindingImpl.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.model = model

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        model.selectAction.observe(this, this)
    }

    override fun onChanged(notificationAsset: NotificationAsset?) {
        if (notificationAsset == null || notificationAsset == NotificationAsset.Null) return

        val manager = fragmentManager ?: return
        manager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .replace(id, NotificationDetailsFragment.newInstance(notificationAsset))
            .addToBackStack(null)
            .commit()
    }
}
