package com.sirapp.PO;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
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

    public static final int BUTTON_WIDTH3 = 300;
    public static final int BUTTON_WIDTH4 = 400;
    private static final String TAG = "SwipeHelper";
    private RecyclerView recyclerView;
    private List<UnderlayButton> buttons;
    private GestureDetector gestureDetector;
    private GestureDetector gestureDetector2;
    private int swipedPos = -1;
    private float swipeThreshold = 0.5f;
    private Map<Integer, List<UnderlayButton>> buttonsBuffer;
    private Queue<Integer> recoverQueue;

    private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener(){
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            for (UnderlayButton button : buttons){
                if(button.onClick(e.getX(), e.getY()))
                    break;
            }

            return true;
        }
    };

    private GestureDetector.SimpleOnGestureListener gestureListener2 = new GestureDetector.SimpleOnGestureListener(){
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            for (UnderlayButton button : buttons){
                if(button.onClick2(e.getX(), e.getY()))
                    break;
            }

            return true;
        }
    };

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent e) {
            if (swipedPos < 0) return false;
            Point point = new Point((int) e.getRawX(), (int) e.getRawY());

            RecyclerView.ViewHolder swipedViewHolder = recyclerView.findViewHolderForAdapterPosition(swipedPos);
            View swipedItem = swipedViewHolder.itemView;
            Rect rect = new Rect();
            swipedItem.getGlobalVisibleRect(rect);

            if (e.getAction() == MotionEvent.ACTION_DOWN || e.getAction() == MotionEvent.ACTION_UP ||e.getAction() == MotionEvent.ACTION_MOVE) {
                if (rect.top < point.y && rect.bottom > point.y) {
                    gestureDetector.onTouchEvent(e);
                    gestureDetector2.onTouchEvent(e);
                }
                //else if(rect.top > point.y && rect.bottom < point.y){                }
                else {
                    recoverQueue.add(swipedPos);
                    swipedPos = -1;
                    recoverSwipedItem();
                }
            }
            return false;
        }
    };

    public SwipeHelper(Context context, RecyclerView recyclerView) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.recyclerView = recyclerView;
        this.buttons = new ArrayList<>();
        this.gestureDetector = new GestureDetector(context, gestureListener);
        this.gestureDetector2 = new GestureDetector(context, gestureListener2);
        this.recyclerView.setOnTouchListener(onTouchListener);
        buttonsBuffer = new HashMap<>();
        recoverQueue = new LinkedList<Integer>(){
            @Override
            public boolean add(Integer o) {
                if (contains(o)){
                    return false;
                }

                else{
                    return super.add(o);
                }

            }
        };
        attachSwipe();
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int pos = viewHolder.getAdapterPosition();
        Log.e(TAG, "onSwipedposAA "+pos);

        if (swipedPos != pos)
            recoverQueue.add(swipedPos);

        swipedPos = pos;

        if (buttonsBuffer.containsKey(swipedPos))
            buttons = buttonsBuffer.get(swipedPos);
        else
            buttons.clear();

        buttonsBuffer.clear();
        swipeThreshold = 0.5f * buttons.size() * BUTTON_WIDTH3;
        recoverSwipedItem();
    }

    @Override
    public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
        return swipeThreshold;
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
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        int pos = viewHolder.getAdapterPosition();
        float translationX = dX;
        View itemView = viewHolder.itemView;

//        Log.e(TAG, "posAA "+pos);

        if (pos < 0){
            swipedPos = pos;
            return;
        }

        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
            if(dX < 0) {

                List<UnderlayButton> buffer = new ArrayList<>();

                if (!buttonsBuffer.containsKey(pos)){
                    instantiateUnderlayButton(viewHolder, buffer);
                    buttonsBuffer.put(pos, buffer);
                }
                else {
                    buffer = buttonsBuffer.get(pos);
                }

                translationX = dX * buffer.size() * BUTTON_WIDTH3 / itemView.getWidth();
                drawButtons(c, itemView, buffer, pos, translationX);
            }

           // if(pos == 0){
                if(dX > 0) {
                    List<UnderlayButton> buffer = new ArrayList<>();

                    if (!buttonsBuffer.containsKey(pos)){
                        Log.e(TAG, "buttonsBufferAX "+buttonsBuffer.toString());
                        Log.e(TAG, "posAX "+pos);
//                        Log.e(TAG, "viewHolder "+viewHolder);
                        instantiateUnderlayButton(viewHolder, buffer);
                        buttonsBuffer.put(1, buffer);
                    }
                    else {
                        buffer = buttonsBuffer.get(1);
                    }

//                    Log.e(TAG , "buffer.size() "+buffer.size());
//                    Log.e(TAG , "dX "+dX);
//                    Log.e(TAG , "itemView.getWidth() "+itemView.getWidth());

                    translationX = dX * buffer.size() * BUTTON_WIDTH4 / itemView.getWidth();
                   // drawButtons(c, itemView, buffer, pos, translationX);
                }
          //  }


        }

        super.onChildDraw(c, recyclerView, viewHolder, translationX, dY, actionState, isCurrentlyActive);
    }

    private synchronized void recoverSwipedItem(){
        while (!recoverQueue.isEmpty()){
            int pos = recoverQueue.poll();
            if (pos > -1) {
                recyclerView.getAdapter().notifyItemChanged(pos);
            }
        }
    }

    private void drawButtons(Canvas c, View itemView, List<UnderlayButton> buffer, int pos, float dX) {
        float right = itemView.getRight();
        float left = itemView.getLeft();
        float dButtonWidth = (-1) * dX / buffer.size();

        Log.e(TAG, "right "+right);
        Log.e(TAG, "left "+left);
        Log.e(TAG, "dButtonWidth "+dButtonWidth);
        Log.e(TAG, "itemView "+itemView.getId());

        for (UnderlayButton button : buffer) {
            if (dX < 0) {
                left = right - dButtonWidth;
                button.onDraw(
                        c,
                        new RectF(
                                left,
                                itemView.getTop(),
                                right,
                                itemView.getBottom()
                        ),
                        pos, dX
                );
                right = left;
            } else if (dX > 0) {
                right = left - dButtonWidth;
                button.onDraw(c,
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


    public void attachSwipe(){
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(this);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public abstract void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons);

    public static class UnderlayButton {
        private static final String TAG = "UnderlayButton";
        private String text;
        private int imageResId;
        private int color;
        private UnderlayButtonClickListener clickListener;
        private String text2;
        private int imageResId2;
        private int color2;
        private UnderlayButtonClickListener2 clickListener2;
        private RectF clickRegion;
        private RectF clickRegion2;
        private int pos;


        public UnderlayButton(String text, int imageResId, int color, UnderlayButtonClickListener clickListener) {
            this.text = text;
            this.imageResId = imageResId;
            this.color = color;
            this.clickListener = clickListener;

        }

//        public UnderlayButton(String text2, int imageResId2, int color2, UnderlayButtonClickListener2 clickListener2) {
//            this.text2 = text2;
//            this.imageResId2 = imageResId2;
//            this.color2 = color2;
//            this.clickListener2 = clickListener2;
//        }

        public UnderlayButton(String text, int imageResId, int color, UnderlayButtonClickListener clickListener, String text2, int imageResId2, int color2, UnderlayButtonClickListener2 clickListener2) {
            this.text = text;
            this.imageResId = imageResId;
            this.color = color;
            this.clickListener = clickListener;
            this.text2 = text2;
            this.imageResId2 = imageResId2;
            this.color2 = color2;
            this.clickListener2 = clickListener2;
        }

        public boolean onClick(float x, float y){
            Log.e(TAG, "onClick");
//            clickListener.onClick(pos);
            if (clickRegion != null && clickRegion.contains(x, y)){
                clickListener.onClick(pos);
                return true;
            }
            return false;
        }

        public boolean onClick2(float x, float y){
            Log.e(TAG, "onClick2");
          //  clickListener2.onClick(pos);
            if (clickRegion2 != null && clickRegion2.contains(x, y)){
                clickListener2.onClick(pos);
                return true;
            }
            return false;
        }

        public void onDraw(Canvas c, RectF rect, int pos, float dX) {

            Paint p = new Paint();
            c.drawRect(rect, p);

            float cHeight = rect.height();
                    float cWidth = rect.width();

            Log.e(TAG, "cWidth "+cWidth);
            Log.e(TAG, "cHeight "+cHeight);

//            Log.e(TAG, "onDrawAA "+dX);
//
//            Paint p = new Paint();
//
//            // Draw background
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//
//
//                if (dX < 0) {
//
//                    //Boton derecho
////                    c.drawRect(rect, p);
////                    p.setColor(color);
//
//                    Log.e(TAG, "texttext "+text);
//                    p.setColor(color);
//                    c.drawRect(rect, p);
//
//                    // Draw Text
//                    p.setColor(Color.WHITE);
//                    p.setTextSize(Resources.getSystem().getDisplayMetrics().density * 12);
//
//                    Rect r = new Rect();
//                    float cHeight = rect.height();
//                    float cWidth = rect.width();
//                    p.setTextAlign(Paint.Align.LEFT);
//                    p.getTextBounds(text, 0, text.length(), r);
//                    float x = cWidth / 2f - r.width() / 2f - r.left;
//                    float y = cHeight / 2f + r.height() / 2f - r.bottom;
//                    c.drawText(text, rect.left + x, rect.top + y, p);
//
//                    clickRegion = rect;
//                    this.pos = pos;
//
//
//                }
//                else{
////                    //Boton Izquierdo
////                    p.setColor(color2);
////                    c.drawRect(rect, p);
//
//                  //  Paint p = new Paint();
//
//                    // Draw background
//                    p.setColor(color2);
//                    c.drawRect(rect, p);
//
//                    // Draw Text
//                    p.setColor(Color.WHITE);
//                    p.setTextSize(Resources.getSystem().getDisplayMetrics().density * 12);
//
//                    Rect r = new Rect();
//                    float cHeight = rect.height();
//                    float cWidth = rect.width();
//                    p.setTextAlign(Paint.Align.LEFT);
//
//
//                    Log.e(TAG, "text2text2 "+text2);
//
//                   // c.drawColor(0, PorterDuff.Mode.CLEAR);
//
//                    RectF rectF = new RectF(0, 0, 2 , 2);
//                    c.drawRect(rectF, p);
////                    if(!text2.equalsIgnoreCase("")){
////                        p.getTextBounds(text2, 0, text2.length(), r);
////                        float x = cWidth / 2f - r.width() / 2f - r.left;
////                        float y = cHeight / 2f + r.height() / 2f - r.bottom;
////                        c.drawText(text2, rect.left + x, rect.top + y, p);
////
////                        clickRegion = rect;
////                        this.pos = pos;
////                    }else{
//////                        p.getTextBounds("0", 0, 0, r);
//////                        float x = cWidth / 2f - r.width() / 2f - r.left;
//////                        float y = cHeight / 2f + r.height() / 2f - r.bottom;
//////                        c.drawText("", rect.left + x, rect.top + y, p);
//////
//////                        clickRegion = rect;
//////                        this.pos = pos;
////                    }
//
//
//                }
//
//                clickRegion = rect;
//                clickRegion2 = rect;
//                this.pos = pos;
//            }

        }
    }

    public interface UnderlayButtonClickListener {
        void onClick(int pos);
    }

    public interface UnderlayButtonClickListener2 {
        void onClick(int pos);
    }

}
