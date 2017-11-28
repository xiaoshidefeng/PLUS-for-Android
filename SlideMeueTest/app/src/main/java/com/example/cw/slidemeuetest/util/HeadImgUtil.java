package com.example.cw.slidemeuetest.util;

import com.example.cw.slidemeuetest.R;
import com.facebook.drawee.generic.RoundingParams;

/**
 * Created by asus on 2017/11/28.
 */

public class HeadImgUtil {
    public static RoundingParams getRoundingParams(float radius, float border) {
        RoundingParams roundingParams = RoundingParams.fromCornersRadius(radius);
        roundingParams.setBorder(R.color.colorWhite, (float) border);
        roundingParams.setRoundAsCircle(true);
        return roundingParams;
    }
}
