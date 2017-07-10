package com.shy.gllearn01;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * Created by axnshy on 2017/7/9.
 */

public class SimpleGLSurfaceView extends GLSurfaceView {

    MyRenderer mRenderer;

    public SimpleGLSurfaceView(Context context) {
        this(context, null);
    }

    public SimpleGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        /*final ActivityManager activityManager = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        final boolean supportEs2 = configurationInfo.reqGlEsVersion>=0x2000;
        if(supportEs2){*/
//        Log.d("TAG", "supportEl:   " + configurationInfo.reqGlEsVersion);
        setEGLContextClientVersion(2);
        mRenderer = new MyRenderer();
        setRenderer(mRenderer);
//        }

    }
}
