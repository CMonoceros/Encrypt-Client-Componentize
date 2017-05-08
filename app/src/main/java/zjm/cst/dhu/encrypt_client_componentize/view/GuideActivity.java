package zjm.cst.dhu.encrypt_client_componentize.view;

import android.Manifest;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import zjm.cst.dhu.basemodule.view.BaseActivity;
import zjm.cst.dhu.encrypt_client_componentize.R;
import zjm.cst.dhu.encrypt_client_componentize.databinding.MainBinding;
import zjm.cst.dhu.encrypt_client_componentize.view.adapter.GuideViewPaperAdapter;
import zjm.cst.dhu.library.utils.others.PermissionUtil;

/**
 * Created by zjm on 2017/5/7.
 */

public class GuideActivity extends BaseActivity {
    private ViewPager viewPager;
    private GuideViewPaperAdapter guideViewPaperAdapter;
    private List<View> viewPaperItemList = new ArrayList<>();
    private List<ImageView> guideImageViewList = new ArrayList<>();
    private int nowPosition = 0;
    private int guidePointMargin = 10;
    private int guidePointSize = 20;

    private ViewPager.OnPageChangeListener onPageChangeListener;

    @Override
    protected void injectDependences() {

    }

    public void requestPermission() {
        List<String> permissionList = new ArrayList<>();
        permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissionList.add(Manifest.permission.READ_PHONE_STATE);
        PermissionUtil.requestPermission(this, permissionList);
    }

    @Override
    protected void dataBinding() {
        MainBinding binding = DataBindingUtil.setContentView(this, R.layout.main);
        viewPager = binding.vpGuide;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding();
        setupView();
        addGuidePoint();
        requestPermission();
    }

    private void setupView() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        viewPaperItemList.add(layoutInflater.inflate(R.layout.guide_layout_1, null));
        viewPaperItemList.add(layoutInflater.inflate(R.layout.guide_layout_2, null));
        guideViewPaperAdapter = new GuideViewPaperAdapter(viewPaperItemList, this);
        viewPager.setAdapter(guideViewPaperAdapter);
        onPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                guideImageViewList.get(position).setBackgroundResource(R.drawable.guide_point_accent);
                guideImageViewList.get(nowPosition).setBackgroundResource(R.drawable.guide_point_white);
                nowPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        viewPager.addOnPageChangeListener(onPageChangeListener);
    }

    private void addGuidePoint() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.ll_guide_point);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        for (int i = 0; i < viewPaperItemList.size(); i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(R.drawable.guide_point_white);
            params.leftMargin = guidePointMargin;
            params.rightMargin = guidePointMargin;
            params.height = guidePointSize;
            params.width = guidePointSize;
            linearLayout.addView(imageView, params);
            guideImageViewList.add(imageView);
        }

        guideImageViewList.get(nowPosition).setBackgroundResource(R.drawable.guide_point_accent);
    }
}
