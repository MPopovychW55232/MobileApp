package com.makki.languageapp.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import com.makki.languageapp.databinding.FragmentWelcomeBindingImpl
import com.makki.languageapp.lib.base.BaseFragment
import com.makki.languageapp.lib.views.SpinnerView
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * @author Maksym.Popovych
 */
class WelcomeFragment : BaseFragment(), Observer<Fragment> {

    companion object {
        fun newInstance() = WelcomeFragment()
    }

    private val model: WelcomeViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentWelcomeBindingImpl.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.model = model

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        model.nextFragment.observe(this, this)
    }

    override fun onChanged(t: Fragment?) {
        val manager = fragmentManager
        if (t == null || manager == null) return

        manager.beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .replace(id, t)
            .commit()
    }
}
