package FaceRecognizer;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.IntBuffer;

import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_imgproc;
import static org.bytedeco.javacpp.opencv_contrib.*;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_highgui.*;

/**
 * I couldn't find any tutorial on how to perform face recognition using OpenCV and Java,
 * so I decided to share a viable solution here. The solution is very inefficient in its
 * current form as the training model is built at each run, however it shows what's needed
 * to make it work.
 *
 * The class below takes two arguments: The path to the directory containing the training
 * faces and the path to the image you want to classify. Note that all images has to be of
 * the same size and that the faces already has to be cropped out of their original images
 * (Take a look here http://fivedots.coe.psu.ac.th/~ad/jg/nui07/index.html if you haven't
 * done the face detection yet).
 *
 * For the simplicity of this post, the class also requires that the training images have
 * filename format: <label>-rest_of_filename.png. For example:
 *
 * 1-jon_doe_1.png
 * 1-jon_doe_2.png
 * 2-jane_doe_1.png
 * 2-jane_doe_2.png
 * ...and so on.
 *
 * Source: http://pcbje.com/2012/12/doing-face-recognition-with-javacv/
 *
 * @author Petter Christian Bjelland
 * @author Samuel Audet
 */
public class OpenCVFaceRecognizer {
    public static void main(String[] args) {
        String trainingDir = args[0];
        Mat testImage = imread(args[1], CV_LOAD_IMAGE_GRAYSCALE);

        File root = new File(trainingDir);

        FilenameFilter imgFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                name = name.toLowerCase();
                return name.endsWith(".png");
            }
        };

        File[] imageFiles = root.listFiles(imgFilter);

        MatVector images = new MatVector(imageFiles.length);

        Mat labels = new Mat(imageFiles.length, 1, CV_32SC1);
        IntBuffer labelsBuf = labels.getIntBuffer();

        int counter = 0;

        for (File image : imageFiles) {
            Mat img = imread(image.getAbsolutePath(), CV_LOAD_IMAGE_GRAYSCALE);
            Size normalize = new Size (200, 200);
            Mat normalizeimg = new Mat(normalize, CV_8UC1);;
            opencv_imgproc.resize(img, normalizeimg, normalize);

            int label = Integer.parseInt(image.getName().split("\\-")[0]);

            images.put(counter, normalizeimg);

            labelsBuf.put(counter, label);

            counter++;
        }

        FaceRecognizer faceRecognizer = createLBPHFaceRecognizer();

        faceRecognizer.train(images, labels);
        faceRecognizer.save("LBPH.yaml");

        int predictedLabel = faceRecognizer.predict(testImage);

        int n[] = new int[1];
        double p[] = new double[1];
        faceRecognizer.predict(testImage, n, p);
        double confidence = p[0];

        System.out.println("Predicted label: " + predictedLabel);
        System.out.println("Predicted confidence: " + confidence);
    }
}
