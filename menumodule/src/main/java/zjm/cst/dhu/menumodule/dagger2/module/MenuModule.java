package zjm.cst.dhu.menumodule.dagger2.module;

import dagger.Module;
import dagger.Provides;
import zjm.cst.dhu.basemodule.api.repository.BaseRepository;
import zjm.cst.dhu.menumodule.MenuContract;
import zjm.cst.dhu.menumodule.presenter.MenuPresenter;
import zjm.cst.dhu.menumodule.usecase.FileUseCase;
import zjm.cst.dhu.menumodule.usecase.ResponseBodyUseCase;

/**
 * Created by zjm on 2/9/2017.
 */

@Module
public class MenuModule {
    @Provides
    public FileUseCase provideFileUseCase(BaseRepository baseRepository) {
        return new FileUseCase(baseRepository);
    }

    @Provides
    public ResponseBodyUseCase provideResponseBodyUseCase(BaseRepository baseRepository) {
        return new ResponseBodyUseCase(baseRepository);
    }

    @Provides
    public MenuContract.Presenter provideMenuPresenter(FileUseCase fileUseCase, ResponseBodyUseCase responseBodyUseCase) {
        return new MenuPresenter(fileUseCase,responseBodyUseCase);
    }
}
