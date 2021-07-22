package com.sirapp.Invoice;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
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

public abstract class SwipeHelper3 extends ItemTouchHelper.SimpleCallback {

    public static final int BUTTON_WIDTH = 400;
    private RecyclerView recyclerView;
    private List<SwipeHelper3.UnderlayButton> buttons;
    private GestureDetector gestureDetector;
    private int swipedPos = -1;
    private float swipeThreshold = 0.5f;
    private Map<Integer, List<SwipeHelper3.UnderlayButton>> buttonsBuffer;
    private Queue<Integer> recoverQueue;

    private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener(){
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            for (SwipeHelper3.UnderlayButton button : buttons){
                if(button.onClick(e.getX(), e.getY()))
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
                if (rect.top < point.y && rect.bottom > point.y)
                    gestureDetector.onTouchEvent(e);
                else {
                    recoverQueue.add(swipedPos);
                    swipedPos = -1;
                    recoverSwipedItem();
                }
            }
            return false;
        }
    };




//    @Override
//    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
//        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
//        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
//        return makeMovementFlags(dragFlags, swipeFlags);
//    }


    public SwipeHelper3(Context context, RecyclerView recyclerView) {

//        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN,ItemTouchHelper.RIGHT |ItemTouchHelper.LEFT);
//        this.recyclerView = recyclerView;


        super(0, ItemTouchHelper.RIGHT);
        this.recyclerView = recyclerView;
        this.buttons = new ArrayList<>();
        this.gestureDetector = new GestureDetector(context, gestureListener);
        this.recyclerView.setOnTouchListener(onTouchListener);
        buttonsBuffer = new HashMap<>();
        recoverQueue = new LinkedList<Integer>(){
            @Override
            public boolean add(Integer o) {
                if (contains(o))
                    return false;
                else
                    return super.add(o);
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

        if (swipedPos != pos)
            recoverQueue.add(swipedPos);

        swipedPos = pos;

        if (buttonsBuffer.containsKey(swipedPos))
            buttons = buttonsBuffer.get(swipedPos);
        else
            buttons.clear();

        buttonsBuffer.clear();

//        if(pos == 0){
//            swipeThreshold = 0.5f * buttons.size() * 300;
//        }else{
        swipeThreshold = 0.5f * buttons.size() * BUTTON_WIDTH;
//        }

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

        if (pos < 0){
            swipedPos = pos;
            return;
        }

        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
            if(dX > 0) {
                List<SwipeHelper3.UnderlayButton> buffer = new ArrayList<>();

                if (!buttonsBuffer.containsKey(pos)){
                    instantiateUnderlayButton(viewHolder, buffer);
                    buttonsBuffer.put(pos, buffer);
                }
                else {
                    buffer = buttonsBuffer.get(pos);
                }

                translationX = dX * buffer.size() * BUTTON_WIDTH / itemView.getWidth();

                drawButtons(c, itemView, buffer, pos, translationX);
            }
        }

        super.onChildDraw(c, recyclerView, viewHolder, translationX, dY, actionState, isCurrentlyActive);
    }



//    @Override
//    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//        int pos = viewHolder.getAdapterPosition();
//        float translationX = dX;
//        View itemView = viewHolder.itemView;
//
//        if (pos < 0){
//            swipedPos = pos;
//            return;
//        }
//
//        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
//            if(dX > 0) {
//                List<SwipeHelper3.UnderlayButton> buffer = new ArrayList<>();
//
//                if (!buttonsBuffer.containsKey(pos)){
//                    instantiateUnderlayButton(viewHolder, buffer);
//                    buttonsBuffer.put(pos, buffer);
//                }
//                else {
//                    buffer = buttonsBuffer.get(pos);
//                }
//
//
////                if(pos == 0){
////                    translationX = dX * buffer.size() * 300 / itemView.getWidth();
////
////                }else{
//                translationX = dX * buffer.size() * BUTTON_WIDTH / itemView.getWidth();
////                }
//                drawButtons(c, itemView, buffer, pos, translationX);
//
//
//            }
//        }
//
////        View itemView = viewHolder.itemView;
////        int backgroundCornerOffset = 100;
//
//
//
//        super.onChildDraw(c, recyclerView, viewHolder, translationX, dY, actionState, isCurrentlyActive);
//
//
////        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
////            // Get RecyclerView item from the ViewHolder
////            View itemView = viewHolder.itemView;
////
////            dX = dX /2;
////            Paint p = new Paint();
////            if (dX > 0) {
////                /* Set your color for positive displacement */
////
////                // Draw Rect with varying right side, equal to displacement dX
////                c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), 200,
////                        (float) itemView.getBottom(), p);
////            } else {
////                /* Set your color for negative displacement */
////
////                // Draw Rect with varying left side, equal to the item's right side plus negative displacement dX
////                c.drawRect((float) itemView.getRight() + 200, (float) itemView.getTop(),
////                        (float) itemView.getRight(), (float) itemView.getBottom(), p);
////            }
////
////            super.onChildDraw(c, recyclerView, viewHolder, 200, dY, actionState, isCurrentlyActive);
////        }
//
//
//
//
//    }
//




//
//
//    public static final float ALPHA_FULL = 1.0f;
//
//    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
//            // Get RecyclerView item from the ViewHolder
//            View itemView = viewHolder.itemView;
//
//            Paint p = new Paint();
//            //Bitmap icon;
//
//            if (dX > 0) {
//            /* Note, ApplicationManager is a helper class I created
//               myself to get a context outside an Activity class -
//               feel free to use your own method */
//
////                icon = BitmapFactory.decodeResource(
////                        ApplicationManager.getContext().getResources(), R.drawable.a);
//
//                /* Set your color for positive displacement */
//                p.setARGB(255, 255, 0, 0);
//                dX = dX/2;
//                // Draw Rect with varying right side, equal to displacement dX
//                c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX,
//                        (float) itemView.getBottom(), p);
//
//                // Set the image icon for Right swipe
////                c.drawBitmap(icon,
////                        (float) itemView.getLeft() + convertDpToPx(16),
////                        (float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - icon.getHeight())/2,
////                        p);
//            } else {
////                icon = BitmapFactory.decodeResource(
////                        ApplicationManager.getContext().getResources(), R.drawable.myrightdrawable);
//
//                /* Set your color for negative displacement */
//                p.setARGB(255, 0, 255, 0);
//
//                // Draw Rect with varying left side, equal to the item's right side
//                // plus negative displacement dX
//                c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),
//                        (float) itemView.getRight(), (float) itemView.getBottom(), p);
//
//                //Set the image icon for Left swipe
////                c.drawBitmap(icon,
////                        (float) itemView.getRight() - convertDpToPx(16) - icon.getWidth(),
////                        (float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - icon.getHeight())/2,
////                        p);
//            }
//
//            // Fade out the view as it is swiped out of the parent's bounds
//            final float alpha = ALPHA_FULL - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
//            viewHolder.itemView.setAlpha(alpha);
//
//            dX = dX/2;
//            viewHolder.itemView.setTranslationX(100);
//
//        } else {
//            dX = dX/2;
//            super.onChildDraw(c, recyclerView, viewHolder, 100, dY, actionState, isCurrentlyActive);
//        }
//    }
//
////    private int convertDpToPx(int dp){
////        return Math.round(dp * (getResources().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));
////    }




    private synchronized void recoverSwipedItem(){
        while (!recoverQueue.isEmpty()){
            int pos = recoverQueue.poll();
            if (pos > -1) {
                recyclerView.getAdapter().notifyItemChanged(pos);
            }
        }
    }

    private void drawButtons(Canvas c, View itemView, List<SwipeHelper3.UnderlayButton> buffer, int pos, float dX){
//        float right = itemView.getRight();
//        float dButtonWidth = (-1) * dX / buffer.size();
        float right = itemView.getRight();
        float left = itemView.getLeft();
        float dButtonWidth = (-1) * dX / buffer.size();
        for (SwipeHelper3.UnderlayButton button : buffer) {
            right = left - dButtonWidth;
            button.onDraw(
                    c,
                    new RectF(
                            right,
                            itemView.getTop(),
                            left,
                            itemView.getBottom()
                    ),
                    pos
            );

            left = right;


//            if (dX > 0) {
//                right = left - dButtonWidth;
//                button.onDraw(c,
//                        new RectF(
//                                right,
//                                itemView.getTop(),
//                                left,
//                                itemView.getBottom()
//                        ), pos, dX
//                );
//                left = right;
//            }
        }

//        float right = itemView.getRight();
//        float left = itemView.getLeft();
//        float dButtonWidth = (-1) * dX / buffer.size();
//
//        for (SwipeHelper3.UnderlayButton button : buffer) {
////            if (dX < 0) {
////                left = right - dButtonWidth;
////                button.onDraw(
////                        c,
////                        new RectF(
////                                left,
////                                itemView.getTop(),
////                                right,
////                                itemView.getBottom()
////                        ),
////                        pos, dX
////                );
////                right = left;
////            } else
//                if (dX > 0) {
//                right = left - dButtonWidth;
//                button.onDraw(c,
//                        new RectF(
//                                right,
//                                itemView.getTop(),
//                                left,
//                                itemView.getBottom()
//                        ), pos, dX
//                );
//                left = right;
//            }
//        }


    }

    public void attachSwipe(){
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(this);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public abstract void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<SwipeHelper3.UnderlayButton> underlayButtons);

    public static class UnderlayButton {
        private String text;
        private int imageResId;
        private int color;
        private int pos;
        private RectF clickRegion;
        private SwipeHelper3.UnderlayButtonClickListener clickListener;

        public UnderlayButton(String text, int imageResId, int color, SwipeHelper3.UnderlayButtonClickListener clickListener) {
            this.text = text;
            this.imageResId = imageResId;
            this.color = color;
            this.clickListener = clickListener;
        }

        public boolean onClick(float x, float y){
            if (clickRegion != null && clickRegion.contains(x, y)){
                clickListener.onClick(pos);
                return true;
            }

            return false;
        }

        public void onDraw(Canvas c, RectF rect, int pos){
//            Paint p = new Paint();
//
//            // Draw background
//            p.setColor(color);
//            c.drawRect(rect, p);
//
//            // Draw Text
//           // p.setColor(Color.WHITE);
//            p.setTextSize(Resources.getSystem().getDisplayMetrics().density * 12);
//
//            Rect r = new Rect();
//            float cHeight = rect.height();
//            float cWidth = rect.width();
//            p.setTextAlign(Paint.Align.RIGHT);
//            p.getTextBounds(text, 0, text.length(), r);
//            float x = cWidth / 2f - r.width() / 2f - r.left;
//            float y = cHeight / 2f + r.height() / 2f - r.bottom;
//            c.drawText(text, rect.left + x, rect.top + y, p);
//
//            // c.drawText(Html.fromHtml("<![CDATA[<font color='#145A14'>text</font>]]>"), rect.left + x, rect.top + y, p);
//
////            Paint textPaint = new Paint();
////            textPaint.setARGB(200, 254, 0, 0);
////            textPaint.setTextAlign(Paint.Align.CENTER);
////
////            textPaint.setTextSize(300);
////            c.drawText("Hello", c.getWidth()/2, c.getHeight()/2  , textPaint);
//
//            clickRegion = rect;
//            this.pos = pos;



            Paint p = new Paint();
            p.setColor(color);
            p.setTextSize(Resources.getSystem().getDisplayMetrics().density * 12);
            Rect r = new Rect();
            float cHeight = rect.height();
            float cWidth = rect.width();
            p.setTextAlign(Paint.Align.LEFT);
            p.getTextBounds(text, 0, text.length(), r);
            float x = cWidth / 2f - r.width() / 2f - r.left;
            float y = cHeight / 2f + r.height() / 2f - r.bottom;
            c.drawText("Mark as delivery received", 50, rect.top + y, p);

            clickRegion = rect;
            this.pos = pos;
        }
    }

    public interface UnderlayButtonClickListener {
        void onClick(int pos);
    }
}