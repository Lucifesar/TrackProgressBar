package com.example.administrator.trackprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ProgressBar;

/**
 * Created by liuqiang on 2016/6/30 0030.
 * 轨道进度条
 */
public class TrackProgressBar extends ProgressBar
{
    private static final int DEFAULT_SHOW_MAX_CHARACTER = 4;
    private static final String PROGRESS_TEXT_COLOR = "#ff888888";
    private static final String PROGRESS_STROKE_COLOR = "#ff0000ff";
    private Paint mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//进度条画笔,同时设置抗锯齿
    private Paint mProgressTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//进度百分比画笔,同时设置抗锯齿
    private RectF mRectfLeft;
    private  RectF mRectFRigth;
    private  Rect mProgressTextRect;
    private float centerX,centerY;
    private float radii;//半径
    private float diameter;//直径
    private float lineL;
    private float startX,startY;
    private int mProgressWidth;
    private int mProgressHeight;
    //进度文字颜色
    private int mProgressTextColor;
    //进度文字大小
    private int mProgressTextSize;
    //进度条颜色
    private int mProgressStrokeColor;
    //进度条宽度
    private int mProgressStrokeWidth;

    private OnTrackProgressBarListener mOnTrackProgressBarListener;
    public TrackProgressBar(Context context) {
        super(context);
    }


    public TrackProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs,0);

        initProgressBar();

        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.TrackProgressBar);
        mProgressWidth = a.getDimensionPixelOffset(R.styleable.TrackProgressBar_progress_width,mProgressWidth);
        mProgressHeight = a.getDimensionPixelOffset(R.styleable.TrackProgressBar_progress_height,mProgressHeight);
        mProgressTextColor =a .getColor(R.styleable.TrackProgressBar_progress_text_color,Color.parseColor(PROGRESS_TEXT_COLOR));
        mProgressTextSize  =a.getDimensionPixelSize(R.styleable.TrackProgressBar_progress_text_size,mProgressTextSize);
        mProgressStrokeColor = a.getColor(R.styleable.TrackProgressBar_progress_stroke_color,Color.parseColor(PROGRESS_STROKE_COLOR));
        mProgressStrokeWidth = a.getDimensionPixelOffset(R.styleable.TrackProgressBar_progress_stroke_width,mProgressStrokeWidth);
        a.recycle();

        //设置进度条为确定的
        setIndeterminate(false);
        //设置文字居中
        mProgressTextPaint.setTextAlign(Paint.Align.CENTER);
        //设置字体颜色
        mProgressTextPaint.setColor(mProgressTextColor);
        //设置字体大小
        setTextSize(mProgressTextSize,mProgressTextPaint);

        //设置进度宽度
        mProgressPaint.setStrokeWidth(mProgressStrokeWidth);
        //设置进度颜色
        mProgressPaint.setColor(mProgressStrokeColor);
        //设置进度空心展示
        mProgressPaint.setStyle(Paint.Style.STROKE);
        //设置默认padding
        if(mProgressStrokeWidth>0) {
            setPadding(mProgressStrokeWidth, mProgressStrokeWidth, mProgressStrokeWidth, mProgressStrokeWidth);
        }

        setOnTrackProgressBarListener(getOnTrackProgressBarListener());

    }
    private void initProgressBar(){
        mProgressWidth = 48;
        mProgressHeight = 48;
        mProgressTextSize =10;
    }

    /**
     * 设置文字大小 保证设置的sp不超过文字总长度
     * @param textSize 文字的像素大小
     * @param paint 画笔
     */
    private void setTextSize(int textSize,Paint paint){
        //取得所有字符的像素总数
        int cutPx = textSize*DEFAULT_SHOW_MAX_CHARACTER;
        if(cutPx<=mProgressWidth){
            paint.setTextSize(textSize);
        }else{
            paint.setTextSize(mProgressHeight/2);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        diameter = mProgressHeight;
        radii = mProgressHeight/2;
        centerX = w / 2;
        centerY = h / 2;
        startX = 0;
        startY = 0;
        lineL = mProgressWidth-diameter;
        startX =centerX- mProgressWidth/2;
        startY =centerY-radii;
        //初始化左侧圆弧矩形框
        mRectfLeft = new RectF(startX,startY,startX+diameter,startY+diameter);
        //初始化右侧圆弧矩形框
        mRectFRigth = new RectF(startX+lineL,startY,startX+diameter+lineL,startY+diameter);
        //初始化文字矩形框
        mProgressTextRect = new Rect();
        Log.e("onSizeChange","w = "+w+" h = "+ h + " oldw  ="+ oldw +" oldh = "+ oldh + " centerX = "+ centerX+" centerY = "+centerY);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int dw = mProgressWidth ;
        int dh = mProgressHeight ;
        dw += getPaddingLeft() + getPaddingRight();
        dh += getPaddingTop() + getPaddingTop();
        setMeasuredDimension(dw,dh);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        int progress = getProgress();
        mOnTrackProgressBarListener.onTrackProgressChanged(this,progress);
        String progressText = progress + "%";
        mProgressTextPaint.getTextBounds(progressText, 0, progressText.length(), mProgressTextRect);
        if(getProgress()!=0)
            canvas.drawText(progressText,centerX,centerY+mProgressTextRect.height()/2,mProgressTextPaint);
        if(getProgress()<=getMax()*0.25) {
            canvas.drawArc(mRectfLeft, 90f, 180f *getProgress()/25f, false, mProgressPaint);
        }else{
            canvas.drawArc(mRectfLeft, 90f, 180f, false, mProgressPaint);
            if(getProgress()>getMax()*0.25&&getProgress()<=getMax()*0.5){
                canvas.drawLine(startX+radii,startY,(startX+radii)+lineL*(getProgress()-25)/25f,startY,mProgressPaint);
            }else{
                canvas.drawLine(startX+radii,startY,startX+radii+lineL,startY,mProgressPaint);
                if(getProgress()>getMax()*0.5&&getProgress()<=getMax()*0.75){
                    canvas.drawArc(mRectFRigth,-90f,180f*(getProgress()-50)/25f,false,mProgressPaint);
                }else{
                    canvas.drawArc(mRectFRigth,-90f,180f,false,mProgressPaint);
                    if(getProgress()<getMax()-1){
                        canvas.drawLine(startX+radii+lineL,startY+diameter,startX+radii+lineL*((getMax()-getProgress())/25f),startY+diameter,mProgressPaint);
                    }else{
                        canvas.drawLine(startX+radii+lineL,startY+diameter,startX+radii,startY+diameter,mProgressPaint);
                    }
                }
            }
        }
    }


    public OnTrackProgressBarListener getOnTrackProgressBarListener() {
        return mOnTrackProgressBarListener;
    }

    public void setOnTrackProgressBarListener(OnTrackProgressBarListener onTrackProgressBarListener) {
        mOnTrackProgressBarListener = onTrackProgressBarListener;
    }

   public interface OnTrackProgressBarListener{
         void onTrackProgressChanged(TrackProgressBar mTrackProgressBar,int mProgress);
    }
}
