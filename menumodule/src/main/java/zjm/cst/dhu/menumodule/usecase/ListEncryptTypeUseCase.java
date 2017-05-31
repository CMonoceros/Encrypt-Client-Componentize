package zjm.cst.dhu.menumodule.usecase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import zjm.cst.dhu.basemodule.api.repository.BaseRepository;
import zjm.cst.dhu.basemodule.model.EncryptType;
import zjm.cst.dhu.menumodule.usecase.base.BaseListEncryptTypeUseCase;

/**
 * Created by zjm on 2017/3/3.
 */

public class ListEncryptTypeUseCase implements BaseListEncryptTypeUseCase<List<EncryptType>> {

    private BaseRepository mBaseRepository;
    private String userId;
    private Map<String, String> params = new HashMap<>();


    public ListEncryptTypeUseCase(BaseRepository baseRepository) {
        this.mBaseRepository = baseRepository;
    }

    public void setUserId(String id) {
        this.userId = id;
        params.put("user_id", userId);
    }

    public void setTypeId(String id) {
        params.put("type_id", id);
    }

    public void setRate(String rate) {
        params.put("label_rate", rate);
    }

    @Override
    public Observable<List<EncryptType>> getEncryptType() {
        return mBaseRepository.getEncryptType();
    }

    @Override
    public Observable<List<EncryptType>> getEncryptTypeByOwner() {
        return mBaseRepository.getEncryptTypeByOwner(userId);
    }

    @Override
    public Observable<List<EncryptType>> setEncryptTypeRate() {
        return mBaseRepository.setEncryptTypeRate(params);
    }
}
