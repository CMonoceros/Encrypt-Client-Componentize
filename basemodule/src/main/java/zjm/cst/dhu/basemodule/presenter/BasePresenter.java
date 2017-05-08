package zjm.cst.dhu.basemodule.presenter;

import zjm.cst.dhu.basemodule.view.BaseView;

/**
 * Created by zjm on 2017/2/24.
 */

public interface BasePresenter<T extends BaseView> {
    void attachView(T BaseView);

    void detachView();
}
