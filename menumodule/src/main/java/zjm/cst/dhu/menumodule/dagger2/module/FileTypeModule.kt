package zjm.cst.dhu.menumodule.dagger2.module

import dagger.Module
import dagger.Provides
import zjm.cst.dhu.basemodule.api.repository.BaseRepository
import zjm.cst.dhu.menumodule.FileTypeContract
import zjm.cst.dhu.menumodule.presenter.FileTypePresenter
import zjm.cst.dhu.menumodule.usecase.EncryptRelationUseCase
import zjm.cst.dhu.menumodule.usecase.ListEncryptTypeUseCase

/**
 * Created by zjm on 2017/3/3.
 */

@Module
class FileTypeModule {
    @Provides
    fun provideListEncryptTypeUseCase(baseRepository: BaseRepository): ListEncryptTypeUseCase {
        return ListEncryptTypeUseCase(baseRepository)
    }

    @Provides
    fun provideEncryptRelationUseCase(baseRepository: BaseRepository): EncryptRelationUseCase {
        return EncryptRelationUseCase(baseRepository)
    }

    @Provides
    fun provideFileTypePresenter(listEncryptTypeUseCase: ListEncryptTypeUseCase, encryptRelationUseCase: EncryptRelationUseCase): FileTypeContract.Presenter {
        return FileTypePresenter(listEncryptTypeUseCase, encryptRelationUseCase)
    }
}
