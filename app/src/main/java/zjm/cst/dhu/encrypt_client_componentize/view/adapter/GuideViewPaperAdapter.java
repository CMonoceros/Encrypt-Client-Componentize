package zjm.cst.dhu.encrypt_client_componentize.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import zjm.cst.dhu.encrypt_client_componentize.R;

/**
 * Created by zjm on 2017/5/8.
 */

public class GuideViewPaperAdapter extends PagerAdapter {

    private List<View> list;
    private Context context;

    public GuideViewPaperAdapter(List<View> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(list.get(position), 0);
        if (position == list.size() - 1) {
            Button button = (Button) container.findViewById(R.id.b_guide_start);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = new Uri.Builder().scheme("LoginModule").build();
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(intent);
                }
            });
        }
        return list.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(list.get(position));
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
