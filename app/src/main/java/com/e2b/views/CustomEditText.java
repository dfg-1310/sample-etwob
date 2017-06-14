package com.e2b.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.widget.EditText;

import com.e2b.R;
import com.e2b.utils.FontManager;


public class CustomEditText extends AppCompatEditText {

    private Context context;
    private String typeface;

    public CustomEditText(Context context) {
        super(context);
        this.context = context;
    }

    public CustomEditText(Context context, String typeface) {
        super(context);
        this.context = context;
        this.typeface = typeface;
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.TextView);

        typeface = a.getString(R.styleable.TextView_ctypeface);
        setCTypeFace(typeface);
    }

    public CustomEditText(Context context, AttributeSet attrs,
                              int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setInputType(int type) {
        super.setInputType(type);
        setCTypeFace(typeface);
    }

    public void setCTypeFace(String tf) {
        typeface = tf;
        if (tf != null) {
            FontManager.getInstance(context).setTypeFace(this, tf);
        }
    }

}
