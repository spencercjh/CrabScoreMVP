package notmvp.view;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import top.spencer.crabscore.R;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 猫头鹰输入密码的时候捂住眼睛
 * 布局文件中宽=175dp，height=107dp,也可以不写默认，只是为了预览
 *
 * @author boobooL
 * Created by boobooL on 2016/4/19 0019
 * Created 邮箱 ：boobooMX@163.com
 */
public class OwlView extends View {
    private Context mContext;
    private Bitmap bmOwl;
    private Bitmap bmOwlArmLeft;
    private Bitmap bmOwlArmRight;
    private int bmHeight;
    private int moveHeight;
    private int alpha = 255;

    private int moveLength = 0;
    private Paint handPaintBefore;
    private Paint handPaintAfter;


    public OwlView(Context context) {
        this(context, null);
    }

    public OwlView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OwlView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        setBackgroundColor(Color.TRANSPARENT);
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getViewTreeObserver().addOnGlobalLayoutListener(this);
                ViewGroup.LayoutParams lp = getLayoutParams();
                if (lp == null) {
                    return;
                }
                lp.width = dip2px(175);
                lp.height = dip2px(107);
                setLayoutParams(lp);
                setTranslationY(dip2px(9));
            }
        });

        bmOwl = BitmapFactory.decodeResource(getResources(), R.mipmap.owl_login);
        bmOwlArmLeft = BitmapFactory.decodeResource(getResources(), R.mipmap.owl_login_arm_left);
        bmOwlArmRight = BitmapFactory.decodeResource(getResources(), R.mipmap.owl_login_arm_right);

        bmOwl = compressBitmap(bmOwl, dip2px(115), dip2px(107), false);
        bmOwlArmLeft = compressBitmap(bmOwlArmLeft, dip2px(40), dip2px(65), true);
        bmOwlArmRight = compressBitmap(bmOwlArmRight, dip2px(40), dip2px(65), true);

        bmHeight = bmOwlArmLeft.getHeight() / 3 * 2 - dip2px(10);

        handPaintBefore = new Paint();
        handPaintBefore.setColor(Color.parseColor("#472d20"));
        handPaintBefore.setAntiAlias(true);

        handPaintAfter = new Paint();
        handPaintAfter.setAntiAlias(true);

    }

    public void close() {
        ValueAnimator alphaVa = ValueAnimator.ofInt(0, 255).setDuration(300);
        alphaVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                alpha = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        alphaVa.start();
        ValueAnimator moveVa = ValueAnimator.ofInt(dip2px(45), 0).setDuration(200);
        moveVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                moveLength = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        moveVa.setStartDelay(200);
        moveVa.start();
        ValueAnimator va = ValueAnimator.ofInt(bmHeight, 0).setDuration(300);
        va.setInterpolator(new LinearInterpolator());
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                moveHeight = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        va.start();
    }

    public void open() {
        final ValueAnimator alphaVa = ValueAnimator.ofInt(255, 0).setDuration(300);
        alphaVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                alpha = (int) animation.getAnimatedValue();
                invalidate();

            }
        });
        alphaVa.start();
        ValueAnimator moveVa = ValueAnimator.ofInt(0, dip2px(45)).setDuration(200);
        moveVa.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                moveLength = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        moveVa.start();

        ValueAnimator va = ValueAnimator.ofInt(0, bmHeight).setDuration(300);
        va.setInterpolator(new LinearInterpolator());
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                moveHeight = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        va.setStartDelay(100);
        va.start();

    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bmOwl, new Rect(0, 0, bmOwl.getWidth(), bmOwl.getHeight()),
                new Rect(dip2px(30), 0, dip2px(30) + bmOwl.getWidth(),
                        bmOwl.getHeight()), handPaintAfter);
        handPaintBefore.setAlpha(alpha);
        //noinspection SuspiciousNameCombination
        canvas.drawOval(new RectF(moveHeight, getHeight() - dip2px(20), moveLength + dip2px(30),
                getHeight()), handPaintBefore);
        canvas.drawOval(new RectF(getWidth() - dip2px(30) - moveLength, getHeight() - dip2px(20),
                getWidth() - moveLength, getHeight()), handPaintBefore);
        canvas.drawBitmap(bmOwlArmLeft,
                new Rect(0, 0, bmOwlArmLeft.getWidth(), moveHeight),
                new Rect(dip2px(43), getHeight() - moveHeight - dip2px(10), dip2px(43) +
                        bmOwlArmLeft.getWidth(), getHeight() - dip2px(9)), handPaintAfter);
        //canvas.drawBitmap(Bitmap,Rect,Rect,Paint)
        canvas.drawBitmap(bmOwlArmRight,
                new Rect(0, 0, bmOwlArmRight.getWidth(), moveHeight),
                new Rect(getWidth() - dip2px(40) - bmOwlArmRight.getWidth(), getHeight() -
                        moveHeight - dip2px(10), getWidth() - dip2px(40), getHeight() -
                        dip2px(9)), handPaintAfter);
    }

    /**
     * 压缩图片
     *
     * @param bitmap   bitmap
     * @param reqsW    reqsW
     * @param reqsH    reqsH
     * @param isAdjust isAdjust
     * @return Bitmap
     */
    private Bitmap compressBitmap(Bitmap bitmap, int reqsW, int reqsH, boolean isAdjust) {
        if (bitmap == null || reqsW == 0 || reqsH == 0) {
            return bitmap;
        }
        if (bitmap.getWidth() > reqsW || bitmap.getHeight() > reqsH) {
            /*
             * BigDecimal用法详解
             一、简介
             Java在java.math包中提供的API类BigDecimal，用来对超过16位有效位的数进行精确的运算。双精度浮点型变量double可以处理16位有效数。在实际应用中，需要对更大或者更小的数进行运算和处理。float和double只能用来做科学计算或者是工程计算，在商业计算中要用java.math.BigDecimal。BigDecimal所创建的是对象，我们不能使用传统的+、-、*、/等算术运算符直接对其对象进行数学运算，而必须调用其相对应的方法。方法中的参数也必须是BigDecimal的对象。构造器是类的特殊方法，专门用来创建对象，特别是带有参数的对象。
             二、构造器描述
             BigDecimal(int)       创建一个具有参数所指定整数值的对象。
             BigDecimal(double) 创建一个具有参数所指定双精度值的对象。
             BigDecimal(long)    创建一个具有参数所指定长整数值的对象。
             BigDecimal(String) 创建一个具有参数所指定以字符串表示的数值的对象。

             三、方法描述
             add(BigDecimal)        BigDecimal对象中的值相加，然后返回这个对象。
             subtract(BigDecimal) BigDecimal对象中的值相减，然后返回这个对象。
             multiply(BigDecimal)  BigDecimal对象中的值相乘，然后返回这个对象。
             divide(BigDecimal)     BigDecimal对象中的值相除，然后返回这个对象。
             toString()                将BigDecimal对象的数值转换成字符串。
             doubleValue()          将BigDecimal对象中的值以双精度数返回。
             floatValue()             将BigDecimal对象中的值以单精度数返回。
             longValue()             将BigDecimal对象中的值以长整数返回。
             intValue()               将BigDecimal对象中的值以整数返回。

             四、格式化及例子
             由于NumberFormat类的format()方法可以使用BigDecimal对象作为其参数，可以利用BigDecimal对超出16位有效数字的货币值，百分值，以及一般数值进行格式化控制。
             以利用BigDecimal对货币和百分比格式化为例。首先，创建BigDecimal对象，进行BigDecimal的算术运算后，分别建立对货币和百分比格式化的引用，最后利用BigDecimal对象作为format()方法的参数，输出其格式化的货币值和百分比。
             */
            float scaleX = new BigDecimal(reqsW).divide(new BigDecimal(bitmap.getWidth()), 4, RoundingMode.DOWN).floatValue();
            float scaleY = new BigDecimal(reqsH).divide(new BigDecimal(bitmap.getHeight()), 4, RoundingMode.DOWN).floatValue();
            if (isAdjust) {
                scaleX = scaleX < scaleY ? scaleX : scaleY;
                //noinspection SuspiciousNameCombination
                scaleY = scaleX;
            }
            //Matrix的作用是什么
            Matrix matrix = new Matrix();
            matrix.postScale(scaleX, scaleY);

            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }
        return bitmap;
    }

    private int dip2px(int dpValue) {
        float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);

    }


}
