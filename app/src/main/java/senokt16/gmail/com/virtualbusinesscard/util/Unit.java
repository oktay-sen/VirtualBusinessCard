package senokt16.gmail.com.virtualbusinesscard.util;

import android.content.Context;

public class Unit {

    public static int px(Context context, int px) {
        return (int) (px / context.getResources().getDisplayMetrics().density + 0.5f);
    }

    public static int dp(Context context, int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density + 0.5f);
    }
}
