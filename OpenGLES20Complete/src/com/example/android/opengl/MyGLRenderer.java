/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

public class MyGLRenderer implements GLSurfaceView.Renderer {

	private LessonTwoRenderer cubeRenderer = new LessonTwoRenderer();
	private Context appContext = null;
	private LessonFourRenderer texRenderer;
	
    private static final String TAG = "MyGLRenderer";
//    static float triangleCoords[] = { // in counterclockwise order:
//        0.0f,  0.622008459f, 0.0f,   // top
//       -0.5f, -0.311004243f, 0.0f,   // bottom left
//        0.5f, -0.311004243f, 0.0f    // bottom right
//    };
//    static float triangleCoords2[] = { // in counterclockwise order:
//        0.0f,  0.622008459f, 0.0f,   // top
//        0.5f, -0.311004243f, 0.0f,    // bottom right
//       -0.5f, -0.311004243f, 0.0f   // bottom left
//    };
    static float c1 = (float) (1 / Math.sqrt(3) / 2);
    static float c2 = (float) (Math.sqrt(3) / 2);
    static float z = (float) Math.sqrt(2 / 3.0);
    static float triangleCoords[] = { // in counterclockwise order:
        0.0f,  0.0f, z,  // top
       -0.5f, -c1, 0.0f,   // bottom left
        0.5f, -c1, 0.0f    // bottom right
    };
    static float triangleCoords2[] = { // in counterclockwise order:
        0.0f, 0.0f, z,  // top
        0.5f, -c1, 0.0f,    // bottom right
        0.0f, c2 - c1, 0.0f   // bottom left
    };
    static float triangleCoords3[] = { // in counterclockwise order:
        0.0f, 0.0f, z,  // top
        0.0f, c2 - c1, 0.0f,    // bottom right
       -0.5f, -c1, 0.0f   // bottom left
    };
    static float triangleCoords4[] = { // in counterclockwise order:
    	-0.0f, c2 - c1, 0.0f,  // top
         0.5f, -c1, 0.0f,    // bottom right
        -0.5f, -c1, 0.0f   // bottom left
    };
    float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 0.2f };
    float color2[] = { 0.5f, 0, 0 , 0.2f };
    float color3[] = { 0, 0.5f, 0 , 0.2f };
    float color4[] = { 0, 0, 0.5f , 0.2f };
    static final int COORDS_PER_VERTEX = 3;
    final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
    private Triangle mTriangle;
    private Triangle mTriangle2;
    private Triangle mTriangle3;
    private Triangle mTriangle4;
    private Square   mSquare;

    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjMatrix = new float[16];
    private final float[] mVMatrix = new float[16];
    private final float[] mRotationMatrix = new float[16];
    private final float[] translateMatrix = new float[16];

    // Declare as volatile because we are updating it from another thread
    public volatile float mAngle;
    
    public MyGLRenderer(Context context) {
    	this.appContext = context;
    	texRenderer = new LessonFourRenderer(context);
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        mTriangle = new Triangle(triangleCoords, COORDS_PER_VERTEX,
        		vertexStride, vertexCount, color);
        mTriangle2 = new Triangle(triangleCoords2, COORDS_PER_VERTEX,
        		vertexStride, vertexCount, color2);
        mTriangle3 = new Triangle(triangleCoords3, COORDS_PER_VERTEX,
        		vertexStride, vertexCount, color3);
        mTriangle4 = new Triangle(triangleCoords4, COORDS_PER_VERTEX,
        		vertexStride, vertexCount, color4);
        mSquare   = new Square();
        
//        cubeRenderer.onSurfaceCreated(unused, config);
        texRenderer.onSurfaceCreated(unused, config);
    }

    @Override
    public void onDrawFrame(GL10 unused) {

        // Draw background color
//        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mVMatrix, 0, 0, 0, 2f, 0f, 0f, -5f, 0f, 1f, 0f);
//        Matrix.setLookAtM(mVMatrix, 0, 0, 0, -0.5f, 0f, 0f, -5f, 0f, 1f, 0f);

        // Calculate the projection and view transformation
//        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);

        // Create a rotation for the square
//        long time = SystemClock.uptimeMillis() % 5000L;
        long time = SystemClock.elapsedRealtime() % 5000L;
        float angle = (360f / 5000f) * ((int) time);
//        Matrix.setRotateM(mRotationMatrix, 0, angle, 0, 1, 0);
        Matrix.setIdentityM(mRotationMatrix, 0);
        Matrix.translateM(mRotationMatrix, 0, -0.5f, -0.5f, 0);
        Matrix.rotateM(mRotationMatrix, 0, angle, 0, 1, 0);

        // Combine the rotation matrix with the projection and camera view
//        Matrix.multiplyMM(mMVPMatrix, 0, mRotationMatrix, 0, mMVPMatrix, 0);

        // Combine the rotation matrix with the projection and camera view
        Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, mRotationMatrix, 0);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);

        // Draw square
        mSquare.draw(mMVPMatrix);

        // Re-calculate the projection and view transformation
        float radius = 1;
        double radAngle = Math.toRadians(angle * 2);
        Matrix.setIdentityM(mRotationMatrix, 0);
        Matrix.rotateM(mRotationMatrix, 0, 30, 1, 0, 0);
        Matrix.translateM(mRotationMatrix, 0, 0.5f, 0.5f, -2);
        Matrix.rotateM(mRotationMatrix, 0, angle, 0.5f, 1, 0);
        Matrix.translateM(mRotationMatrix, 0, 0, 0, radius * (float) Math.cos(radAngle));
//        		radius * (float) Math.sin(radAngle), 0);
//        Matrix.setRotateM(mRotationMatrix, 0, angle, 0, 1, 0);
//        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);

//        // Create a rotation for the triangle
//        time = SystemClock.uptimeMillis() % 4000L;
//        angle = 0.090f * ((int) time);
////        Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0, 0, -1.0f);
//        Matrix.setRotateM(mRotationMatrix, 0, angle, 1, 0, 0);

        // Combine the rotation matrix with the projection and camera view
//        Matrix.multiplyMM(mMVPMatrix, 0, mRotationMatrix, 0, mMVPMatrix, 0);
//        Matrix.translateM(mMVPMatrix, 0, 0, 0, 0);

        // COMMENT: Seems that the Android OpenGLES20 sample code does
        // matrix multiplications in the wrong order. Both here and in the
        // vertex shader position code. Weird results until I fixed this.
        // The correct order is Model x View, the ModelView x Projection.
        
        // Multiply the rotation (model) matrix by the camera view
        Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, mRotationMatrix, 0);

        // Multiply the ModelView matrix by the projection matrix
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);

        // Draw triangle
        mTriangle.draw(mMVPMatrix);
        mTriangle4.draw(mMVPMatrix);
        mTriangle3.draw(mMVPMatrix);
        mTriangle2.draw(mMVPMatrix);
        
//        cubeRenderer.onDrawFrame(unused);
        texRenderer.onDrawFrame(unused);
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
//        Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
        Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1f, 1f, 1, 100);
        
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
//        GLES20.glEnable(GLES20.GL_BLEND);
//        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE);
//        GLES20.glBlendFunc(GLES20.GL_DST_ALPHA, GLES20.GL_ZERO);
        
//        cubeRenderer.onSurfaceChanged(unused, width, height);
        texRenderer.onSurfaceChanged(unused, width, height);
    }

    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    /**
     * Utility method for debugging OpenGL calls. Provide the name of the call
     * just after making it:
     *
     * <pre>
     * mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
     * MyGLRenderer.checkGlError("glGetUniformLocation");</pre>
     *
     * If the operation is not successful, the check throws an error.
     *
     * @param glOperation - Name of the OpenGL call to check.
     */
    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }
}

class Triangle {

    private final String vertexShaderCode =
        // This matrix member variable provides a hook to manipulate
        // the coordinates of the objects that use this vertex shader
        "uniform mat4 uMVPMatrix;" +

        "attribute vec4 vPosition;" +
        "void main() {" +
        // the matrix must be included as a modifier of gl_Position
        // COMMENT: This was the original order which produced weird results.
//        "  gl_Position = vPosition * uMVPMatrix;" +
        "  gl_Position = uMVPMatrix * vPosition;" +
        "}";

    private final String fragmentShaderCode =
        "precision mediump float;" +
        "uniform vec4 vColor;" +
        "void main() {" +
        "  gl_FragColor = vColor;" +
        "}";

    private final FloatBuffer vertexBuffer;
    private final int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;

    private int COORDS_PER_VERTEX;
    private int vertexCount;
    private int vertexStride;

    
    // number of coordinates per vertex in this array
//    static final int COORDS_PER_VERTEX = 3;
//    static float triangleCoords[] = { // in counterclockwise order:
//         0.0f,  0.622008459f, 0.0f,   // top
//        -0.5f, -0.311004243f, 0.0f,   // bottom left
//         0.5f, -0.311004243f, 0.0f    // bottom right
//    };
//    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
//    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    // Set color with red, green, blue and alpha (opacity) values
    float color[] = new float[4];//{ 0.63671875f, 0.76953125f, 0.22265625f, 0.5f };
//    float color2[] = { 1, 0, 0 , 1f };

    public Triangle(float[] triangleCoords, int COORDS_PER_VERTEX,
    		int vertexStride, int vertexCount, float[] color) {
    	
    	this.COORDS_PER_VERTEX = COORDS_PER_VERTEX;
    	this.vertexStride = vertexStride;
    	this.vertexCount = vertexCount;
    	this.color = color;
    	
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                triangleCoords.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(triangleCoords);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);

        // prepare shaders and OpenGL program
        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                                                   vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                                                     fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables

    }

    public void draw(float[] mvpMatrix) {
        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                                     GLES20.GL_FLOAT, false,
                                     vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        MyGLRenderer.checkGlError("glGetUniformLocation");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
        MyGLRenderer.checkGlError("glUniformMatrix4fv");

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}

class Square {

    private final String vertexShaderCode =
        // This matrix member variable provides a hook to manipulate
        // the coordinates of the objects that use this vertex shader
        "uniform mat4 uMVPMatrix;" +

        "attribute vec4 vPosition;" +
        "void main() {" +
        // the matrix must be included as a modifier of gl_Position
        // COMMENT: This was the original order which produced weird results.
//        "  gl_Position = vPosition * uMVPMatrix;" +
        "  gl_Position = uMVPMatrix * vPosition;" +
        "}";

    private final String fragmentShaderCode =
        "precision mediump float;" +
        "uniform vec4 vColor;" +
        "void main() {" +
        "  gl_FragColor = vColor;" +
        "}";

    private final FloatBuffer vertexBuffer;
    private final ShortBuffer drawListBuffer;
    private final int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    static float squareCoords[] = { -0.5f,  0.5f, 0.0f,   // top left
                                    -0.5f, -0.5f, 0.0f,   // bottom left
                                     0.5f, -0.5f, 0.0f,   // bottom right
                                     0.5f,  0.5f, 0.0f }; // top right

    private final short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices

    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    // Set color with red, green, blue and alpha (opacity) values
    float color[] = { 0.2f, 0.709803922f, 0.898039216f, 1.0f };

    public Square() {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
        // (# of coordinate values * 4 bytes per float)
                squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(
        // (# of coordinate values * 2 bytes per short)
                drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

        // prepare shaders and OpenGL program
        int vertexShader = MyGLRenderer.loadShader(GLES20.GL_VERTEX_SHADER,
                                                   vertexShaderCode);
        int fragmentShader = MyGLRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER,
                                                     fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables
    }

    public void draw(float[] mvpMatrix) {
        // Add program to OpenGL environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                                     GLES20.GL_FLOAT, false,
                                     vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        // get handle to shape's transformation matrix
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        MyGLRenderer.checkGlError("glGetUniformLocation");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
        MyGLRenderer.checkGlError("glUniformMatrix4fv");

        // Draw the square
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length,
                              GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}
