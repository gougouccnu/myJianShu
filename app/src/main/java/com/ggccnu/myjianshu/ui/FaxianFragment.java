package com.ggccnu.myjianshu.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.ggccnu.myjianshu.R;
import com.ggccnu.myjianshu.adapter.CategoryFragmentAdaptor;
import com.ggccnu.myjianshu.fragment.CateDetailFragment;
import com.ggccnu.myjianshu.fragment.ChildCateFragment;
import com.ggccnu.myjianshu.mode.Article;
import com.ggccnu.myjianshu.mode.Category;
import com.ggccnu.myjianshu.mode.Person;
import com.ggccnu.myjianshu.widget.BaseFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by lishaowei on 16/6/18.
 */
public class FaxianFragment extends BaseFragment {

    private static final int UPDATE_VIEWPAGE = 1;
    private static final String TAG = "FaxianFragment" ;
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

    private Handler mHandler;
    private Person mPerson;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.frag_faxian, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void fetchData() {

    }

    private void initViews(final View view) {
        mCategoryList.clear();
        mCateDetailFragmentList.clear();

        mCategoryList.add(new Category(0, "文章", "", 0));
        mCategoryList.add(new Category(1, "专题", "", 1));
        // 获取子fragment里面cid=0的文章
        queryArticlesByCategoryID(0);
        // 子线程中更新UI
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == UPDATE_VIEWPAGE) {
                    Log.d(TAG, "handle message");
                    // 添加子frag
                    ChildCateFragment mChildCateFragment = new ChildCateFragment();

                    Bundle mBundle = new Bundle();
                    mBundle.putInt("cid", mCategoryList.get(0).getId());
                    mBundle.putSerializable("articles", (Serializable) msg.obj);
                    mChildCateFragment.setArguments(mBundle);
                    mCateDetailFragmentList.add(mChildCateFragment);

                    for (int i = 1; i < mCategoryList.size(); i++) {
                        CateDetailFragment mCateDetailFragment = new CateDetailFragment();
                        Bundle mBundle2 = new Bundle();
                        mBundle2.putInt("cid", mCategoryList.get(i).getId());
                        mBundle2.putSerializable("articles", (Serializable) msg.obj);
                        mCateDetailFragment.setArguments(mBundle2);
                        mCateDetailFragmentList.add(mCateDetailFragment);
                    }

                    //ChildCateFragment mChildCateFragment = new ChildCateFragment();
                    //mCateDetailFragmentList.add(mChildCateFragment);
                    // Initialize the ViewPager and set an adapter
                    mPager = (ViewPager) view.findViewById(R.id.pager_pgMain);
                    mPagerTabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs_main);
                    mPager.setAdapter(new CategoryFragmentAdaptor(getChildFragmentManager(), mCateDetailFragmentList, mCategoryList));
                    mPagerTabs.setViewPager(mPager);
                }
            }
        };
    }



    private void queryArticles() {
        BmobQuery<Article> articlesBmobQuery = new BmobQuery<>();
        articlesBmobQuery.order("createdAt");
        articlesBmobQuery.findObjects(getActivity(), new FindListener<Article>() {
            @Override
            public void onSuccess(List<Article> list) {
                if (list != null && list.size() > 0) {
                    final List<Article> mArticleList = new ArrayList<Article>();
                    for (Article article : list) {
                        mArticleList.add(article);
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Message msg = new Message();
                            msg.what = UPDATE_VIEWPAGE;
                            msg.obj = mArticleList;
                            mHandler.sendMessage(msg);
                        }
                    }).start();
                }
                Log.d(TAG, "queryArticles onSuccess");
            }

            @Override
            public void onError(int i, String s) {
                Log.d(TAG, "queryArticles onError");
            }
        });
    }

    private void queryArticlesByCategoryID(Integer categoryID) {
        BmobQuery<Article> articlesBmobQuery = new BmobQuery<>();
        articlesBmobQuery.order("createdAt");
        articlesBmobQuery.addWhereEqualTo("cid", categoryID);
        articlesBmobQuery.findObjects(getActivity(), new FindListener<Article>() {
            @Override
            public void onSuccess(List<Article> list) {
                if (list != null && list.size() > 0) {
                    final List<Article> mArticleList = new ArrayList<Article>();
                    for (Article article : list) {
                        mArticleList.add(article);
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Message msg = new Message();
                            msg.what = UPDATE_VIEWPAGE;
                            msg.obj = mArticleList;
                            mHandler.sendMessage(msg);
                        }
                    }).start();
                }
                Log.d(TAG, "queryArticles onSuccess");
            }

            @Override
            public void onError(int i, String s) {
                Log.d(TAG, "queryArticles onError");
            }
        });
    }

    private void queryCategory() {
        //showLoadingDialog();
        BmobQuery<Category> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.order("createdAt");// 按照时间降序
        categoryBmobQuery.findObjects(getActivity(), new FindListener<Category>() {
            @Override
            public void onSuccess(List<Category> list) {
                if (list != null && list.size() > 0) {
                    mCategoryList.addAll(list);
                    queryArticles();
                }
                Log.d(TAG, "queryCategory onSuccess");
            }

            @Override
            public void onError(int i, String s) {
                Log.d(TAG, "queryCategory onError");
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
        // save child fragment back stack

    }

    /**
     * Called when the Fragment is visible to the user.  This is generally
     * tied to {@link Activity#onStart() Activity.onStart} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    /**
     * Called when the fragment is no longer in use.  This is called
     * after {@link #onStop()} and before {@link #onDetach()}.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    /**
     * Called when the fragment is no longer attached to its activity.  This
     * is called after {@link #onDestroy()}.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach");
    }

    /**
     * Called when the view previously created by {@link #onCreateView} has
     * been detached from the fragment.  The next time the fragment needs
     * to be displayed, a new view will be created.  This is called
     * after {@link #onStop()} and before {@link #onDestroy()}.  It is called
     * <em>regardless</em> of whether {@link #onCreateView} returned a
     * non-null view.  Internally it is called after the view's state has
     * been saved but before it has been removed from its parent.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView");
    }

    /**
     * Called when a fragment is first attached to its context.
     * {@link #onCreate(Bundle)} will be called after this.
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach");
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     * This is generally
     * tied to {@link Activity#onResume() Activity.onResume} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onRescume");
    }
}
