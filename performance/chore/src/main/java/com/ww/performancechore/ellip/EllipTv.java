package com.ww.performancechore.ellip;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import kotlin.text.Typography;


public class EllipTv extends AppCompatTextView {

    public EllipTv(Context context) {
        super(context);
    }

    public EllipTv(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public EllipTv(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        float availableScreenWidth = getMeasuredWidth() - getCompoundPaddingLeft() - getCompoundPaddingRight();
        float availableTextWidth = availableScreenWidth * getMaxLines();
        CharSequence ellipsizedText = TextUtils.ellipsize(getText(), getPaint(), availableTextWidth, getEllipsize());
        Log.d("gggg","ellipsizedText1:"+ellipsizedText);
        if (!(ellipsizedText.toString().equals(getText().toString()))) {
            // If the ellipsizedText is different than the original text, this means that it didn't fit and got indeed ellipsized.
            // Calculate the new availableTextWidth by taking into consideration the size of the custom ellipsis, too.
            availableTextWidth = (availableScreenWidth - getPaint().measureText(getEllipsis())) * getMaxLines();
            ellipsizedText = TextUtils.ellipsize(getText(), getPaint(), availableTextWidth, getEllipsize());
            Log.d("gggg","ellipsizedText2:"+ellipsizedText);
            int estart =  ellipsizedText.toString().indexOf(Typography.ellipsis);
            int eend = estart + 1;
            getSb().clear();
            String str = getSb().append(ellipsizedText).replace(estart,eend, getSb1()).toString();
            setText(str);
        }
    }

    String getEllipsis(){
        return "666";
    }

    SpannableStringBuilder sb;

    public SpannableStringBuilder getSb() {
        if(sb == null){
            sb = new SpannableStringBuilder();

        }
        return sb;
    }


    SpannableString sb1;

    public SpannableString getSb1() {
        if(sb1==null){
            sb1 = new SpannableString(getEllipsis());
            sb1.setSpan(new ForegroundColorSpan(Color.RED),0,getEllipsis().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return sb1;
    }
}
