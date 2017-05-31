package zjm.cst.dhu.library.utils.network


import java.io.File
import java.io.IOException

import okhttp3.MediaType
import okhttp3.RequestBody
import okio.Buffer
import okio.BufferedSink
import okio.BufferedSource
import okio.ForwardingSink
import okio.Okio
import okio.Sink
import rx.Observable
import rx.functions.Action1
import rx.schedulers.Schedulers

/**
 * Created by zjm on 2017/1/6.
 */

class UploadFileRequestBody : RequestBody {

    private var mRequestBody: RequestBody? = null
    private val params: Map<String, String>? = null
    private val mBufferedSource: BufferedSource? = null
    private val listener: ProgressListener
    private var bufferedSink: BufferedSink? = null
    private var isPublishProgress = true

    constructor(file: File, listener: ProgressListener) {
        this.mRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        this.listener = listener
    }

    constructor(requestBody: RequestBody, listener: ProgressListener) {
        this.mRequestBody = requestBody
        this.listener = listener
    }


    override fun contentType(): MediaType? {
        return mRequestBody!!.contentType()
    }

    @Throws(IOException::class)
    override fun contentLength(): Long {
        return mRequestBody!!.contentLength()
    }


    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        if (bufferedSink == null) {
            //包装
            bufferedSink = Okio.buffer(sink(sink))
        }
        //写入
        mRequestBody!!.writeTo(bufferedSink!!)
        //必须调用flush，否则最后一部分数据可能不会被写入
        bufferedSink!!.flush()
    }

    private fun sink(sink: Sink): Sink {
        return object : ForwardingSink(sink) {
            //当前写入字节数
            internal var bytesWritten = 0L
            //总字节长度，避免多次调用contentLength()方法
            internal var contentLength = 0L

            internal var done = false

            @Throws(IOException::class)
            override fun write(source: Buffer, byteCount: Long) {
                super.write(source, byteCount)
                if (contentLength.toInt() == 0) {
                    //获得contentLength的值，后续不再调用
                    contentLength = mRequestBody!!.contentLength()
                }
                //增加当前写入的字节数
                bytesWritten += byteCount
                if (contentLength == bytesWritten) {
                    done = true
                }
                if (isPublishProgress) {
                    isPublishProgress = false
                    //回调上传接口
                    Observable.just(bytesWritten).observeOn(Schedulers.io()).subscribe {
                        listener.onProgress(bytesWritten, contentLength, done)
                        try {
                            Thread.sleep(500)
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        }

                        isPublishProgress = true
                    }
                }

            }
        }
    }
}
