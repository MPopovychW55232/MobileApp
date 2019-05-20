package com.makki.basic.injects

import org.koin.core.definition.Definition
import org.koin.core.module.Module
import org.koin.core.qualifier.named

/**
 * @author Maksym.Popovych
 */

inline fun <reified T> Module.singleAsEnum(qualifier: Enum<*>, noinline definition: Definition<T>) =
    this.single(named(qualifier.name), definition = definition)