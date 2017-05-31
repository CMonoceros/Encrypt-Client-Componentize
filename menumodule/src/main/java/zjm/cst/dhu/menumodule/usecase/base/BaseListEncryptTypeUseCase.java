package zjm.cst.dhu.menumodule.usecase.base;

import rx.Observable;

/**
 * Created by zjm on 3/23/2017.
 */

public interface BaseListEncryptTypeUseCase<T> {
    Observable<T> getEncryptType();

    Observable<T> getEncryptTypeByOwner();

    Observable<T> setEncryptTypeRate();
}
