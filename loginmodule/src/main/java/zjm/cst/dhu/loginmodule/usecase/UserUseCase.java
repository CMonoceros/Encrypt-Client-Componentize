package zjm.cst.dhu.loginmodule.usecase;

import rx.Observable;
import zjm.cst.dhu.basemodule.api.repository.BaseRepository;
import zjm.cst.dhu.basemodule.model.User;
import zjm.cst.dhu.loginmodule.usecase.base.BaseUserUseCase;

/**
 * Created by zjm on 2017/2/24.
 */

public class UserUseCase implements BaseUserUseCase<User> {

    private BaseRepository mRepository;
    private User mUser;

    public UserUseCase(BaseRepository repository){
        this.mRepository=repository;
    }

    public void setUser(User user){
        this.mUser=user;
    }

    @Override
    public Observable<User> login() {
        return mRepository.login(mUser);
    }

    @Override
    public Observable<User> register() {
        return mRepository.register(mUser);
    }
}
