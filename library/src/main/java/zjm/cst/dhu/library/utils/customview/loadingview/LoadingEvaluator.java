package zjm.cst.dhu.library.utils.customview.loadingview;

import android.animation.TypeEvaluator;

/**
 * Created by zjm on 2017/4/22.
 */

public class LoadingEvaluator implements TypeEvaluator {

    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue) {
        PointInCircle start = (PointInCircle) startValue;
        PointInCircle end = (PointInCircle) endValue;
        PointInCircle res = new PointInCircle(start.getX(), start.getY(), (end.getD() - start.getD()) * fraction + start.getD(), start.getR());
        return res;
    }
}
