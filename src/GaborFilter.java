import java.util.HashMap;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

public class GaborFilter {

    private static Mat gaborFilter(Mat img, Size kSize, double sigma,
                                  double theta, double gamma, double lambda, double psi) {
        img.convertTo(img, CvType.CV_32F);
        Mat gabor = new Mat (img.size(), CvType.CV_32F);
        Mat kernel = Imgproc.getGaborKernel(kSize, sigma, theta, lambda, gamma, psi, CvType.CV_32F);
        Imgproc.filter2D(img, gabor,-1, kernel);
        return gabor;
    }

    public static Mat blendGaborFilters(Mat img, HashMap<String, String> parameters){
        int numOfAngles = Integer.parseInt(parameters.get(GaborLabels.ANGLES.getText()));
        int kSizeVal = Integer.parseInt(parameters.get(GaborLabels.KSIZE.getText()));
        double sigma = Double.parseDouble(parameters.get(GaborLabels.SIGMA.getText()));
        double gamma = Double.parseDouble(parameters.get(GaborLabels.GAMA.getText()));
        double lambda = Double.parseDouble(parameters.get(GaborLabels.LAMBDA.getText()));
        double psi = Double.parseDouble(parameters.get(GaborLabels.PSI.getText()));
        Size kSize = new Size(kSizeVal, kSizeVal);
        Mat[] filters = new Mat[numOfAngles];
        double theta = 0;
        for(int i = 0; i<numOfAngles; i++){
            filters[i] = gaborFilter(img, kSize, sigma, theta, gamma, lambda, psi);
            theta+=Math.PI/numOfAngles;
        }
        Mat filtered = filters[0];
        for(int i = 1; i<filters.length; i++){
            Core.addWeighted(filtered, 1, filters[i], 1, 0, filtered);
        }
        return filtered;
    }
}
