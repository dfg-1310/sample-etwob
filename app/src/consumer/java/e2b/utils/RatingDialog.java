package e2b.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.e2b.R;
import com.e2b.views.CustomEditText;

import java.text.DecimalFormat;

import e2b.intrface.ICustomCallback;


public class RatingDialog extends Dialog implements View.OnClickListener {
    private ICustomCallback callback;

    private Button submit_star;
    private CustomEditText commentCustomEditText;
//    private TextView seekArcProgress;
    private float rating;


    public RatingDialog(Context context, final float initialRating, ICustomCallback callBack) {
        super(context, R.style.TransaparantDialog);
        this.callback = callBack;
        this.rating = initialRating;
        this.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.rating_star);


        RatingBar dialog_ratingbar = (RatingBar) findViewById(R.id.dialog_ratingbar);
        submit_star = (Button) findViewById(R.id.submit_star);
        commentCustomEditText = (CustomEditText) findViewById(R.id.et_rating_comment);
        submit_star.setEnabled(false);
//        seekArc = (SeekArc) findViewById(R.id.seekArc);
//        seekArc.setMax(5);
//        seekArc.setProgress(rating);
//        seekArcProgress = (TextView) findViewById(R.id.seekArcProgress);
        dialog_ratingbar.setRating(rating);
//        seekArc.setEnabled(false);
        initView(rating);

        dialog_ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
//                seekArc.setProgress(ratingBar.getRating());
                rating = ratingBar.getRating();
                initView(rating);
            }
        });

        submit_star.setOnClickListener(this);
        this.show();

    }

    private void initView(float rating) {

        if (rating > 0) {
            String v = roundFloat(rating);
            submit_star.setEnabled(true);
            submit_star.setAlpha(1f);
//            seekArcProgress.setText(v);

        } else {
            submit_star.setEnabled(false);
            submit_star.setAlpha(0.5f);
//            seekArcProgress.setText("");
        }
    }

    @Override
    public void onClick(View view) {
        callback.onOkClicked(RatingDialog.this, rating, commentCustomEditText.getText().toString());
    }

    public static String roundFloat(double d) {
        DecimalFormat df2 = new DecimalFormat("#0.00");
        return df2.format(d);
    }


}


