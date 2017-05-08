package zjm.cst.dhu.registermodule.usecase.base;

import rx.Observable;

/**
 * Created by zjm on 3/23/2017.
 */

public interface BaseUserUseCase<User> {
    Observable<User> login();

    Observable<User> register();
}
