package zjm.cst.dhu.library.utils.network;


import java.io.File;
import java.io.IOException;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;
import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by zjm on 2017/1/6.
 */

public class UploadFileRequestBody extends RequestBody {

    private RequestBody mRequestBody;
    private Map<String, String> params;
    private BufferedSource mBufferedSource;
    private final ProgressListener listener;
    private BufferedSink bufferedSink;
    private boolean isPublishProgress = true;

    public UploadFileRequestBody(File file, ProgressListener listener) {
        this.mRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        this.listener = listener;
    }

    public UploadFileRequestBody(RequestBody requestBody, ProgressListener listener) {
        this.mRequestBody = requestBody;
        this.listener = listener;
    }


    @Override
    public MediaType contentType() {
        return mRequestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return mRequestBody.contentLength();
    }


    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (bufferedSink == null) {
            //包装
            bufferedSink = Okio.buffer(sink(sink));
        }
        //写入
        mRequestBody.writeTo(bufferedSink);
        //必须调用flush，否则最后一部分数据可能不会被写入
        bufferedSink.flush();
    }

    private Sink sink(Sink sink) {
        return new ForwardingSink(sink) {
            //当前写入字节数
            long bytesWritten = 0L;
            //总字节长度，避免多次调用contentLength()方法
            long contentLength = 0L;

            boolean done = false;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if (contentLength == 0) {
                    //获得contentLength的值，后续不再调用
                    contentLength = mRequestBody.contentLength();
                }
                //增加当前写入的字节数
                bytesWritten += byteCount;
                if (contentLength == bytesWritten) {
                    done = true;
                }
                if (isPublishProgress) {
                    isPublishProgress = false;
                    //回调上传接口
                    Observable.just(bytesWritten).observeOn(Schedulers.io()).subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            listener.onProgress(bytesWritten, contentLength, done);
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            isPublishProgress = true;
                        }
                    });
                }

            }
        };
    }
}
