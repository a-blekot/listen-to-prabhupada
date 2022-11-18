package com.listentoprabhupada.common.resources

import android.content.Context
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.desc

fun StringResource.resolve(context: Context) =
    desc().toString(context)
