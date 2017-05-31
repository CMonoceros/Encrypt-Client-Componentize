package zjm.cst.dhu.library.utils.network


import java.io.IOException

import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.Buffer
import okio.BufferedSource
import okio.ForwardingSource
import okio.Okio
import okio.Source
import rx.Observable
import rx.functions.Action1
import rx.schedulers.Schedulers

/**
 * Created by zjm on 2017/1/10.
 */

class DownloadFileResponseBody(private val responseBody: ResponseBody, private val listener: ProgressListener) : ResponseBody() {
    private var bufferedSource: BufferedSource? = null
    private var isPublishProgress = true

    override fun contentType(): MediaType? {
        return responseBody.contentType()
    }

    override fun contentLength(): Long {
        return responseBody.contentLength()
    }

    override fun source(): BufferedSource? {
        if (null == bufferedSource) {
            bufferedSource = Okio.buffer(source(responseBody.source()))
        }
        return bufferedSource
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            internal var totalBytesRead = 0L

            @Throws(IOException::class)
            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                totalBytesRead += if (bytesRead.toInt() != -1) bytesRead else 0
                val done = bytesRead.toInt() == -1
                if (isPublishProgress) {
                    isPublishProgress = false
                    //回调上传接口
                    Observable.just(totalBytesRead).observeOn(Schedulers.io()).subscribe {
                        listener.onProgress(totalBytesRead, responseBody.contentLength(), done)
                        try {
                            Thread.sleep(500)
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        }

                        isPublishProgress = true
                    }
                }
                return bytesRead
            }
        }
    }
}
