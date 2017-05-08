package zjm.cst.dhu.menumodule.usecase;

import java.util.List;

import rx.Observable;
import zjm.cst.dhu.basemodule.api.repository.BaseRepository;
import zjm.cst.dhu.basemodule.model.EncryptType;
import zjm.cst.dhu.menumodule.usecase.base.BaseListEncryptTypeUseCase;

/**
 * Created by zjm on 2017/3/3.
 */

public class ListEncryptTypeUseCase implements BaseListEncryptTypeUseCase<List<EncryptType>> {

    private BaseRepository mBaseRepository;


    public ListEncryptTypeUseCase(BaseRepository baseRepository){
        this.mBaseRepository=baseRepository;
    }

    @Override
    public Observable<List<EncryptType>> getEncryptType() {
        return mBaseRepository.getEncryptType();
    }
}
