package zjm.cst.dhu.library.utils.network;


import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;
import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by zjm on 2017/1/10.
 */

public class DownloadFileResponseBody extends ResponseBody {

    private final ResponseBody responseBody;
    private final ProgressListener listener;
    private BufferedSource bufferedSource;
    private boolean isPublishProgress = true;

    public DownloadFileResponseBody(ResponseBody responseBody, ProgressListener listener) {
        this.responseBody = responseBody;
        this.listener = listener;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (null == bufferedSource) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                final boolean done = (bytesRead == -1);
                if (isPublishProgress) {
                    isPublishProgress = false;
                    //回调上传接口
                    Observable.just(totalBytesRead).observeOn(Schedulers.io()).subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            listener.onProgress(totalBytesRead, responseBody.contentLength(), done);
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            isPublishProgress = true;
                        }
                    });
                }
                return bytesRead;
            }
        };
    }
}
