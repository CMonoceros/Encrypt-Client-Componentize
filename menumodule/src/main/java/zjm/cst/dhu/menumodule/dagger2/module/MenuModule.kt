package zjm.cst.dhu.menumodule.dagger2.module

import dagger.Module
import dagger.Provides
import zjm.cst.dhu.basemodule.api.repository.BaseRepository
import zjm.cst.dhu.menumodule.MenuContract
import zjm.cst.dhu.menumodule.presenter.MenuPresenter
import zjm.cst.dhu.menumodule.usecase.FileUseCase
import zjm.cst.dhu.menumodule.usecase.ResponseBodyUseCase

/**
 * Created by zjm on 2/9/2017.
 */

@Module
class MenuModule {
    @Provides
    fun provideFileUseCase(baseRepository: BaseRepository): FileUseCase {
        return FileUseCase(baseRepository)
    }

    @Provides
    fun provideResponseBodyUseCase(baseRepository: BaseRepository): ResponseBodyUseCase {
        return ResponseBodyUseCase(baseRepository)
    }

    @Provides
    fun provideMenuPresenter(fileUseCase: FileUseCase, responseBodyUseCase: ResponseBodyUseCase): MenuContract.Presenter {
        return MenuPresenter(fileUseCase, responseBodyUseCase)
    }
}
