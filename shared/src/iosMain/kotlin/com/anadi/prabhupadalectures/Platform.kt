package com.anadi.prabhupadalectures

import com.anadi.prabhupadalectures.data.lectures.Lecture
import io.ktor.utils.io.*
import platform.UIKit.UIDevice
import java.io.File

actual class Platform actual constructor() {
    actual val platform: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun Lecture.writeChannel(): ByteWriteChannel {
    TODO("Not yet implemented")
}