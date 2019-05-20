package com.makki.languageapp.fragments.main.messages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.makki.basic.model.NotificationAsset
import com.makki.languageapp.lib.base.BaseFragment
import org.koin.android.viewmodel.ext.android.viewModel
import com.makki.languageapp.databinding.FragmentNotificationDetailsBindingImpl

/**
 * @author Maksym.Popovych
 */
class NotificationDetailsFragment : BaseFragment() {

    companion object {
        fun newInstance(notification: NotificationAsset) =
            NotificationDetailsFragment().also {
                it.arguments = Bundle().apply {
                    putParcelable("item", notification)
                }
            }
    }

    private val model: NotificationDetailsViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentNotificationDetailsBindingImpl.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.model = model

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        model.setNotification(arguments?.getParcelable("item") ?: return)
    }

    override fun onBackPressed(): Boolean {
        return fragmentManager?.popBackStackImmediate() == true
    }
}
