package com.ggccnu.myjianshu.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ggccnu.myjianshu.R;
import com.ggccnu.myjianshu.adapter.ArticleAdapter;
import com.ggccnu.myjianshu.adapter.ViewPagerFragAdapter;
import com.ggccnu.myjianshu.mode.Article;
import com.ggccnu.myjianshu.mode.ViewPagerSlide;
import com.ggccnu.myjianshu.ui.CateDetailActivity;
import com.ggccnu.myjianshu.utils.ArticleCallbackListener;
import com.ggccnu.myjianshu.utils.BmobRequestUtil;
import com.ggccnu.myjianshu.utils.ViewPagerSlideCallbackListener;
import com.ggccnu.myjianshu.widget.BaseFragment;
import com.viewpagerindicator.LinePageIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by lishaowei on 16/6/4.
 */
public class TopicFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "TopicFragment" ;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView rvArticle;
    private LinearLayoutManager mLayoutManager;
    private List<Article> mArticleList = new ArrayList<>();

    private int mCid;
    private ArticleAdapter mArticleAdapter;
    //use weak reference or can not switch back tab
    //private RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());//TODO: use other get context

    private ViewPager mViewPager;
    private LinePageIndicator mLinePageIndicator;

    private List<ViewPagerSlide> mViewPagerSlideList = new ArrayList<>();

    private Timer timer;
    // slide page initial position
    private int page = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCid = getArguments().getInt("cid");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_cate_detail_fragment, container, false);
        BmobRequestUtil.queryArticlesByCategoryID(getActivity(), mCid, new ArticleCallbackListener() {
            @Override
            public void onFinish(List<Article> list) {
                mArticleList.clear();
                mArticleList.addAll(list);
                mArticleAdapter = new ArticleAdapter(mArticleList, getActivity());
                rvArticle.setAdapter(mArticleAdapter);
                // RecyclerView inside Scrollview时，让滑动smooth
                //rvArticle.setNestedScrollingEnabled(false);
                mArticleAdapter.setOnItemClickLitener(new ArticleAdapter.OnItemClickLitener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toast.makeText(getActivity(), "item " + position + " clicked", Toast.LENGTH_LONG).show();
                        // 跳转到article详细页面
                        Intent intent = new Intent(getContext(), CateDetailActivity.class);
                        intent.putExtra("article_item", mArticleList.get(position));
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });
            }

            @Override
            public void onError(String s) {

            }
        });
        initViews(view);
        return view;
    }

    @Override
    public void onRefresh() {
        queryNewArticlesByCategoryID(mCid);
        mSwipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }

    private void initViews(final View view) {
        rvArticle = (RecyclerView) view.findViewById(R.id.rv_article);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swpRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mLayoutManager = new LinearLayoutManager(getActivity());
        rvArticle.setLayoutManager(mLayoutManager);
        rvArticle.setOnScrollListener(new RecyclerView.OnScrollListener() {
            /**
             * Callback method to be invoked when the RecyclerView has been scrolled. This will be
             * called after the scroll has completed.
             * <p>
             * This callback will also be called if visible item range changes after a layout
             * calculation. In that case, dx and dy will be 0.
             *
             * @param recyclerView The RecyclerView which scrolled.
             * @param dx           The amount of horizontal scroll.
             * @param dy           The amount of vertical scroll.
             */
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
                int totalItemCount = mLayoutManager.getItemCount();
                if (lastVisibleItem >= totalItemCount - 5 && dy > 0) {
                    boolean isLoadingMore = false;
                    if (isLoadingMore) {
                        Log.d(TAG, "ignore manually update");
                    } else {
                        queryNewArticlesByCategoryID(mCid);
                        isLoadingMore = false;
                    }
                }
            }
        });
        if (mCid == 0) {
            //请求服务器获取picture slide URL
            BmobRequestUtil.queryViewPagerSlideURL(getActivity(), new ViewPagerSlideCallbackListener() {
                @Override
                public void onFinish(List<ViewPagerSlide> list) {
                    if (list != null && list.size() > 0) {
                        if (list.size() != mViewPagerSlideList.size()) {
                            mViewPagerSlideList.addAll(list);
                        }
                        // set view pager
                        mViewPager = (ViewPager) view.findViewById(R.id.view_pager_cate_detail_fragment);
                        mViewPager.setAdapter(new ViewPagerFragAdapter(getChildFragmentManager(), mViewPagerSlideList));

                        mLinePageIndicator = (LinePageIndicator) view.findViewById(R.id.line_page_indicator);
                        mLinePageIndicator.setViewPager(mViewPager);

                        mViewPager.setVisibility(View.VISIBLE);
                        mLinePageIndicator.setVisibility(View.VISIBLE);

                        pageAutoSwitcher(4);
                    }
                    Log.d(TAG, "query new viewpagerslides onSuccess");
                }

                @Override
                public void onError(String s) {
                    Log.d(TAG, "query new viewpagerslides onError:" + s);
                }
            });
        }
    }

    private void pageAutoSwitcher(int seconds) {
        timer = new Timer(); // At this line a new Thread will be created
        timer.scheduleAtFixedRate(new RemindTask(), 0, seconds * 1000); // delay in milliseconds
    }

    // this is an inner class...
    class RemindTask extends TimerTask {

        @Override
        public void run() {

            // As the TimerTask run on a seprate thread from UI thread we have
            // to call runOnUiThread to do work on UI thread.
            getActivity().runOnUiThread(new Runnable() {
                public void run() {

                    if (page > mViewPagerSlideList.size()) { // In my case the number of pages are 2
                        page = 0;
                        //timer.cancel();
                        // Showing a toast for just testing purpose
                        //Toast.makeText(getContext(), "Timer stoped",
                          //      Toast.LENGTH_LONG).show();
                    } else {
                        mViewPager.setCurrentItem(page++);
                    }
                }
            });
        }
    }

    private void queryNewArticlesByCategoryID(Integer categoryID) {
        BmobQuery<Article> articlesBmobQuery = new BmobQuery<>();
        articlesBmobQuery.order("createdAt");
        articlesBmobQuery.addWhereEqualTo("cid", categoryID);
        articlesBmobQuery.findObjects(getActivity(), new FindListener<Article>() {
            @Override
            public void onSuccess(List<Article> list) {
                if (list != null && list.size() > 0) {
                    mArticleList.clear();
                    mArticleList.addAll(list);
                    // notify data changed
                    mArticleAdapter.notifyDataSetChanged();
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                Log.d(TAG, "query new Articles onSuccess" + mCid);
            }

            @Override
            public void onError(int i, String s) {
                Log.d(TAG, "query new Articles onError" + mCid);
            }
        });
    }

    @Override
    public void fetchData() {

    }
}
