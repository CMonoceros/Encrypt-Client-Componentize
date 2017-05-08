package zjm.cst.dhu.registermodule.dagger2.module;

import dagger.Module;
import dagger.Provides;
import zjm.cst.dhu.basemodule.api.repository.BaseRepository;
import zjm.cst.dhu.registermodule.RegisterContract;
import zjm.cst.dhu.registermodule.presenter.RegisterPresenter;
import zjm.cst.dhu.registermodule.usecase.UserUseCase;

/**
 * Created by zjm on 2017/3/1.
 */

@Module
public class RegisterModule {
    @Provides
    public UserUseCase provideUserUseCase(BaseRepository baseRepository) {
        return new UserUseCase(baseRepository);
    }

    @Provides
    public RegisterContract.Presenter provideRegisterPresenter(UserUseCase userUseCase) {
        return new RegisterPresenter(userUseCase);
    }
}
