package zjm.cst.dhu.loginmodule.dagger2.module;

import dagger.Module;
import dagger.Provides;
import zjm.cst.dhu.basemodule.api.repository.BaseRepository;
import zjm.cst.dhu.loginmodule.LoginContract;
import zjm.cst.dhu.loginmodule.presenter.LoginPresenter;
import zjm.cst.dhu.loginmodule.usecase.UserUseCase;

/**
 * Created by zjm on 2017/2/24.
 */
@Module
public class LoginModule {
    @Provides
    public UserUseCase provideUserUseCase(BaseRepository baseRepository) {
        return new UserUseCase(baseRepository);
    }

    @Provides
    public LoginContract.Presenter provideLoginPresenter(UserUseCase userUseCase) {
        return new LoginPresenter(userUseCase);
    }
}
