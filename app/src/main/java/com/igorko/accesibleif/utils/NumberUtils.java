package com.igorko.accesibleif.utils;

import android.util.TypedValue;
import com.igorko.accesibleif.app.AppContext;

/**
 * Created by Igorko on 22.11.2016.
 */

public class NumberUtils {

    public static float getFloat(int dimen){
        TypedValue typedValue = new TypedValue();
        AppContext.getInstance().getResources().getValue(dimen, typedValue, true);
        return typedValue.getFloat();
    }

}
