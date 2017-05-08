package zjm.cst.dhu.menumodule.dagger2.module;

import dagger.Module;
import dagger.Provides;
import zjm.cst.dhu.basemodule.api.repository.BaseRepository;
import zjm.cst.dhu.menumodule.FileTypeContract;
import zjm.cst.dhu.menumodule.presenter.FileTypePresenter;
import zjm.cst.dhu.menumodule.usecase.EncryptRelationUseCase;
import zjm.cst.dhu.menumodule.usecase.ListEncryptTypeUseCase;

/**
 * Created by zjm on 2017/3/3.
 */

@Module
public class FileTypeModule {
    @Provides
    public ListEncryptTypeUseCase provideListEncryptTypeUseCase(BaseRepository baseRepository) {
        return new ListEncryptTypeUseCase(baseRepository);
    }

    @Provides
    public EncryptRelationUseCase provideEncryptRelationUseCase(BaseRepository baseRepository) {
        return new EncryptRelationUseCase(baseRepository);
    }

    @Provides
    public FileTypeContract.Presenter provideFileTypePresenter(ListEncryptTypeUseCase listEncryptTypeUseCase, EncryptRelationUseCase encryptRelationUseCase) {
        return new FileTypePresenter(listEncryptTypeUseCase, encryptRelationUseCase);
    }
}
