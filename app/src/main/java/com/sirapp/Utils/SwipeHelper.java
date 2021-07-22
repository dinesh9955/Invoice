package com.sirapp.Utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public abstract class SwipeHelper extends ItemTouchHelper.SimpleCallback {

    public static final int BUTTON_WIDTH2 = 400;
    private RecyclerView recycler_invoices2;
    private List<UnderlayButton> buttons2;
    private GestureDetector gestureDetector2;
    private int swipedPos2 = -1;
    private float swipeThreshold2 = 0.5f;
    private Map<Integer, List<UnderlayButton>> buttonsBuffer2;
    private Queue<Integer> recoverQueue2;

    private GestureDetector.SimpleOnGestureListener gestureListener2 = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            for (UnderlayButton button : buttons2) {
                if (button.onClick(e.getX(), e.getY()))
                    break;
            }
            return true;
        }
    };

    private View.OnTouchListener onTouchListener2 = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent e) {
            if (swipedPos2 < 0) return false;
            Point point = new Point((int) e.getRawX(), (int) e.getRawY());

            RecyclerView.ViewHolder swipedViewHolder = recycler_invoices2.findViewHolderForAdapterPosition(swipedPos2);

            if(swipedViewHolder != null){
                View swipedItem = swipedViewHolder.itemView;
                Rect rect = new Rect();
                swipedItem.getGlobalVisibleRect(rect);
                if (e.getAction() == MotionEvent.ACTION_DOWN || e.getAction() == MotionEvent.ACTION_UP || e.getAction() == MotionEvent.ACTION_MOVE) {
                    if (rect.top < point.y && rect.bottom > point.y)
                        gestureDetector2.onTouchEvent(e);
                    else {
                        recoverQueue2.add(swipedPos2);
                        swipedPos2 = -1;
                        recoverSwipedItem();
                    }
                }
            }

            return false;
        }
    };

    public SwipeHelper(Context context) {
        super(0, ItemTouchHelper.RIGHT);
        this.buttons2 = new ArrayList<>();
        this.gestureDetector2 = new GestureDetector(context, gestureListener2);
        buttonsBuffer2 = new HashMap<>();
        recoverQueue2 = new LinkedList<Integer>() {
            @Override
            public boolean add(Integer o) {
                if (contains(o))
                    return false;
                else
                    return super.add(o);
            }
        };
    }


    @Override
    public boolean onMove(RecyclerView recycler_invoices, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int pos = viewHolder.getAdapterPosition();

        if (swipedPos2 != pos)
            recoverQueue2.add(swipedPos2);

        swipedPos2 = pos;

        if (buttonsBuffer2.containsKey(swipedPos2))
            buttons2 = buttonsBuffer2.get(swipedPos2);
        else
            buttons2.clear();

        buttonsBuffer2.clear();
        swipeThreshold2 = 0.5f * buttons2.size() * BUTTON_WIDTH2;
        recoverSwipedItem();
    }

    @Override
    public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
        return swipeThreshold2;
    }

    @Override
    public float getSwipeEscapeVelocity(float defaultValue) {
        return 0.1f * defaultValue;
    }

    @Override
    public float getSwipeVelocityThreshold(float defaultValue) {
        return 5.0f * defaultValue;
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recycler_invoices, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        int pos = viewHolder.getAdapterPosition();
        float translationX = dX;
        View itemView = viewHolder.itemView;

        if (pos < 0) {
            swipedPos2 = pos;
            return;
        }

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
//            if (dX < 0) {
//                List<UnderlayButton> buffer = new ArrayList<>();
//
//                if (!buttonsBuffer.containsKey(pos)) {
//                    instantiateUnderlayButton(viewHolder, buffer);
//                    buttonsBuffer.put(pos, buffer);
//                } else {
//                    buffer = buttonsBuffer.get(pos);
//                }
//
//                translationX = dX * buffer.size() * BUTTON_WIDTH / itemView.getWidth();
//                drawButtons(c, itemView, buffer, pos, translationX);
//            }

            if(dX > 0) {
                List<SwipeHelper.UnderlayButton> buffer = new ArrayList<>();

                if (!buttonsBuffer2.containsKey(pos)){
                    instantiateUnderlayButton(viewHolder, buffer);
                    buttonsBuffer2.put(pos, buffer);
                }
                else {
                    buffer = buttonsBuffer2.get(pos);
                }

                translationX = dX * buffer.size() * BUTTON_WIDTH2 / itemView.getWidth();
                drawButtons(c, itemView, buffer, pos, translationX);
            }
        }

        super.onChildDraw(c, recycler_invoices, viewHolder, translationX, dY, actionState, isCurrentlyActive);
    }

    private synchronized void recoverSwipedItem() {
        while (!recoverQueue2.isEmpty()) {
            int pos = recoverQueue2.poll();
            if (pos > -1) {
                recycler_invoices2.getAdapter().notifyItemChanged(pos);
            }
        }
    }

    private void drawButtons(Canvas c, View itemView, List<SwipeHelper.UnderlayButton> buffer, int pos, float dX) {
//        float right = itemView.getRight();
//        float dButtonWidth = (-1) * dX / buffer.size();

        float right = itemView.getRight();
        float left = itemView.getLeft();
        float dButtonWidth = (-1) * dX / buffer.size();

        for (SwipeHelper.UnderlayButton button : buffer) {
//            float left = right - dButtonWidth;
//            button.onDraw(
//                    c,
//                    new RectF(
//                            left,
//                            itemView.getTop(),
//                            right,
//                            itemView.getBottom()
//                    ),
//                    pos
//            );
//
//            right = left;

            if (dX > 0) {
                right = left - dButtonWidth;
                button.onDraw2(c,
                        new RectF(
                                right,
                                itemView.getTop(),
                                left,
                                itemView.getBottom()
                        ), pos, dX
                );
                left = right;
            }
        }
    }

    public void attachToRecyclerView(RecyclerView recycler_invoices) {
        this.recycler_invoices2 = recycler_invoices;
        this.recycler_invoices2.setOnTouchListener(onTouchListener2);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(this);
        itemTouchHelper.attachToRecyclerView(this.recycler_invoices2);
    }

    public abstract void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<SwipeHelper.UnderlayButton> underlayButtons);

    public interface UnderlayButtonClickListener2 {
        void onClick(int pos);
    }

    public static class UnderlayButton {
        private static final String TAG = "UnderlayButton";
        private String text;
        private int imageResId;
        private int color;
        private int pos;
        private RectF clickRegion;
        private UnderlayButtonClickListener2 clickListener;

        public UnderlayButton(String text, int imageResId, int color, UnderlayButtonClickListener2 clickListener) {
            this.text = text;
            this.imageResId = imageResId;
            this.color = color;
            this.clickListener = clickListener;
        }

        public boolean onClick(float x, float y) {
            Log.e(TAG, "onClick"+pos);
            clickListener.onClick(pos);
//            if (clickRegion != null && clickRegion.contains(x, y)) {
//                clickListener.onClick(pos);
//                return true;
//            }

            return false;
        }

        public void onDraw(Canvas c, RectF rect, int pos) {
            Paint p = new Paint();

            // Draw background
            p.setColor(color);
            c.drawRect(rect, p);

            // Draw Text
            p.setColor(Color.WHITE);
            //p.setTextSize(LayoutHelper.getPx(MyApplication.getAppContext(), 12));
            p.setTextSize(Resources.getSystem().getDisplayMetrics().density * 12);

            Rect r = new Rect();
            float cHeight = rect.height();
            float cWidth = rect.width();
            p.setTextAlign(Paint.Align.LEFT);
            p.getTextBounds(text, 0, text.length(), r);
            float x = cWidth / 2f - r.width() / 2f - r.left;
            float y = cHeight / 2f + r.height() / 2f - r.bottom;
            c.drawText(text, rect.left + x, rect.top + y, p);

            clickRegion = rect;
            this.pos = pos;
        }


        public void onDraw2(Canvas c, RectF rect, int pos, float dX) {
            Paint p = new Paint();

            // Draw background

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {


                if (dX < 0) {

                    //Boton derecho
                    c.drawRect(rect, p);
                    p.setColor(Color.BLUE);

                }
                else{

                    p.setColor(color);
                    c.drawRect(rect, p);

                    // Draw Text
                    p.setColor(Color.WHITE);
                    p.setTextSize(Resources.getSystem().getDisplayMetrics().density * 12);

                    Rect r = new Rect();
                    float cHeight = rect.height();
                    float cWidth = rect.width();
                    p.setTextAlign(Paint.Align.LEFT);
                    p.getTextBounds(text, 0, text.length(), r);
                    float x = cWidth / 2f - r.width() / 2f - r.left;
                    float y = cHeight / 2f + r.height() / 2f - r.bottom;
                    c.drawText(text, rect.left + x, rect.top + y, p);

                    clickRegion = rect;
                    this.pos = pos;

                }

                clickRegion = rect;
                this.pos = pos;
            }

        }

    }
}
