package com.example.testc2.selector;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FlowLayout extends ViewGroup {
    //所有的子控件的容器
    List<List<View>> list = new ArrayList<>();
    //把每一行的行高存起来
    List<Integer> listLineHeight = new ArrayList<>();
    //防止测量多次
    private boolean isMeasure = false;

    private boolean computed = false;


    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new MarginLayoutParams(getContext(),attributeSet);
    }


    //在被调用这个方法之前   它的父容器  已经把它的测量模式改成了当前控件的测量模式
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        Log.e("FLOW_LO",  "onMeasure");
        //获取到父容器 给我们的参考值
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        //获取到自己的测量模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heighthMode = MeasureSpec.getMode(heightMeasureSpec);

        //保存当前控件的里面子控件的总宽高
        int childCountWidth = 0;
        int childCountHieght =0;
        if(!isMeasure){
            isMeasure = true;
        }else{

            //当前控件中子控件一行使用的宽度值
            int lineCountWidth = 0;
            //保存一行中最高的子控件的高度
            int lineMaxHeight = 0;

            //存储每个子控件的宽高
            int iChildWidth = 0;
            int iChileHeight = 0;
            //创建一行的容器
            List<View> viewList = new ArrayList<>();
            //遍历所有的子控件
            int childCount = getChildCount();
            for(int x=0;x<childCount;x++){
                //获取到子控件
                View childAt = getChildAt(x);
                //先测量子控件
                measureChild(childAt,widthMeasureSpec,heightMeasureSpec);
                MarginLayoutParams layoutParams = (MarginLayoutParams) childAt.getLayoutParams();
                //计算当前子控件的实际宽高
                iChildWidth = childAt.getMeasuredWidth() + layoutParams.leftMargin +layoutParams.rightMargin;
                iChileHeight = childAt.getMeasuredHeight() + layoutParams.bottomMargin +layoutParams.topMargin;
                //当这个字控件的宽度累加之后是否大于
                if(iChildWidth + lineCountWidth > widthSize){   //需要换行
                    //每次进入到这里的时候  只是 保存了上一行的信息  并没有保存当前行的信息

                    //如果需要换行 我们就要保存一行的信息
                    //每次换行的时候都要比较当前行和前面行谁的宽度最大
                    childCountWidth = Math.max(lineCountWidth,childCountWidth);
                    //如果需要换行  要累加行高
                    childCountHieght += lineMaxHeight;
                    //把行高记录到集合中
                    listLineHeight.add(lineMaxHeight);
                    //把这一行的数据放进总容器
                    list.add(viewList);
                    //把一行的容器 重新创建一个
                    viewList = new ArrayList<>();
                    //将每一行的总长度  重新初始化
                    lineCountWidth = iChildWidth;
                    //将高度也重新初始化
                    lineMaxHeight = iChileHeight;

                    viewList.add(childAt);

                }else{//不需要换行
                    lineCountWidth+=iChildWidth;
                    //对比每个子控件到底谁的高度最高
                    lineMaxHeight = Math.max(lineMaxHeight,iChileHeight);
                    //如果当前不需要换行  就将当前控件保存在一行中
                    viewList.add(childAt);
                }

                //这样做的原因是  之前的ifelse中 不会把最后一行的高度加进listLineHeight
                // 最后一行要特殊对待 不管最后一个item是不是最后一行的第一个item
                if(x == childCount - 1){
                    //保存当前行信息
                    childCountWidth = Math.max(lineCountWidth,childCountWidth);
                    childCountHieght +=lineMaxHeight;

                    listLineHeight.add(lineMaxHeight);
                    list.add(viewList);
                }
            }

        }
        //设置控件最终的大小
        int measureWidth = ((widthMode == MeasureSpec.EXACTLY)?widthSize:childCountWidth);
        int measureHeight = ((heighthMode == MeasureSpec.EXACTLY)?heightSize:childCountHieght);

        Log.e("FLOW_LO","widthMode:"+widthMode+"   heighthMode:"+heighthMode+"   childCountWidth:"+childCountWidth+   "  childCountHieght:"+childCountHieght);

        setMeasuredDimension(measureWidth,measureHeight);
        Log.e("FLOW_LO","setMeasuredDimension  measureWidth:"+measureWidth+"   measureHeight:"+measureHeight );

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        Log.e("FLOW_LO","onLayout   changed:"+changed+"  size:"+list.size());
        //摆放子控件的位置
        int left,top,bottom,right;
        //保存上一个控件的边距
        int countLeft = 0;
        //保存上一行的高度的边距
        int countTop = 0;
        //遍历每所有行
        for (List<View> views : list) {
            //遍历每一行的控件
            for (View view : views) {
                //获取到控件的属性对象
                MarginLayoutParams layoutParams = (MarginLayoutParams) view.getLayoutParams();
                left = countLeft+layoutParams.leftMargin;
                top = countTop + layoutParams.topMargin;
                right = left+view.getMeasuredWidth();
                bottom = top+view.getMeasuredHeight();

                Object tag = view.getTag();
                if(tag==null){
                    view.layout(left,top,right,bottom);
                    view.setTag("layed");
                }
                countLeft+=view.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
            }

            //获取到当前这一行的position
            int i = list.indexOf(views);
            countLeft = 0;
            countTop+= listLineHeight.get(i);
        }
        list.clear();
        listLineHeight.clear();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }


}
