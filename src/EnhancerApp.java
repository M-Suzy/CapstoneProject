import javafx.util.Pair;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

public class EnhancerApp {
    private static final int numOfParameters = 6;
    private static final List<Pair<String, String>> labels = GaborLabels.getValues();
    private static JFrame gaborFrame;
    private static JFrame imgFrame;
    private static JLabel imgLabel;
    private static JPanel basePanel;
    private static JPanel topPanel;
    private static JPanel bottomPanel;
    private static JButton uploadBtn;
    private static JButton saveBtn;
    private static JButton filterBtn;
    private static JTextField[] inputs;
    private static HashMap<String, String> parameters;
    private static File image;
    private static String imagePath, ext;
    private static Mat src;

    EnhancerApp() {
        inputs = new JTextField[numOfParameters];
        parameters = new HashMap<>(numOfParameters);
        basePanel = new JPanel(new FlowLayout());
        uploadBtn = new JButton("Upload image");
        saveBtn = new JButton("Save image");
        filterBtn = new JButton("Filter");
        topPanel = new JPanel(new GridLayout(numOfParameters, 2));
        bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        gaborFrame = new JFrame("Gabor parameters");
        gaborFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        basePanel.setOpaque(true);
        gaborFrame.setContentPane(basePanel);
        gaborFrame.setSize(new Dimension(290, 300));
        gaborFrame.setVisible(true);
    }

    public static void imshow(Mat src, String ext) {
        BufferedImage bufImage = null;
        try {
            MatOfByte matOfByte = new MatOfByte();
            Imgcodecs.imencode(ext, src, matOfByte);
            byte[] byteArray = matOfByte.toArray();
            InputStream in = new ByteArrayInputStream(byteArray);
            bufImage = ImageIO.read(in);
            if (imgFrame == null) {
                imgFrame = new JFrame("Image");
                imgLabel = new JLabel(new ImageIcon(bufImage));
                imgFrame.getContentPane().setLayout(new FlowLayout());
                imgFrame.getContentPane().add(imgLabel);
                imgFrame.pack();
                imgFrame.setVisible(true);
            } else {
                imgLabel.setIcon(new ImageIcon(bufImage));
                imgFrame.repaint();
                imgFrame.pack();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createGUI() {
        basePanel.add(uploadBtn);
        uploadBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                jfc.setDialogTitle("Select an image");
                jfc.setAcceptAllFileFilterUsed(false);
                FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG, JPG, BMP images",
                        "bmp", "png", "jpg");
                jfc.addChoosableFileFilter(filter);
                int returnValue = jfc.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    image = jfc.getSelectedFile();
                    imagePath = String.valueOf(image.getAbsoluteFile());
                    src = Imgcodecs.imread(imagePath);
                    ext = "." + imagePath.substring(imagePath.lastIndexOf(".") + 1);
                    imshow(src, ext);
                }
            }
        });

        for (int i = 0; i < numOfParameters; i++) {
            JLabel l = new JLabel(labels.get(i).getKey(), JLabel.TRAILING);
            topPanel.add(l);
            inputs[i] = new JTextField(5);
            inputs[i].setText(labels.get(i).getValue());
            l.setLabelFor(inputs[i]);
            topPanel.add(inputs[i]);
        }
        basePanel.add(topPanel);
        bottomPanel.add(filterBtn);
        filterBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < numOfParameters; i++)
                    parameters.put(labels.get(i).getKey(), inputs[i].getText());
                Mat enhanced = GaborFilter.blendGaborFilters(src, parameters);
               // Mat edges = doSobel(enhanced);
                imshow(enhanced, ext);
            }
        });
        bottomPanel.add(saveBtn);
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                    jfc.setDialogTitle("Select an image");
                    int returnValue = jfc.showSaveDialog(null);
                    if (returnValue == JFileChooser.APPROVE_OPTION) {
                        File outputfile = jfc.getSelectedFile();
                        Image im = imgFrame.getIconImage();
                        BufferedImage bf = (BufferedImage) (im);
                        try {
                            ImageIO.write(bf, ".png", outputfile);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
            }
        });
        basePanel.add(bottomPanel);
    }
    private Mat doSobel(Mat frame) {
        Mat gray = new Mat();
        Imgproc.cvtColor(frame, gray, Imgproc.COLOR_BGR2GRAY);
        Mat edges = new Mat();

        // Detecting the edges
        Imgproc.Canny(gray, edges, 60, 60*3);

        return edges;

    }
}