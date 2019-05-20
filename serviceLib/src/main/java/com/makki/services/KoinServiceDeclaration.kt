package com.makki.services

import android.app.Application
import com.makki.basic.injects.ProcessorsEnum
import com.makki.basic.injects.singleAsEnum
import com.makki.services.api.base.RxManager
import com.makki.services.api.base.ServiceCommunication
import com.makki.services.api.base.cms.announcements.AnnouncementApi_External
import com.makki.services.api.base.cms.announcements.AnnouncementApi_General
import com.makki.services.api.base.cms.base.ApiConfiguration
import com.makki.services.api.base.cms.base.HttpClient
import com.makki.services.api.base.cms.key.KeyApi
import com.makki.services.api.base.cms.login.LoginApi
import com.makki.services.api.base.cms.movement.MovementApi_Cache
import com.makki.services.api.base.cms.movement.MovementApi_External
import com.makki.services.api.base.cms.movement.MovementApi_General
import com.makki.services.api.base.cms.movement.MovementApi_Persistent
import com.makki.services.api.base.cms.notifications.NotificationApi_External
import com.makki.services.api.base.cms.notifications.NotificationApi_General
import com.makki.services.api.base.cms.notifications.NotificationApi_Persistent
import com.makki.services.api.base.cms.person.PersonApi_External
import com.makki.services.api.base.cms.person.PersonApi_General
import com.makki.services.api.base.cms.person.PersonApi_Persistent
import com.makki.services.api.base.cms.schedule.GradesApi_External
import com.makki.services.api.base.cms.schedule.GradesApi_General
import com.makki.services.api.base.cms.schedule.ScheduleApi_External
import com.makki.services.api.base.cms.schedule.ScheduleApi_General
import com.makki.services.api.base.database.DatabaseComponent
import com.makki.services.api.base.processors.*
import com.makki.services.api.managers.*
import org.koin.core.module.Module

/**
 * @author Maksym.Popovych
 */

fun Module.declareServiceComponents(application: Application) {
    single { ServiceCommunication(application) }
    single { RxManager(application) }
    single { AuthManager() }
    single { NotificationManager() }
    single { PersonManager() }
    single { MovementManager() }
    single { ScheduleManager() }
    single { AnnouncementManager() }
    single { GradesManager() }

    single { HttpClient() }
    single { ApiConfiguration() }
    single { DatabaseComponent(application) }

    single { KeyApi() }

    singleAsEnum(ProcessorsEnum.Login) { LoginProcessor() }
    single { LoginApi() }

    singleAsEnum(ProcessorsEnum.AnnouncementGetAll) { Announcement_GetAll() }
    single { AnnouncementApi_General() }
    single { AnnouncementApi_External() }

    singleAsEnum(ProcessorsEnum.NotificationGetAll) { Notification_GetAll() }
    single { NotificationApi_General() }
    single { NotificationApi_Persistent() }
    single { NotificationApi_External() }

    singleAsEnum(ProcessorsEnum.PersonGetById) { Person_GetById() }
    single { PersonApi_General() }
    single { PersonApi_Persistent() }
    single { PersonApi_External() }

    singleAsEnum(ProcessorsEnum.MovementGetAll) { Movement_GetAll() }
    singleAsEnum(ProcessorsEnum.MovementGetByRange) { Movement_GetByRange() }
    singleAsEnum(ProcessorsEnum.MovementGetByDayId) { Movement_GetByDayId() }
    single { MovementApi_General() }
    single { MovementApi_Cache() }
    single { MovementApi_Persistent() }
    single { MovementApi_External() }

    singleAsEnum(ProcessorsEnum.ScheduleGetAllActive) { Schedule_GetAllActive() }
    single { ScheduleApi_General() }
    single { ScheduleApi_External() }

    singleAsEnum(ProcessorsEnum.GradesGetAll) { Grades_GetAll() }
    single { GradesApi_General() }
    single { GradesApi_External() }

}