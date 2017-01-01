package com.ggccnu.myjianshu.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
public class CateDetailFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "CateDetailFragment" ;
    private static final int UPDATE_ARTICLES = 1;
    private static final int FIRST_LOAD_ITEMS = 2;
    private static final int LOAD_NEW_ITEMS = 3;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView rvArticle;
    private LinearLayoutManager mLayoutManager;
    private List<Article> mArticleList = new ArrayList<>();

    private int mCid;
    private ArticleAdapter mArticleAdapter;
    private Handler mUpdateItemsHandler,mLoadItemsHandler;
    //use weak reference or can not switch back tab
    //private RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());//TODO: use other get context

    private ViewPager mViewPager;
    private LinePageIndicator mLinePageIndicator;

    private List<ViewPagerSlide> mViewPagerSlideList = new ArrayList<>();
    private static final int MSG_LOAD_SLIDES = 4;
    private Handler mLoadSlidesHandler;


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
        queryArticlesByCategoryID(mCid);
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
        mLoadItemsHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == FIRST_LOAD_ITEMS) {
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
                            /*
                            intent.putExtra(ARTICLE_AUTHOR, mArticleList.get(position).getAuthor());
                            intent.putExtra(ARTICLE_AUHTOR_PIC, mArticleList.get(position).getAuthorIconUrl());
                            intent.putExtra(ARTICLE_TIME, mArticleList.get(position).getReadTimes());
                            intent.putExtra(ARTICLE_TITLE, mArticleList.get(position).getTitle());
                            intent.putExtra(ARTICLE_PICTURE, mArticleList.get(position).getPictureUrl());
                            */
                            intent.putExtra("article_item", mArticleList.get(position));
                            startActivity(intent);
                        }

                        @Override
                        public void onItemLongClick(View view, int position) {

                        }
                    });
                }
            }
        };
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
            queryViewPagerSlideURL();

            mLoadSlidesHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if(msg.what == MSG_LOAD_SLIDES) {
                        // set view pager
                        mViewPager = (ViewPager) view.findViewById(R.id.view_pager_cate_detail_fragment);
                        mViewPager.setAdapter(new ViewPagerFragAdapter(getChildFragmentManager(), mViewPagerSlideList));

                        mLinePageIndicator = (LinePageIndicator) view.findViewById(R.id.line_page_indicator);
                        mLinePageIndicator.setViewPager(mViewPager);

                        mViewPager.setVisibility(View.VISIBLE);
                        mLinePageIndicator.setVisibility(View.VISIBLE);

                        pageAutoSwitcher(4);
                    }
                }
            };
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

// ---------------------------------------------------------------------------

    private void queryArticlesByCategoryID(Integer categoryID) {
        BmobQuery<Article> articlesBmobQuery = new BmobQuery<>();
        articlesBmobQuery.order("createdAt");
        articlesBmobQuery.setLimit(15);
        articlesBmobQuery.addWhereEqualTo("cid", categoryID);
        articlesBmobQuery.findObjects(getActivity(), new FindListener<Article>() {
            @Override
            public void onSuccess(List<Article> list) {
                if (list != null && list.size() > 0) {
                    mArticleList.clear();
                    mArticleList.addAll(list);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Message msg = new Message();
                            msg.what = FIRST_LOAD_ITEMS;
                            //msg.obj = mArticleList;
                            mLoadItemsHandler.sendMessage(msg);
                        }
                    }).start();
                }
                Log.d(TAG, "query new Articles onSuccess" + mCid);
            }

            @Override
            public void onError(int i, String s) {
                Log.d(TAG, "query new Articles onError" + mCid);
            }
        });
    }

    private void queryViewPagerSlideURL( ) {
        BmobQuery<ViewPagerSlide> slideBmobQuery = new BmobQuery<>();
        slideBmobQuery.order("createdAt");
        slideBmobQuery.findObjects(getActivity(), new FindListener<ViewPagerSlide>() {
            @Override
            public void onSuccess(List<ViewPagerSlide> list) {
                if (list != null && list.size() > 0) {
                    if(list.size() != mViewPagerSlideList.size()) {
                        mViewPagerSlideList.addAll(list);
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Message msg = new Message();
                            msg.what = MSG_LOAD_SLIDES;
                            //msg.obj = mArticleList;
                            mLoadSlidesHandler.sendMessage(msg);
                        }
                    }).start();
                }
                Log.d(TAG, "query new viewpagerslides onSuccess");
            }

            @Override
            public void onError(int i, String s) {
                Log.d(TAG, "query new viewpagerslides onError:" + s);
            }
        });
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
        Log.d(TAG, "onActivityCreated" + mCid);
    }

    /**
     * Called when the Fragment is no longer resumed.  This is generally
     * tied to {@link Activity#onPause() Activity.onPause} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause" + mCid);
    }

    /**
     * Called when the Fragment is visible to the user.  This is generally
     * tied to {@link Activity#onStart() Activity.onStart} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart" + mCid);
    }

    /**
     * Called when the fragment is no longer in use.  This is called
     * after {@link #onStop()} and before {@link #onDetach()}.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy" + mCid);
    }

    /**
     * Called when the fragment is no longer attached to its activity.  This
     * is called after {@link #onDestroy()}.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach" + mCid);
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
        Log.d(TAG, "onDestroyView" + mCid);
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
        Log.d(TAG, "onAttach" + mCid);
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
        Log.d(TAG, "onRescume" + mCid);
    }
}
