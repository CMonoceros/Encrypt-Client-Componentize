package zjm.cst.dhu.menumodule.usecase;

import java.util.Map;

import okhttp3.RequestBody;
import rx.Observable;
import zjm.cst.dhu.basemodule.api.repository.BaseRepository;
import zjm.cst.dhu.basemodule.model.File;
import zjm.cst.dhu.menumodule.usecase.base.BaseFileUseCase;

/**
 * Created by zjm on 3/9/2017.
 */

public class FileUseCase implements BaseFileUseCase<File> {
    private BaseRepository mRepository;
    private  Map<String,RequestBody> fileMap;


    public FileUseCase(BaseRepository repository) {
        this.mRepository = repository;
    }


    public void setFileMap( Map<String,RequestBody> fileMap) {
        this.fileMap=fileMap;
    }


    @Override
    public Observable<File> uploadFile() {
        return mRepository.uploadFile(fileMap);
    }
}
