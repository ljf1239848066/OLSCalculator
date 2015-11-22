package com.ols.view;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.Scroller;

public class HorizontalListView extends AdapterView<ListAdapter> {
    public static final int REACH_TO_LEFT = 10010;
    public static final int REACH_TO_RIGHT = 10011;
    public static final int REACH_TO_MIDDLE = 10012;
    public static final int REACH_TO_NULL = 10013;

    private Context mContext;
    private int mWidth;
    private int mMaxScrollX;
    private float mLastMotionX;
    private int mTouchSlop;
    private VelocityTracker mVelocityTracker;
    private float mMaximumVelocity;
    private int mMinimumVelocity;
    private int mCurrentPos;

    private OnScrollListener mScrollListener;

    private boolean mIsBeingDragged;
    private Scroller mScroller;
    private float mStartX;
    private float mEndX;
    private boolean canHandle;
    private ListAdapter mAdapter;
    private int mItemCount;
    private int mAllWidth;
    private int mSelectedIndex;

    private OnItemSelectedListener mOnItemSelected;
    private GestureDetector mGesture;
    private boolean mRemainSelected;
    private HeaderListDataObserver mDataObserver;

    public HorizontalListView(Context context) {
        this(context, null);
        mContext = context;
    }

    public HorizontalListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mRemainSelected = false;
        mScroller = new Scroller(context);
        mRemainSelected = attrs.getAttributeBooleanValue(null, "selected_Flag", false);
        initData();
    }

    private void initData() {
        mCurrentPos = REACH_TO_LEFT;
        mIsBeingDragged = false;
        mSelectedIndex = -1;
        mAllWidth = 0;
        mItemCount = 0;
        canHandle = true;
        mGesture = new GestureDetector(getContext(), mOnGesture);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        mMaxScrollX = 0;
        final ViewConfiguration configuration = ViewConfiguration.get(mContext);
        mMinimumVelocity = 600;// configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
    }

    private void getMaxScrollX() {
        mMaxScrollX = 0;
        clearAllSelect(-1);
        if (mAdapter == null)
            return;
        int width = 0;
        for (int i = 0; i < mAdapter.getCount(); i++) {
            View childView = getChildAt(i);
            measureView(childView);
            childView.layout(width, 0, width + childView.getMeasuredWidth(),
                    childView.getMeasuredHeight()*3);
            Log.d("getMaxScrollX:i="+i, "childView.getMeasuredHeight()="+childView.getMeasuredHeight());
            width += childView.getMeasuredWidth();
            ViewGroup.LayoutParams kmp = new ViewGroup.LayoutParams(childView.getMeasuredWidth(),
                    LayoutParams.FILL_PARENT);
            childView.setLayoutParams(kmp);
        }
        mMaxScrollX = width - mWidth;
        if (mMaxScrollX == 0 && mScrollListener != null) {
            mScrollListener.reachToResult(REACH_TO_NULL);
        }
        mAllWidth = width;
        invalidate();
    }

    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mGesture.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mWidth = r - l;
        getMaxScrollX();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        if ((action == MotionEvent.ACTION_MOVE) && (mIsBeingDragged)) {
            return true;
        }
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_MOVE: {
                final float x = ev.getX();
                final int xDiff = (int) Math.abs(x - mLastMotionX);
                if (xDiff > mTouchSlop) {
                    mIsBeingDragged = true;
                    mLastMotionX = x;
                }
                break;
            }
            case MotionEvent.ACTION_DOWN: {
                final float x = ev.getX();
                mLastMotionX = x;
                mStartX = mLastMotionX;
                mIsBeingDragged = !mScroller.isFinished();
                break;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mIsBeingDragged = false;
        }
        return mIsBeingDragged;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if (!canHandle) {
            return false;
        }
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        final int action = event.getAction();
        final float x = event.getX();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                if (!mIsBeingDragged) {
                    return true;
                }
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                mLastMotionX = event.getX();
                mStartX = mLastMotionX;
                break;
            case MotionEvent.ACTION_MOVE:
                onInterceptTouchEvent(event);
                if (mIsBeingDragged) {
                    int deltaX = (int) (mLastMotionX - x);
                    mLastMotionX = x;
                    int scrollX = getScrollX();
                    if (scrollX < -mWidth) {
                        deltaX = 0;
                    } else if (scrollX > mMaxScrollX + mWidth) {
                        deltaX = 0;
                    }
                    scrollBy(deltaX, 0);
                    observePos();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mIsBeingDragged) {
                    final VelocityTracker velocityTracker = mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                    int initialVelocity = (int) velocityTracker.getXVelocity();
                    if ((Math.abs(initialVelocity) > mMinimumVelocity)) {
                        mEndX = event.getX();
                        if (mEndX > mStartX) {
                            fling(Math.abs(initialVelocity) * -1);
                        } else {
                            fling(Math.abs(initialVelocity));
                        }
                    }
                    mIsBeingDragged = false;
                    if (mVelocityTracker != null) {
                        mVelocityTracker.recycle();
                        mVelocityTracker = null;
                    }
                    if (getScrollX() < 0) {
                        scrollTo(0, 0);
                    } else if (getScrollX() > mMaxScrollX) {
                        scrollTo(mMaxScrollX, 0);
                    }
                }
                break;
        }
        return true;
    }

    private void fling(int velocityX) {
        int width = getWidth();
        mScroller.fling(getScrollX(), getScrollY(), velocityX, 0, 0,
                Math.max(0, mAllWidth - width), 0, 0);
        invalidate();
    }

    @Override
    public void computeScroll() {
        // TODO Auto-generated method stub
        if (mScroller.computeScrollOffset()) {
            int oldX = getScrollX();
            int oldY = getScrollY();
            int x = mScroller.getCurrX();
            int y = mScroller.getCurrY();
            if (oldX != x || oldY != y) {
                scrollTo(x, 0);
            }
            observePos();
            postInvalidate();
        }
    }

    private void observePos() {
        try {
            int scrollX = getScrollX();
            if (mCurrentPos != REACH_TO_MIDDLE) {
                if (scrollX > 0 && scrollX < mMaxScrollX) {
                    mCurrentPos = REACH_TO_MIDDLE;
                    if (mScrollListener != null) {
                        mScrollListener.reachToResult(REACH_TO_MIDDLE);
                    }
                }
            }
            if (scrollX <= 0 && mCurrentPos != REACH_TO_LEFT) {
                mCurrentPos = REACH_TO_LEFT;
                if (mScrollListener != null) {
                    mScrollListener.reachToResult(REACH_TO_LEFT);
                }
            } else if (scrollX >= mMaxScrollX && mCurrentPos != REACH_TO_RIGHT) {
                mCurrentPos = REACH_TO_RIGHT;
                if (mScrollListener != null) {
                    mScrollListener.reachToResult(REACH_TO_RIGHT);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        getMaxScrollX();
    }

    public void setCanHandle(boolean b) {
        this.canHandle = b;
    }

    public boolean getCanHandle() {
        return this.canHandle;
    }

    public synchronized void setSelection(int i) {
        if (i < 0)
            i = 0;
        try {
            if (i >= getChildCount()) {
                mScroller.startScroll(getScrollX(), 0, mMaxScrollX - getScrollX(), 0, 500);
                return;
            }
            int mRMax = getChildAt(getChildCount() - 1).getRight();
            int mCR = getChildAt(i).getLeft();
            if (mRMax - mCR > mWidth) {
                int left = getAllLeft(i);
                mScroller.startScroll(getScrollX(), 0, left - getScrollX(), 0, 500);
                // scrollBy(left-getScrollX(),0);
            } else {
                mScroller.startScroll(getScrollX(), 0, mMaxScrollX - getScrollX(), 0, 500);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        requestLayout();
    }

    public void toLeft() {
        int width = 0;
        int pos = 0;
        int currScroll = getScrollX();
        while (width <= currScroll && pos < getChildCount()) {
            width += getChildAt(pos).getMeasuredWidth();
            pos++;
        }
        setSelection(pos);
    }

    public void toRight() {
        int width = 0;
        int pos = 0;
        int currScroll = getScrollX();
        while (width < currScroll && pos < getChildCount()) {
            width += getChildAt(pos).getMeasuredWidth();
            pos++;
        }
        if (pos < 1)
            pos = 1;
        setSelection(pos - 1);
    }

    private int getAllLeft(int pos) {
        int width = 0;
        for (int i = 0; i < pos; i++) {
            width += getChildAt(i).getMeasuredWidth();
        }
        return width;
    }

    public void setAdapter(ListAdapter adapter) {
        if (mAdapter != null) {
            mAdapter.unregisterDataSetObserver(mDataObserver);
        }
        mAdapter = adapter;
        mDataObserver = new HeaderListDataObserver();
        mAdapter.registerDataSetObserver(mDataObserver);
        initData();
        if (mAdapter != null) {
            mItemCount = mAdapter.getCount();
        }
        initView();
        getMaxScrollX();
        requestLayout();
    }

    private void initView() {
        if (mItemCount <= 0)
            return;
        removeAllViewsInLayout();
        for (int i = 0; i < mItemCount; i++) {
            View childView = mAdapter.getView(i, null, this);
            ViewGroup.LayoutParams mlp = childView.getLayoutParams();
            if (mlp == null) {
                mlp = new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT);
            }
            addViewInLayout(childView, -1, mlp);
        }
    }

    public void addOnScrollListener(OnScrollListener listener) {
        this.mScrollListener = listener;
        if (mScrollListener != null) {
            mScrollListener.reachToResult(mCurrentPos);
        }
    }

    public void setOnItemSelectedListener(OnItemSelectedListener itemSelectedListener) {
        this.mOnItemSelected = itemSelectedListener;
    }

    private void clearAllSelect(int except) {
        mSelectedIndex = -1;
        for (int i = 0; i < getChildCount(); i++) {
            if(i==except)continue;
            View chView = getChildAt(i);
            chView.setSelected(false);
        }
    }

    public int getSelectedIndex() {
        return mSelectedIndex;
    }

    public void setRemainSelected(boolean b) {
        mRemainSelected = b;
    }

    class HeaderListDataObserver extends DataSetObserver {

        @Override
        public void onChanged() {
            int currentX = getScrollX();
            reset();
            try {
                if (currentX > mMaxScrollX) {
                    scrollTo(mMaxScrollX, 0);
                } else {
                    scrollTo(currentX, 0);
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

        @Override
        public void onInvalidated() {
            reset();
            scrollTo(0, 0);
        }

    };

    private void reset() {
        initData();
        if (mAdapter != null) {
            mItemCount = mAdapter.getCount();
        }
        initView();
        getMaxScrollX();
        requestLayout();
    }

    @Override
    public ListAdapter getAdapter() {
        // TODO Auto-generated method stub
        return mAdapter;
    }

    @Override
    public View getSelectedView() {
        // TODO Auto-generated method stub
        try {
            if (mAdapter == null)
                return null;
            if (mSelectedIndex > 0 && mSelectedIndex < mAdapter.getCount()) {
                return getChildAt(mSelectedIndex);
            } else {
                return null;
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return null;
        }
    }

    private OnGestureListener mOnGesture = new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
        	 for (int i = 0; i < getChildCount(); i++) {
                 View child = getChildAt(i);
                 if (isEventWithinView(e, child)) {
                     clearAllSelect(i);
                     if (mOnItemSelected != null) {
                         mOnItemSelected.onItemSelected(HorizontalListView.this, child, i, i);
                     }
                     if (mRemainSelected) {
                         mSelectedIndex = i;
                         child.setSelected(true);
                     }
                     break;
                 }
             }
             return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                if (isEventWithinView(e, child)) {
                    clearAllSelect(i);
                    if (mRemainSelected) {
                        mSelectedIndex = i;
                        child.setSelected(true);
                    }
                    break;
                }
            }
        }

        private boolean isEventWithinView(MotionEvent e, View child) {
            Rect viewRect = new Rect();
            int[] childPosition = new int[2];
            child.getLocationOnScreen(childPosition);
            int left = childPosition[0];
            int right = left + child.getWidth();
            int top = childPosition[1];
            int bottom = top + child.getHeight();
            viewRect.set(left, top, right, bottom);
            return viewRect.contains((int) e.getRawX(), (int) e.getRawY());
        }
    };

    public interface OnScrollListener {
        public void reachToResult(int direction);
    }
}
