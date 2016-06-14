package com.ggccnu.myjianshu.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.astuetz.PagerSlidingTabStrip;
import com.ggccnu.myjianshu.R;
import com.ggccnu.myjianshu.adapter.CategoryFragmentAdaptor;
import com.ggccnu.myjianshu.mode.Article;
import com.ggccnu.myjianshu.mode.Category;
import com.ggccnu.myjianshu.mode.Person;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by lishaowei on 16/6/7.
 */
public class ChildCateFragment extends Fragment {

    private static final int UPDATE_VIEWPAGE = 0;
    private static final String TAG = "ChildCateFragment" ;
    /**
     * 分类滑动选项卡
     */
    private PagerSlidingTabStrip mPagerTabs;
    /**
     * 滑动组件
     */
    private ViewPager mPager;
    /**
     * 选项fragment界面
     */
    private ArrayList<Fragment> mCateDetailFragmentList = new ArrayList<>();

    /**
     * 分类数组
     */
    private ArrayList<Category> mCategoryList = new ArrayList<>();

    List<Article> mArticleList = new ArrayList<>();

    private Handler mHandler;
    private Person mPerson;

    LinearLayout mLinearLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 访问网络 获取category和article
//        mHandler = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//
//            }
//        };
        queryCategoryAndArticle();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mLinearLayout = (LinearLayout) inflater.inflate(R.layout.frag_child_category, container, false);
        mPager = (ViewPager) mLinearLayout.findViewById(R.id.pager_pgMain_child_category);
        mPagerTabs = (PagerSlidingTabStrip) mLinearLayout.findViewById(R.id.tabs_main_child_category);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == UPDATE_VIEWPAGE) {
                    initView();
                    mPager.setAdapter(new CategoryFragmentAdaptor(getFragmentManager(), mCateDetailFragmentList, mCategoryList));
                    mPagerTabs.setViewPager(mPager);
                }
            }
        };
        return mLinearLayout;
    }

    private void initView() {

//        mArticleList.add(new Article(0, "Lucy", 0, 2, 5, 1, 3, "日报", "404", "404", "标题"));
//        mArticleList.add(new Article(1, "Jack", 1, 2, 5, 1, 3, "日报", "404", "404", "标题"));
        mCategoryList.add(new Category(0, "child_cate0", "", 0));
        mCategoryList.add(new Category(1, "child_cate1", "", 1));

        for (int i = 0; i < mCategoryList.size(); i++) {
            CateDetailFragment mCateDetailFragment = new CateDetailFragment();
            Bundle mBundle = new Bundle();
            mBundle.putInt("cid", mCategoryList.get(i).getId());
            mBundle.putSerializable("articles", (Serializable) mArticleList);
            mCateDetailFragment.setArguments(mBundle);
            mCateDetailFragmentList.add(mCateDetailFragment);
        }

    }
    private void queryCategoryAndArticle() {
//        BmobQuery<Category> categoryBmobQuery = new BmobQuery<>();
//        categoryBmobQuery.order("createdAt");// 按照时间降序
//        categoryBmobQuery.findObjects(getActivity(), new FindListener<Category>() {
//            @Override
//            public void onSuccess(List<Category> list) {
//                if (list != null && list.size() > 0) {
//                    mCategoryList.addAll(list);
//                }
//            }
//
//            @Override
//            public void onError(int i, String s) {
//                //HankkinUtils.showToast(SplasActivity.this, s);
//            }
//        });
        BmobQuery<Article> articleBmobQuery = new BmobQuery<>();
        articleBmobQuery.order("createdAt");
        articleBmobQuery.findObjects(getActivity(), new FindListener<Article>() {
            @Override
            public void onSuccess(List<Article> list) {
                if (list != null && list.size() > 0) {
                    mArticleList.addAll(list);
                    Log.d(TAG, "queryArticles onSuccess");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Message message = new Message();
                            message.what = UPDATE_VIEWPAGE;
                            mHandler.sendMessage(message);
                        }
                    }).start();
                }
            }

            @Override
            public void onError(int i, String s) {
                Log.d(TAG, "queryArticles onError");
            }
        });
    }
}
