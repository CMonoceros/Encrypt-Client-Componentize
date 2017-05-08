package zjm.cst.dhu.menumodule.dagger2.module;

import dagger.Module;
import dagger.Provides;
import zjm.cst.dhu.basemodule.api.repository.BaseRepository;
import zjm.cst.dhu.menumodule.FileListContract;
import zjm.cst.dhu.menumodule.presenter.FileListPresenter;
import zjm.cst.dhu.menumodule.usecase.ListFileUseCase;

/**
 * Created by zjm on 3/2/2017.
 */

@Module
public class FileListModule {
    @Provides
    public ListFileUseCase provideListFileUseCase(BaseRepository baseRepository) {
        return new ListFileUseCase(baseRepository);

    }

    @Provides
    public FileListContract.Presenter provideFileListPresenter(ListFileUseCase listFileUseCase) {
        return new FileListPresenter(listFileUseCase);
    }
}
