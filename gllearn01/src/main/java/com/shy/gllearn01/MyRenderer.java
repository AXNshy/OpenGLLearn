package com.shy.gllearn01;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by axnshy on 2017/7/10.
 */

public class MyRenderer implements GLSurfaceView.Renderer {


    //store model data in float buffer

    private FloatBuffer mTriangle1Vertices;
    private FloatBuffer mTriangle2Vertices;
    private FloatBuffer mTriangle3Vertices;

    private int Program;

    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;

    private float[] mMVPMatrix = new float[16];
    private float[] mViewMatrix = new float[16];

    /** Store the projection matrix. This is used to project the scene onto a 2D viewport. */
    private float[] mProjectionMatrix = new float[16];

    /**
     * Store the model matrix. This matrix is used to move models from object space (where each model can be thought
     * of being located at the center of the universe) to world space.
     */
    private float[] mModelMatrix = new float[16];

    public MyRenderer() {


    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        // Set the background clear color to gray.
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);
        final float[] triangle1VerticesData = {
                // x,y,z
                //r,g,b,a
                -0.5f,-0.25f,0.0f,
                1.0f,0.0f,0.0f,1.0f,

                0.5f,-0.25f,0.0f,
                0.0f,0.0f,1.0f,1.0f,

                0.0f,0.55987923f,0.0f,
                0.0f,1.0f,0.0f,1.0f
        };
        // This triangle is yellow, cyan, and magenta.
        final float[] triangle2VerticesData = {
                // X, Y, Z,
                // R, G, B, A
                -0.5f, -0.25f, 0.0f,
                1.0f, 1.0f, 0.0f, 1.0f,

                0.5f, -0.25f, 0.0f,
                0.0f, 1.0f, 1.0f, 1.0f,

                0.0f, 0.559016994f, 0.0f,
                1.0f, 0.0f, 1.0f, 1.0f};

        // This triangle is white, gray, and black.
        final float[] triangle3VerticesData = {
                // X, Y, Z,
                // R, G, B, A
                -0.5f, -0.25f, 0.0f,
                1.0f, 1.0f, 1.0f, 1.0f,

                0.5f, -0.25f, 0.0f,
                0.5f, 0.5f, 0.5f, 1.0f,

                0.0f, 0.559016994f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f};

        mTriangle1Vertices = ByteBuffer.allocateDirect(triangle1VerticesData.length*4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mTriangle2Vertices = ByteBuffer.allocateDirect(triangle2VerticesData.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mTriangle3Vertices = ByteBuffer.allocateDirect(triangle3VerticesData.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mTriangle1Vertices.put(triangle1VerticesData).position(0);
        mTriangle2Vertices.put(triangle2VerticesData).position(0);
        mTriangle3Vertices.put(triangle3VerticesData).position(0);






        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,vertexShaderCode);

        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,fragmentShaderCode);



        Program = createProgram(vertexShader,fragmentShader);

        Log.d("TAG","Program : "+Program);

        mMVPMatrixHandle = GLES20.glGetUniformLocation(Program,"u_MVPMatrix");
        mPositionHandle = GLES20.glGetAttribLocation(Program,"a_Position");
        mColorHandle = GLES20.glGetAttribLocation(Program,"a_Color");

        GLES20.glUseProgram(Program);


        // Position the eye behind the origin.
        final float eyeX = 0.0f;
        final float eyeY = 0.0f;
        final float eyeZ = 1.5f;

        // We are looking toward the distance
        final float lookX = 0.0f;
        final float lookY = 0.0f;
        final float lookZ = -5.0f;

        // Set our up vector. This is where our head would be pointing were we holding the camera.
        final float upX = 0.0f;
        final float upY = 1.0f;
        final float upZ = 0.0f;

        // Set the view matrix. This matrix can be said to represent the camera position.
        // NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
        // view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {

        GLES20.glViewport(0,0,i,i1);
        // Create a new perspective projection matrix. The height will stay the same
        // while the width will vary as per aspect ratio.
        final float ratio = (float) i / i1;
        final float left = -ratio;
        final float right = ratio;
        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = 1.0f;
        final float far = 10.0f;

        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {

        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT|GLES20.GL_COLOR_BUFFER_BIT);
        long time = SystemClock.uptimeMillis()%10000L;
        float angleInDegrees = (360.0f/10000f)*((int)time);
        Matrix.setIdentityM(mModelMatrix,0);
        Matrix.setRotateM(mModelMatrix,0,angleInDegrees,0.0f,0.0f,1.0f);
        drawTriangle(mTriangle1Vertices);

        // Draw one translated a bit down and rotated to be flat on the ground.
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 0.0f, -0.5f, 0.0f);
        Matrix.rotateM(mModelMatrix, 0, 90.0f, 1.0f, 0.0f, 0.0f);
        Matrix.rotateM(mModelMatrix, 0, angleInDegrees, 0.0f, 0.0f, 1.0f);
        drawTriangle(mTriangle2Vertices);

        // Draw one translated a bit to the right and rotated to be facing to the left.
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 0.0f, -1.0f, 0.0f);
        Matrix.rotateM(mModelMatrix, 0, 90.0f, 0.0f, 1.0f, 0.0f);
        Matrix.rotateM(mModelMatrix, 0, angleInDegrees, 0.0f, 0.0f, 1.0f);
        drawTriangle(mTriangle3Vertices);
    }
    private void drawTriangle(FloatBuffer mTriangleVertices) {

        mTriangle1Vertices.position(0);
        GLES20.glVertexAttribPointer(mPositionHandle,3,GLES20.GL_FLOAT,false,7*4,mTriangleVertices);
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        mTriangle1Vertices.position(3);
        GLES20.glVertexAttribPointer(mColorHandle,4,GLES20.GL_FLOAT,false,7*4,mTriangleVertices);
        GLES20.glEnableVertexAttribArray(mColorHandle);

        Matrix.multiplyMM(mMVPMatrix,0,mViewMatrix,0,mModelMatrix,0);
        Matrix.multiplyMM(mMVPMatrix,0,mProjectionMatrix,0,mMVPMatrix,0);

        GLES20.glUniformMatrix4fv(mMVPMatrixHandle,1,false,mMVPMatrix,0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,3);
    }


    private  static int loadShader(int type,String ShaderCode){
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader,ShaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }

    private static int createProgram(int vertexShader,int fragmentShader){
        int program =   GLES20.glCreateProgram();

        GLES20.glAttachShader(program,vertexShader);
        GLES20.glAttachShader(program,fragmentShader);
        GLES20.glBindAttribLocation(program,0,"a_Position");
        GLES20.glBindAttribLocation(program,1,"a_Color");

        GLES20.glLinkProgram(program);

        return program;
    }

    final String vertexShaderCode =
            "uniform mat4 u_MVPMatrix;      \n"     // A constant representing the combined model/view/projection matrix.

                    + "attribute vec4 a_Position;     \n"     // Per-vertex position information we will pass in.
                    + "attribute vec4 a_Color;        \n"     // Per-vertex color information we will pass in.

                    + "varying vec4 v_Color;          \n"     // This will be passed into the fragment shader.

                    + "void main()                    \n"     // The entry point for our vertex shader.
                    + "{                              \n"
                    + "   v_Color = a_Color;          \n"     // Pass the color through to the fragment shader.
                    // It will be interpolated across the triangle.
                    + "   gl_Position = u_MVPMatrix   \n"     // gl_Position is a special variable used to store the final position.
                    + "               * a_Position;   \n"     // Multiply the vertex by the matrix to get the final point in
                    + "}                              \n";

    final String fragmentShaderCode =
            "precision mediump float;       \n"     // Set the default precision to medium. We don't need as high of a
                    // precision in the fragment shader.
                    + "varying vec4 v_Color;          \n"     // This is the color from the vertex shader interpolated across the
                    // triangle per fragment.
                    + "void main()                    \n"     // The entry point for our fragment shader.
                    + "{                              \n"
                    + "   gl_FragColor = v_Color;     \n"     // Pass the color directly through the pipeline.
                    + "}                              \n";
}
