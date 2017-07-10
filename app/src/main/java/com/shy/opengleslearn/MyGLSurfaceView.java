package com.shy.opengleslearn;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * Created by axnshy on 2017/6/11.
 */

public class MyGLSurfaceView extends GLSurfaceView {



    MyRender mRender;
    public MyGLSurfaceView(Context context) {
        super(context);
        setEGLContextClientVersion(2);

        mRender = new MyRender();
        setRenderer(mRender);

//        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

    }

    public MyGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



}
