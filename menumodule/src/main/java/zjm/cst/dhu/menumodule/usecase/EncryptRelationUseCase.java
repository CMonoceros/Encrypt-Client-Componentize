package zjm.cst.dhu.menumodule.usecase;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import zjm.cst.dhu.basemodule.api.repository.BaseRepository;
import zjm.cst.dhu.basemodule.model.EncryptRelation;
import zjm.cst.dhu.menumodule.usecase.base.BaseEncryptRelationUseCase;

/**
 * Created by zjm on 3/23/2017.
 */

public class EncryptRelationUseCase implements BaseEncryptRelationUseCase<EncryptRelation> {
    private BaseRepository mRepository;
    private Map<String, String> params=new HashMap<>();;

    public EncryptRelationUseCase(BaseRepository baseRepository) {
        this.mRepository = baseRepository;
    }

    public void setFileId(String fileId) {
        params.put("file_id", fileId);
    }

    public void setTypeId(String typeId) {
        params.put("type_id", typeId);
    }

    public void setDesKey(String desKey) {
        params.put("des_key", desKey);
    }

    public void setDesLayer(String desLayer) {
        params.put("des_layer", desLayer);
    }

    @Override
    public Observable<EncryptRelation> encryptFile() {
        return mRepository.encryptFile(params);
    }
}
