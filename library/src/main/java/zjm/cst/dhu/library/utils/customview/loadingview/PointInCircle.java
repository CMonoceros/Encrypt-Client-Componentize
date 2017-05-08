package zjm.cst.dhu.library.utils.customview.loadingview;

/**
 * Created by zjm on 2017/4/22.
 */

public class PointInCircle {
    private float x;
    private float y;
    private float d;
    private float r;

    public PointInCircle(float x, float y, float d, float r) {
        this.x = x;
        this.y = y;
        this.d = d;
        this.r = r;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getD() {
        return d;
    }

    public float getR() {
        return r;
    }
}
