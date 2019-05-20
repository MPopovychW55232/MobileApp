package com.makki.languageapp

import android.content.Context
import com.makki.languageapp.fragments.auth.LoginViewModel
import com.makki.languageapp.fragments.auth.WelcomeViewModel
import com.makki.languageapp.fragments.auth.SignUpViewModel
import com.makki.languageapp.fragments.main.MainFragViewModel
import com.makki.languageapp.fragments.main.announcement.AnnouncementViewModel
import com.makki.languageapp.fragments.main.grades.GradesViewModel
import com.makki.languageapp.fragments.main.messages.NotificationDetailsViewModel
import com.makki.languageapp.fragments.main.notifications.NotificationViewModel
import com.makki.languageapp.fragments.main.movement.PersonViewModel
import com.makki.languageapp.fragments.main.schedule.ScheduleViewModel
import com.makki.languageapp.fragments.main.study.StudyScreenViewModel
import com.makki.services.declareServiceComponents
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module


/**
 * @author Maksym.Popovych
 */
fun Application.declareKoin() {
    val appModules = module {
        single { ContextProvider(this@declareKoin) }
        viewModel { MainViewModel() }
        viewModel { WelcomeViewModel() }
        viewModel { LoginViewModel(get()) }
        viewModel { SignUpViewModel(get()) }
        viewModel { MainFragViewModel() }
        viewModel { NotificationViewModel() }
        viewModel { NotificationDetailsViewModel() }
        viewModel { PersonViewModel() }
        viewModel { ScheduleViewModel() }
        viewModel { AnnouncementViewModel() }
        viewModel { StudyScreenViewModel() }
        viewModel { GradesViewModel() }
    }

    val apiModules = module {
        declareServiceComponents(this@declareKoin)
    }

    startKoin {
        androidContext(this@declareKoin)
        modules(appModules + apiModules)
    }
}

class ContextProvider(val context: Context)