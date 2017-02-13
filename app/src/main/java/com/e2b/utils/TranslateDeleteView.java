//package com.e2b.utils;
//
//
//import android.content.Context;
//import android.view.View;
//import android.view.animation.Animation;
//import android.view.animation.AnimationUtils;
//import android.view.animation.TranslateAnimation;
//
//import com.app.cijsi.R;
//
//
//public class TranslateDeleteView {
//
//    /*Method to show animation on the left swipe on player name in stack roaster.
//     *Left swipe loads a new view over the player name with a cross sign to delete the
//     * player from Stack Roaster.
//     */
//    public static void TranslateAnimation(Context context, final View view, final int mode) {
//        Animation animation;
//
//        if (mode == 1) {
//            view.setVisibility(View.VISIBLE);
//            animation = AnimationUtils.loadAnimation(context, R.anim.left_swipe);
//        } else {
//            view.setVisibility(View.GONE);
//            animation = AnimationUtils.loadAnimation(context, R.anim.right_swipe);
//        }
//
//
//        animation.setAnimationListener(new TranslateAnimation.AnimationListener() {
//
//            /*Unused overrided method of the AnimationListner Class
//             */
//            @Override
//            public void onAnimationStart(Animation animation) {
//            }
//
//            /*Unused overrided method of the AnimationListner Class
//            */
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//            }
//
//            /*This method checks if it was a left swipe or right based on the value of 'mode'.
//            *Left swipe is judged by a mode value of mode = 1.
//            * Right swipe  is judged by a mode value of mode not equal to 1.
//             */
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                if (mode != 1) {
//                    view.setVisibility(View.GONE);
//                }
//                view.clearAnimation();
//            }
//        });
//        view.startAnimation(animation);
//    }
//
//}
