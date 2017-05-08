package zjm.cst.dhu.library.utils.customview.loadingview;

import android.animation.TimeInterpolator;

/**
 * Created by zjm on 2017/4/22.
 */

public class LoadingInterpolator implements TimeInterpolator {

    @Override
    public float getInterpolation(float input) {
        return input;
    }
}
