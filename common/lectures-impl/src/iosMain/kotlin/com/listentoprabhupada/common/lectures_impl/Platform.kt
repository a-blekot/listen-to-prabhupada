package com.listentoprabhupada.common.lectures_impl

import com.listentoprabhupada.common.data.Lecture
import com.listentoprabhupada.common.lectures_impl.utils.ShareAction
import io.ktor.utils.io.*
import okio.FileSystem
import platform.UIKit.UIDevice

actual class Platform actual constructor() {
    actual val platform: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun Lecture.writeChannel(): ByteWriteChannel {
    TODO("Not yet implemented")
}

actual val Lecture.fileSystem: FileSystem
    get() = FileSystem.SYSTEM

actual val ShareAction.deepLink: String
    get() = TODO("Not yet implemented")