package com.listentoprabhupada.common.results_impl

import io.ktor.client.utils.*
import io.ktor.utils.io.*
import io.ktor.utils.io.bits.*
import kotlinx.cinterop.*
import kotlinx.coroutines.*
import platform.Foundation.*
import platform.posix.memcpy
import platform.posix.uint8_tVar

fun byteWriteChannel(fileName: String): ByteWriteChannel {
    val tempDirUrl = NSFileManager.defaultManager.temporaryDirectory
    val fileUrl = tempDirUrl.URLByAppendingPathComponent(fileName) ?: error("Cannot create temp file")
    val output = NSOutputStream.outputStreamWithURL(fileUrl, false) ?: error("Cannot create temp file")
    output.open()
    return output.toByteWriteChannel(fileName)
}

@OptIn(ExperimentalCoroutinesApi::class)
private fun NSOutputStream.toByteWriteChannel(fileName: String): ByteWriteChannel {
    val outputStream = this
    val coroutineContext = newSingleThreadContext("write-channel-$fileName")
    return CoroutineScope(coroutineContext).reader(autoFlush = true) {
        memScoped {
            try {
                val buffer = allocArray<uint8_tVar>(DEFAULT_HTTP_BUFFER_SIZE.convert())
                copyChannel(channel, outputStream, buffer)
            } catch (cause: Throwable) {
                cause.printStackTrace()
                channel.cancel(cause)
            } finally {
                outputStream.close()
                coroutineContext.close()
            }
        }
    }.channel
}

private suspend fun copyChannel(input: ByteReadChannel, output: NSOutputStream, buffer: CArrayPointer<uint8_tVar>) {
    while (!input.isClosedForRead) {
        val memoryBuffer = Memory.of(buffer, DEFAULT_HTTP_BUFFER_SIZE)
        input.read { source, start, end ->
            val size = end - start
            source.copyTo(memoryBuffer, start, size, 0)
            memcpy(buffer, source.pointer, DEFAULT_HTTP_BUFFER_SIZE.convert())
            output.write(buffer, size.convert())
            size.toInt()
        }
    }
}