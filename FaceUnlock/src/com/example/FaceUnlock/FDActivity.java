package com.example.FaceUnlock;

import static org.bytedeco.javacpp.opencv_contrib.createLBPHFaceRecognizer;
import static org.bytedeco.javacpp.opencv_core.CV_8UC1;
/*import static org.bytedeco.javacpp.opencv_contrib.createEigenFaceRecognizer;
import static org.bytedeco.javacpp.opencv_contrib.subspaceProject;
import static org.bytedeco.javacpp.opencv_contrib.subspaceReconstruct;
import static org.bytedeco.javacpp.opencv_core.CV_8U;
import static org.bytedeco.javacpp.opencv_core.CV_8UC1;
import static org.bytedeco.javacpp.opencv_core.CV_L2;
import static org.bytedeco.javacpp.opencv_core.norm;*/

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;

import org.bytedeco.javacpp.opencv_contrib.FaceRecognizer;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_imgproc;

import static org.bytedeco.javacpp.opencv_highgui.*;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

import com.example.FaceUnlock.R;

import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class FDActivity extends Activity implements CameraBridgeViewBase.CvCameraViewListener2 {

    private static final String TAG = "OpenCv";
    private CameraBridgeViewBase mOpenCvCameraView;
    private Mat                    mRgba;
    private Mat                    mGray;
    private File                   mCascadeFileFace;
//    private File                   mCascadeFileEye;
//    private File                   mCascadeFileEyeWithGlass;
    private static final Scalar    GREEN               = new Scalar(0, 255, 0, 255);
    private static final Scalar    WHITE               = new Scalar(255, 255, 255, 255);
    private static final Scalar    BLACK               = new Scalar(0, 255, 255, 255);
    private static final Scalar    GRAY                = new Scalar(128, 255, 255, 255);
    private int					   threshold		   = 50;
    private CascadeClassifier      mJavaDetectorFace;
//    private CascadeClassifier 	   mJavaDetectorEye;
//    private CascadeClassifier 	   mJavaDetectorEyeWithGlass;
    private float                  mRelativeFaceSize   = 0.2f;
    private int                    mAbsoluteFaceSize   = 0;
//    private float 				   EYE_SX = 0.16f;
//    private float 				   EYE_SY = 0.26f;
//    private float 				   EYE_SW = 0.30f;
//    private float 				   EYE_SH = 0.28f;
	private HomeKeyLocker          locker;
	TextView distance;
	private Handler handler=null;
	private String result=null;
    public FDActivity() {
    	Log.i(TAG, "Instantiated a " + this.getClass());
    }
    
    public BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
    	@Override
    	public void onManagerConnected(int status) {
    		switch(status) {
	    		case LoaderCallbackInterface.SUCCESS:
	    		{
	    			Log.i(TAG, "OpenCV loaded successfully");
	    			
	    			try {
	    				// load face cascade file from application resources
	    				InputStream isface = getResources().openRawResource(R.raw.haarcascade_frontalface_alt2);
	    				File cascadeDirFace = getDir("cascade", Context.MODE_PRIVATE);
	    				mCascadeFileFace = new File(cascadeDirFace, "haarcascade_frontalface_alt2.xml");
	    				FileOutputStream osface = new FileOutputStream(mCascadeFileFace);
	    				
	    				byte[] bufferface = new byte[4096];
	    				int bytesReadFace;
	    				while ((bytesReadFace = isface.read(bufferface)) != -1) {
	    					osface.write(bufferface, 0, bytesReadFace);
	    				}
	    				isface.close();
	    				osface.close();
	    				
	    				mJavaDetectorFace = new CascadeClassifier(mCascadeFileFace.getAbsolutePath());
	    				if (mJavaDetectorFace.empty()) {
	    					Log.e(TAG, "Failed to load face cascade classifier");
	    					mJavaDetectorFace = null;
	    				}
	    				else 
	    					Log.i(TAG, "Loaded face cascade classifier from " + mCascadeFileFace.getAbsolutePath());
	    				cascadeDirFace.delete();
	    				
	    				/*InputStream iseye = getResources().openRawResource(R.raw.haarcascade_eye);
	    				File cascadeDirEye = getDir("cascade", Context.MODE_PRIVATE);
	    				mCascadeFileEye = new File(cascadeDirEye, "haarcascade_eye.xml");
	    				FileOutputStream oseye = new FileOutputStream(mCascadeFileEye);
	    				
	    				byte[] buffereye = new byte[4096];
	    				int bytesReadEye;
	    				while ((bytesReadEye = iseye.read(buffereye)) != -1) {
	    					oseye.write(buffereye, 0, bytesReadEye);
	    				}
	    				iseye.close();
	    				oseye.close();
	    				
	    				mJavaDetectorEye = new CascadeClassifier(mCascadeFileEye.getAbsolutePath());
	    				if (mJavaDetectorEye.empty()) {
	    					Log.e(TAG, "Failed to load eye cascade classifier");
	    					mJavaDetectorEye = null;
	    				}
	    				else 
	    					Log.i(TAG, "Loaded eye cascade classifier from " + mCascadeFileEye.getAbsolutePath());
	    				cascadeDirEye.delete();
	    				
	    				InputStream isEyeWithGlass = getResources().openRawResource(R.raw.haarcascade_eye_tree_eyeglasses);
	    				File cascadeDirEyeWithGlass = getDir("cascade", Context.MODE_PRIVATE);
	    				mCascadeFileEyeWithGlass = new File(cascadeDirEyeWithGlass, "haarcascade_eye_tree_eyeglasses.xml");
	    				FileOutputStream osEyeWithGlass = new FileOutputStream(mCascadeFileEyeWithGlass);
	    				
	    				byte[] bufferEyeWithGlass = new byte[4096];
	    				int bytesReadEyeWithGlass;
	    				while ((bytesReadEyeWithGlass = isEyeWithGlass.read(bufferEyeWithGlass)) != -1) {
	    					osEyeWithGlass.write(bufferEyeWithGlass, 0, bytesReadEyeWithGlass);
	    				}
	    				isEyeWithGlass.close();
	    				osEyeWithGlass.close();
	    				
	    				mJavaDetectorEyeWithGlass = new CascadeClassifier(mCascadeFileEyeWithGlass.getAbsolutePath());
	    				if (mJavaDetectorEyeWithGlass.empty()) {
	    					Log.e(TAG, "Failed to load eye with glass cascade classifier");
	    					mJavaDetectorEyeWithGlass = null;
	    				}
	    				else 
	    					Log.i(TAG, "Loaded eye with glass cascade classifier from " + mCascadeFileEyeWithGlass.getAbsolutePath());
	    				cascadeDirEyeWithGlass.delete();*/
	    			}
	    			catch( IOException e) {
	    				e.printStackTrace();
	    				Log.e(TAG, "Failed to load cascade. Exception thrown: " + e);
	    			}
	    			
	    			mOpenCvCameraView.enableView();
	    		} break;
	    		default: {
	    			super.onManagerConnected(status);
	    		} break;
    		}
    	}
    };

    @Override
    public void onResume() {
        super.onResume();
    }
    
    public void unlock(View v) {
    	Log.i(TAG, "Unlock clicked");
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_9, this, mLoaderCallback);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	Log.i(TAG, "Called onCreate");
    	
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        locker = new HomeKeyLocker();
        locker.lock(this);
        
 	    if(getIntent()!=null&&getIntent().hasExtra("kill")&&getIntent().getExtras().getInt("kill")==1){
       	    finish();
        }
 	   
 	    try {
 	        startService(new Intent(this,LockscreenService.class));

 	        StateListener phoneStateListener = new StateListener();
 	        TelephonyManager telephonyManager =(TelephonyManager)getSystemService(TELEPHONY_SERVICE);
 	        telephonyManager.listen(phoneStateListener,PhoneStateListener.LISTEN_CALL_STATE);

 	    }
        catch (Exception e) {
        	
        }
 	    handler= new Handler();  
        TextView tv = (TextView) findViewById(R.id.textView1);
        distance = (TextView) findViewById(R.id.distance);
        DateFormat df = DateFormat.getDateInstance();
        tv.setText(df.format(new Date()));
        
        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.fd_activity_surface_view);
        mOpenCvCameraView.setCvCameraViewListener(this);
    }

    class StateListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            super.onCallStateChanged(state, incomingNumber);
            switch(state){
                case TelephonyManager.CALL_STATE_RINGING:
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    System.out.println("call Activity off hook");
                	finish();
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    break;
            }
        }
    };
    
    @Override
    public void onPause() {
        super.onPause();
        if(mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }
    
	 
	@Override
	public void onCameraViewStarted(int width, int height) {
        mGray = new Mat();
        mRgba = new Mat();		
	}

	@Override
	public void onCameraViewStopped() {
        mGray.release();
        mRgba.release();
	}
	
	/*// Search for both eyes within the given face image. Returns the eye centers in 'leftEye' and 'rightEye',
	// or sets them to (-1,-1) if each eye was not found. Note that you can pass a 2nd eyeCascade if you
	// want to search eyes using 2 different cascades. For example, you could use a regular eye detector
	// as well as an eyeglasses detector, or a left eye detector as well as a right eye detector.
	// Or if you don't want a 2nd eye detection, just pass an uninitialized CascadeClassifier.
	// Can also store the searched left & right eye regions if desired.
	public void detectBothEyes (org.opencv.core.Mat face, org.opencv.core.Point leftEye, org.opencv.core.Point rightEye, org.opencv.core.Rect searchedLeftEye, org.opencv.core.Rect searchedRightEye) {
		int leftX = Math.round(face.cols() * EYE_SX);
	    int topY = Math.round(face.rows() * EYE_SY);
	    int widthX = Math.round(face.cols() * EYE_SW);
	    int heightY = Math.round(face.rows() * EYE_SH);
	    int rightX = Math.round(face.cols() * (1 - EYE_SX - EYE_SW));
	    Mat topLeftOfFace = face.submat(new Rect(leftX, topY, widthX, heightY));
	    Mat topRightOfFace = face.submat(new Rect(rightX, topY, widthX, heightY));
	    MatOfRect leftEyeRect = new MatOfRect();
	    MatOfRect rightEyeRect = new MatOfRect();
	    
	    if (mJavaDetectorEye != null) {
	    	mJavaDetectorEye.detectMultiScale(topLeftOfFace, leftEyeRect, 1.1, 3, Objdetect.CASCADE_FIND_BIGGEST_OBJECT, new Size(mAbsoluteFaceSize, mAbsoluteFaceSize), new Size());
	    	Rect[] leftEyeArray = leftEyeRect.toArray();
	    	// If the eye was not detected, try a different cascade classifier.
	    	if (leftEyeArray.length <= 0){
	    		mJavaDetectorEyeWithGlass.detectMultiScale(topLeftOfFace, leftEyeRect, 1.1, 3, Objdetect.CASCADE_FIND_BIGGEST_OBJECT, new Size(mAbsoluteFaceSize, mAbsoluteFaceSize), new Size());
	    		leftEyeArray = leftEyeRect.toArray();
	    	}
	    	// Check if the eye was detected.
	    	if (leftEyeArray.length > 0){
	    		// Adjust the left-eye rectangle, since it starts on the left side of the image.
	    		leftEyeArray[0].x += leftX;
	    		leftEyeArray[0].y += topY;
	    		leftEye = new Point(leftEyeArray[0].x + leftEyeArray[0].width/2, leftEyeArray[0].y + leftEyeArray[0].height/2);
	    	}
	    	else {
	    		leftEye = new Point(-1, -1);
	    	}
	    	mJavaDetectorEye.detectMultiScale(topRightOfFace, rightEyeRect, 1.1, 3, Objdetect.CASCADE_FIND_BIGGEST_OBJECT, new Size(mAbsoluteFaceSize, mAbsoluteFaceSize), new Size());
	    	Rect[] rightEyeArray = rightEyeRect.toArray();
	    	// If the eye was not detected, try a different cascade classifier.
	    	if (rightEyeArray.length <= 0){
	    		mJavaDetectorEyeWithGlass.detectMultiScale(topRightOfFace, rightEyeRect, 1.1, 3, Objdetect.CASCADE_FIND_BIGGEST_OBJECT, new Size(mAbsoluteFaceSize, mAbsoluteFaceSize), new Size());
	    		rightEyeArray = rightEyeRect.toArray();
	    	}
	    	// Check if the eye was detected.
	    	if (rightEyeArray.length > 0){
	    		// Adjust the right-eye rectangle, since it starts on the right side of the image.
	    		rightEyeArray[0].x += rightX;
	    		rightEyeArray[0].y += topY;
	    		rightEye = new Point(rightEyeArray[0].x + rightEyeArray[0].width/2, rightEyeArray[0].y + rightEyeArray[0].height/2);
	    	}
	    	else {
	    		rightEye = new Point(-1, -1);
	    	}
	    }
	}*/
	
	// Remove the outer border of the face, so it doesn't include the background & hair.
	// Keeps the center of the rectangle at the same place, rather than just dividing all values by 'scale'.
	public Rect scaleRectFromCenter(Rect wholeFaceRect, float scale)
	{
	    float faceCenterX = wholeFaceRect.x + wholeFaceRect.width * 0.5f;
	    float faceCenterY = wholeFaceRect.y + wholeFaceRect.height * 0.5f;
	    float newWidth = wholeFaceRect.width * scale;
	    float newHeight = wholeFaceRect.height * scale;
	    Rect faceRect = new Rect();
	    faceRect.width = Math.round(newWidth);                     // Shrink the region
	    faceRect.height = Math.round(newHeight);
	    faceRect.x = Math.round(faceCenterX - newWidth * 0.5f);    // Move the region so that the center is still the same spot.
	    faceRect.y = Math.round(faceCenterY - newHeight * 0.5f + 20);

	    return faceRect;
	}

	// It is common that there is stronger light from one half of the face than the other. In that case,
    // if you simply did histogram equalization on the whole face then it would make one half dark and
    // one half bright. So we will do histogram equalization separately on each face half, so they will
    // both look similar on average. But this would cause a sharp edge in the middle of the face, because
    // the left half and right half would be suddenly different. So we also histogram equalize the whole
    // image, and in the middle part we blend the 3 images together for a smooth brightness transition.
	public void equalizeLeftAndRightHalves(Mat faceImg)
	{
	    int w = faceImg.cols();
	    int h = faceImg.rows();
	    
	    // 1) First, equalize the whole face.
	    Mat wholeFace =new Mat(faceImg.size(), CvType.CV_8U);;
	    Imgproc.equalizeHist(faceImg, wholeFace);
	    
	    // 2) Equalize the left half and the right half of the face separately.
	    int midX = w/2;
	    Mat leftSide = faceImg.submat(0, h, 0, midX);
	    Mat rightSide = faceImg.submat(0, h, midX, w);
	    Imgproc.equalizeHist(leftSide, leftSide);
	    Imgproc.equalizeHist(rightSide, rightSide);
	    
	    // 3) Combine the left half and right half and whole face together, so that it has a smooth transition.
	    for (int y=0; y<h; y++) {
	        for (int x=0; x<w; x++) {
	            double v;
	            if (x < w/4) {          // Left 25%: just use the left face.
	            	double[] data1 = leftSide.get(y,x);
	            	v = data1[0];
	            }
	            else if (x < w*2/4) {   // Mid-left 25%: blend the left face & whole face.
	            	double[] data2 = leftSide.get(y,x);
	            	double[] data3 = wholeFace.get(y,x);
	            	double lv = data2[0];
	                double wv = data3[0];
	                // Blend more of the whole face as it moves further right along the face.
	                float f = (x - w*1/4) / (float)(w*0.25f);
	                v = (1.0f - f) * lv + (f) * wv;
	            }
	            else if (x < w*3/4) {   // Mid-right 25%: blend the right face & whole face.
	            	double[] data4 = rightSide.get(y,x-midX);
	            	double[] data5 = wholeFace.get(y,x);
	            	double rv = data4[0];
	            	double wv = data5[0];
	                // Blend more of the right-side face as it moves further right along the face.
	                float f = (x - w*2/4) / (float)(w*0.25f);
	                v = (1.0f - f) * wv + (f) * rv;
	            }
	            else {                  // Right 25%: just use the right face.
	            	double[] data6 = rightSide.get(y,x-midX);
	                v = data6[0];
	            }
	            double[] data7 = new double[1];
	            data7[0] = v;
	            faceImg.put(y,x,data7);
	        }// end x loop
	    }//end y loop
	}
	
	@Override
	public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
		
		mRgba = inputFrame.rgba();
	    mGray = inputFrame.gray();
	    Imgproc.equalizeHist(mGray, mGray);
	    
		new Mat();
        if (mAbsoluteFaceSize == 0) {
            int height = mGray.rows();
            if (Math.round(height * mRelativeFaceSize) > 0) {
                mAbsoluteFaceSize = Math.round(height * mRelativeFaceSize);
            }
        }

        MatOfRect faces = new MatOfRect();

        if (mJavaDetectorFace != null) {
        	Core.flip(mGray.t(), mGray, 0);
        	Core.flip(mRgba.t(), mRgba, 0);
            mJavaDetectorFace.detectMultiScale(mGray, faces, 1.1, 3, Objdetect.CASCADE_FIND_BIGGEST_OBJECT, new Size(mAbsoluteFaceSize, mAbsoluteFaceSize), new Size());
        }
        Rect[] facesArray = faces.toArray();
        for (int i = 0; i < facesArray.length; i++) {
        	// Performs Face Preprocessing as a combination of:
        	//  - geometrical scaling, rotation and translation using Eye Detection,
        	//  - smoothing away image noise using a Bilateral Filter,
        	//  - standardize the brightness on both left and right sides of the face independently using separated Histogram Equalization,
        	//  - removal of background and hair using an Elliptical Mask.
        	float scale = 0.7f;
        	Rect croppedface = scaleRectFromCenter(facesArray[i], scale);
        	Mat sub = mGray.submat(croppedface);
        	/*Point leftEye = new Point();
        	Point rightEye = new Point();
        	Rect searchedLeftEye = new Rect();
        	Rect searchedRightEye = new Rect();
        	detectBothEyes(sub, leftEye, rightEye, searchedLeftEye, searchedRightEye);
            // Check if both eyes were detected.
            if (leftEye.x >= 0 && rightEye.x >= 0) {
            	// Get the center between the 2 eyes.
            	Point eyesCenter = new Point( (leftEye.x + rightEye.x) * 0.5f, (leftEye.y + rightEye.y) * 0.5f );
            	// Get the angle between the 2 eyes.
                double dy = (rightEye.y - leftEye.y);
                double dx = (rightEye.x - leftEye.x);
                double len = Math.sqrt(dx*dx + dy*dy);
                double angle = Math.atan2(dy, dx) * 180.0/Math.PI; // Convert from radians to degrees.
                
                // Hand measurements shown that the left eye center should ideally be at roughly (0.19, 0.14) of a scaled face image.
                double DESIRED_LEFT_EYE_X = 0.16;
                double DESIRED_RIGHT_EYE_X = (1.0f - DESIRED_LEFT_EYE_X);
                double DESIRED_LEFT_EYE_Y = 0.14;
                // Get the amount we need to scale the image to be the desired fixed size we want.
                double desiredLen = (DESIRED_RIGHT_EYE_X - DESIRED_LEFT_EYE_X) * 300;
                double scale = desiredLen / len;
                // Get the transformation matrix for rotating and scaling the face to the desired angle & size.
                Mat rot_mat = Imgproc.getRotationMatrix2D(eyesCenter, angle, scale);
                // Shift the center of the eyes to be the desired center between the eyes.
                rot_mat.put(0, 2, 300 * 0.5f - eyesCenter.x);
                rot_mat.put(1, 2, 300 * DESIRED_LEFT_EYE_Y - eyesCenter.y);
                // Rotate and scale and translate the image to the desired angle & size & position!
                // Note that we use 'w' for the height instead of 'h', because the input face has 1:1 aspect ratio.
                Mat warped = new Mat(300, 300, CvType.CV_8U, GRAY); // Clear the output image to a default grey.
                Imgproc.warpAffine(sub, warped, rot_mat, warped.size());
            }*/
            
            //Imgproc.equalizeHist(sub, sub);
            equalizeLeftAndRightHalves(sub);
            Mat subfilter = new Mat(sub.size(), CvType.CV_8U);
            Imgproc.bilateralFilter(sub, subfilter, 0, 20.0, 2.0);
            
            // Filter out the corners of the face, since we mainly just care about the middle parts.
            // Draw a filled ellipse in the middle of the face-sized image.
            Mat mask = new Mat(sub.size(), CvType.CV_8UC1, WHITE);
            double dw = Math.round(sub.cols());
            double dh = Math.round(sub.rows());
            Point faceCenter = new Point(dw*0.5, dh*0.4);
            Size size = new Size(dw*0.5, dh*0.8);
            Core.ellipse(mask, faceCenter, size, 0.0, 0.0, 360.0, BLACK, Core.FILLED);
            subfilter.setTo(GRAY, mask);
            
        	File path = new File(Environment.getExternalStorageDirectory() + "/Images/");
        	path.mkdirs();
        	File file = new File(path, "face.png");
        	String filename = file.toString();
        	Highgui.imwrite(filename, subfilter);
        	Core.rectangle(mRgba, facesArray[i].tl(), facesArray[i].br(), GREEN, 3);
        	
        	FaceRecognizer faceRecognizer = createLBPHFaceRecognizer();
        	//FaceRecognizer faceRecognizer = createEigenFaceRecognizer();
        	
        	faceRecognizer.load(Environment.getExternalStorageDirectory() + "/Images/LBPH.yaml");
            opencv_core.Mat testImage = imread(Environment.getExternalStorageDirectory() + "/Images/face.png", CV_LOAD_IMAGE_GRAYSCALE);
            opencv_core.Size normalize = new opencv_core.Size (200, 200);
            opencv_core.Mat normalizetestimg = new opencv_core.Mat(normalize, CV_8UC1);
            opencv_imgproc.resize(testImage, normalizetestimg, normalize);
            
            /*opencv_core.Mat eigenvectors = faceRecognizer.getMat("eigenvectors");
            opencv_core.Mat averageFaceRow = faceRecognizer.getMat("mean");
            opencv_core.Mat projection = subspaceProject(eigenvectors, averageFaceRow, normalizetestimg.reshape(1,1));
            opencv_core.Mat reconstructionRow = subspaceReconstruct(eigenvectors, averageFaceRow, projection);
            int faceHeight = normalizetestimg.rows();
            opencv_core.Mat reconstructionMat = reconstructionRow.reshape(1, faceHeight);
            opencv_core.Mat reconstructedFace = new opencv_core.Mat(reconstructionMat.size(), CV_8U);
            reconstructionMat.convertTo(reconstructedFace, CV_8U, 1, 0);
            double errorL2 = norm(normalizetestimg, CV_L2 , reconstructedFace);
        	double similarity = errorL2 / (double)(normalizetestimg.rows() * normalizetestimg.cols());*/
            
            int n[] = new int[1];
            double p[] = new double[1];
            faceRecognizer.predict(normalizetestimg, n, p);
            double confidence = p[0];
            double dc = Math.floor(confidence);
            int c = (int) dc;
            if (c < threshold) {
            	result=c + " legimate user";
            	//locker.unlock();
            	//finish();
            }
            else {
            	result=c + " illegal user";
            }
            handler.post(runnableUi);
        	}
        return mRgba.t();
	}
	
	Runnable runnableUi=new  Runnable(){  
        @Override  
        public void run() {
        	distance.setText(result);
        }  
          
    };  
}
