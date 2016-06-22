package com.ggccnu.myjianshu.fragment;

import android.content.Context;
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

    public ChildCateFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        mLinearLayout = (LinearLayout) inflater.inflate(R.layout.frag_child_category, container, false);
        mPager = (ViewPager) mLinearLayout.findViewById(R.id.pager_pgMain_child_category);
        mPagerTabs = (PagerSlidingTabStrip) mLinearLayout.findViewById(R.id.tabs_main_child_category);
        queryCategoryAndArticle();
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
                    //Log.d(TAG, "queryArticles onSuccess");
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

    /**
     * Called when the fragment's activity has been created and this
     * fragment's view hierarchy instantiated.  It can be used to do final
     * initialization once these pieces are in place, such as retrieving
     * views or restoring state.  It is also useful for fragments that use
     * {@link #setRetainInstance(boolean)} to retain their instance,
     * as this callback tells the fragment when it is fully associated with
     * the new activity instance.  This is called after {@link #onCreateView}
     * and before {@link #onViewStateRestored(Bundle)}.
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");
    }

    /**
     * Called when the Fragment is no longer resumed.  This is generally
     * tied to {@link Activity#onPause() Activity.onPause} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
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
