package com.makki.languageapp.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import com.makki.languageapp.databinding.FragmentLoginBindingImpl
import com.makki.languageapp.fragments.main.MainFragment
import com.makki.languageapp.lib.base.BaseFragment
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * @author Maksym.Popovych
 */
class LoginFragment : BaseFragment(), Observer<LoginViewModel.Action> {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private val model: LoginViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentLoginBindingImpl.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.model = model
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        model.action.observe(this, this)
    }

    override fun onChanged(step: LoginViewModel.Action?) {
        val manager = fragmentManager
        if (step == null || manager == null) return

        var addToBackStack = false
        val nextFragment = if (step == LoginViewModel.Action.SignUp) {
            addToBackStack = true
            SignUpFragment.newInstance()
        } else {
            MainFragment.newInstance()
        }

        val transition = manager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)

        if (addToBackStack) transition.addToBackStack(null)

        transition.replace(id, nextFragment)
            .commit()
    }
}
