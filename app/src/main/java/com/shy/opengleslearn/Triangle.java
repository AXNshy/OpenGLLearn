package com.shy.opengleslearn;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by axnshy on 2017/6/11.
 */

public class Triangle {
    private FloatBuffer vertexBuffer;

    private int Program;


    private int mPositionHandle;
    private int mColorHandle;

    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex



    static final int COORDS_PER_VERTEX = 3;
    static float triangleCoords[] = {
            0.0f,0.0f,0.0f,
            -0.5f,-0.5f,0.0f,
            0.5f,-0.5f,0.0f
    };

    float color[] = {0.63671875f,0.76953125f,0.22265625f,1.0f};

    private final String vertexShaderCode =
            "uniform mat4 mvpMetrix;"+
            "attribute vec4 Position;" +
                    "void main() {" +
                    "  gl_Position = mvpMetrix*Position;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 Color;" +
                    "void main() {" +
                    "  gl_FragColor = Color;" +
                    "}";

    public Triangle() {
        ByteBuffer bb = ByteBuffer.allocateDirect(triangleCoords.length*4);
        bb.order(ByteOrder.nativeOrder());

        vertexBuffer = bb.asFloatBuffer();

        vertexBuffer.put(triangleCoords);

        vertexBuffer.position(0);

        int vertexShader = MyRender.loadShader(GLES20.GL_VERTEX_SHADER,vertexShaderCode);

        int fragmentShader = MyRender.loadShader(GLES20.GL_FRAGMENT_SHADER,fragmentShaderCode);

        Program =   GLES20.glCreateProgram();

        GLES20.glAttachShader(Program,vertexShader);
        GLES20.glAttachShader(Program,fragmentShader);
        GLES20.glLinkProgram(Program);


    }


    int mvpMetrixHandle;

    public void draw(float[] mvpMetrix){
        GLES20.glUseProgram(Program);

        mPositionHandle = GLES20.glGetAttribLocation(Program,"Position");

        GLES20.glEnableVertexAttribArray(mPositionHandle);

        GLES20.glVertexAttribPointer(mPositionHandle,COORDS_PER_VERTEX,
                GLES20.GL_FLOAT,false,
                vertexStride,vertexBuffer);

        mColorHandle = GLES20.glGetUniformLocation(Program,"Color");

        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        mvpMetrixHandle = GLES20.glGetUniformLocation(Program, "mvpMetrix");

        // Apply the combined projection and camera view transformations
        GLES20.glUniformMatrix4fv(mvpMetrixHandle, 1, false, mvpMetrix, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        GLES20.glDisableVertexAttribArray(mPositionHandle);

        // Apply the projection and view transformation


    }

}
