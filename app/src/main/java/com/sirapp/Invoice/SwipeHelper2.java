package com.sirapp.Invoice;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.sirapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public abstract class SwipeHelper2 extends ItemTouchHelper.SimpleCallback {

    public static int BUTTON_WIDTH = 220;
    public static int TEXT_SIZE = 10;
    public static int TEXT_TOP = 30;
    public static int TEXT_TOP_2 = 10;

    private RecyclerView recyclerView;
    private List<UnderlayButton> buttons;
    private GestureDetector gestureDetector;
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

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent e) {
            if (swipedPos < 0) return false;
            Point point = new Point((int) e.getRawX(), (int) e.getRawY());

            RecyclerView.ViewHolder swipedViewHolder = recyclerView.findViewHolderForAdapterPosition(swipedPos);

            if(swipedViewHolder != null){
                if(swipedViewHolder.itemView != null){
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
                }
            }

            return false;
        }
    };

    public SwipeHelper2(Context context1, RecyclerView recyclerView) {
        super(0, ItemTouchHelper.LEFT);
        BUTTON_WIDTH = (int) context1.getResources().getDimension(R.dimen._70sdp);
        TEXT_SIZE = (int) context1.getResources().getDimension(R.dimen._9sdp);
        TEXT_TOP = (int) context1.getResources().getDimension(R.dimen._10sdp);
        TEXT_TOP_2 = (int) context1.getResources().getDimension(R.dimen._7sdp);

        this.recyclerView = recyclerView;
        this.buttons = new ArrayList<>();

        this.gestureDetector = new GestureDetector(context1, gestureListener);
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
            if(dX < 0) {
                List<UnderlayButton> buffer = new ArrayList<>();

                if (!buttonsBuffer.containsKey(pos)){
                    instantiateUnderlayButton(viewHolder, buffer);
                    buttonsBuffer.put(pos, buffer);
                }
                else {
                    buffer = buttonsBuffer.get(pos);
                }


//                if(pos == 0){
//                    translationX = dX * buffer.size() * 300 / itemView.getWidth();
//
//                }else{
                    translationX = dX * buffer.size() * BUTTON_WIDTH / itemView.getWidth();
//                }
                drawButtons(c, itemView, buffer, pos, translationX);
            }
        }

        super.onChildDraw(c, recyclerView, viewHolder, translationX, dY, actionState, isCurrentlyActive);
    }

    private synchronized void recoverSwipedItem(){
        while (!recoverQueue.isEmpty()){
            int pos = recoverQueue.poll();
            if (pos > -1) {
                recyclerView.getAdapter().notifyDataSetChanged();
               // recyclerView.getAdapter().notifyItemChanged(pos);
            }
        }
    }

    private void drawButtons(Canvas c, View itemView, List<UnderlayButton> buffer, int pos, float dX){
        float right = itemView.getRight();
        float dButtonWidth = (-1) * dX / buffer.size();

        for (UnderlayButton button : buffer) {
            float left = right - dButtonWidth;
            button.onDraw(
                    c,
                    new RectF(
                            left,
                            itemView.getTop(),
                            right,
                            itemView.getBottom()
                    ),
                    pos
            );

            right = left;
        }
    }

    public void attachSwipe(){
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(this);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public abstract void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons);




    public static class UnderlayButton {


        private String text;
        private int imageResId;
        private int color;
        private int pos;
        private RectF clickRegion;
        private UnderlayButtonClickListener clickListener;
        private Context context;


        public UnderlayButton(Context context1,  String text, int imageResId, int color, UnderlayButtonClickListener clickListener) {
            this.text = text;
            this.imageResId = imageResId;
            this.color = color;
            this.clickListener = clickListener;
            context = context1;

        }

        public boolean onClick(float x, float y){
            if (clickRegion != null && clickRegion.contains(x, y)){
                clickListener.onClick(pos);
                return true;
            }

            return false;
        }

        public void onDraw(Canvas c, RectF rect, int pos){
            Paint p = new Paint();

            // Draw background
            p.setColor(color);
            c.drawRect(rect, p);

            // Draw Text
            p.setColor(Color.WHITE);
       //     p.setTextSize(Resources.getSystem().getDisplayMetrics().density * 11);


            p.setTextSize(TEXT_SIZE);

            Rect r = new Rect();
            float cHeight = rect.height();
            float cWidth = rect.width();
            p.setTextAlign(Paint.Align.CENTER);
            p.getTextBounds(text, 0, 0, r);
            float x = cWidth / 2f - r.width() / 2f - r.left;
            float y = cHeight / 2f + r.height() / 2f - r.bottom;




            if(text.equalsIgnoreCase(context.getString(R.string.list_Mark_as_delivery_received))){
                String t1 = context.getString(R.string.list_Mark_as_delivery_received1);
                String t2 = context.getString(R.string.list_Mark_as_delivery_received2);
                c.drawText(t1, rect.left + x, rect.top + y, p);
                c.drawText(t2, rect.left + x, rect.top + y + 30, p);
            } else if(text.equalsIgnoreCase(context.getString(R.string.list_Delivery_received))){
                c.drawText(text, rect.left + x, rect.top + y + 10, p);
            } else if(text.equalsIgnoreCase(context.getString(R.string.list_Mark_as_void))){
                String t1 = context.getString(R.string.list_Mark_as_void1);
                String t2 = context.getString(R.string.list_Mark_as_void2);
                c.drawText(t1, rect.left + x, rect.top + y, p);
                c.drawText(t2, rect.left + x, rect.top + y + 30, p);
            } else if(text.equalsIgnoreCase(context.getString(R.string.list_Mark_as_unvoid))){
                String t1 = context.getString(R.string.list_Mark_as_unvoid1);
                String t2 = context.getString(R.string.list_Mark_as_unvoid2);
                c.drawText(t1, rect.left + x, rect.top + y, p);
                c.drawText(t2, rect.left + x, rect.top + y + 30, p);
            } else if(text.equalsIgnoreCase(context.getString(R.string.list_Mark_as_paid))){
                String t1 = context.getString(R.string.list_Mark_as_paid1);
                String t2 = context.getString(R.string.list_Mark_as_paid2);
                c.drawText(t1, rect.left + x, rect.top + y, p);
                c.drawText(t2, rect.left + x, rect.top + y + 30, p);
            } else if(text.equalsIgnoreCase(context.getString(R.string.list_Mark_as_unpaid))){
                String t1 = context.getString(R.string.list_Mark_as_unpaid1);
                String t2 = context.getString(R.string.list_Mark_as_unpaid2);
                c.drawText(t1, rect.left + x, rect.top + y, p);
                c.drawText(t2, rect.left + x, rect.top + y + 30, p);
            } else{
                c.drawText(text, rect.left + x, rect.top + y + 10, p);
            }

//            c.drawText(t1, rect.left + x, rect.top + y, p);
//            c.drawText(t2, rect.left + x, rect.top + y + 50, p);

//            if(text.contains(" ")){
//                for (String line: text.split(" ")) {
//                    c.drawText(line, rect.left + x, rect.top + y, p);
//
////                    y += p.getTextSize();
//                   // y += p.descent() - p.ascent();
//                   // x += p.descent() - p.ascent();
//                    y += p.descent() - p.ascent();
//                }
//            }else{
                //c.drawText(text, rect.left + x, rect.top + y+10, p);
//            }

            //CharSequence m = ViewUtil.noTrailingwhiteLines(Html.fromHtml(text));


//
//            c.drawText(text, rect.left + x, rect.top + y, p);

           // c.drawText(SpannedString"\"<b>Hello bold Text</b>\"", rect.left + x, rect.top + y, p);

           // c.drawText(Html.fromHtml("<![CDATA[<font color='#145A14'>text</font>]]>"), rect.left + x, rect.top + y, p);

//            Paint textPaint = new Paint();
//            textPaint.setARGB(200, 254, 0, 0);
//            textPaint.setTextAlign(Paint.Align.CENTER);
//
//            textPaint.setTextSize(300);
//            c.drawText("Hello", c.getWidth()/2, c.getHeight()/2  , textPaint);

            clickRegion = rect;
            this.pos = pos;
        }
    }

    public interface UnderlayButtonClickListener {
        void onClick(int pos);
    }
}