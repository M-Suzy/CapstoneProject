import org.opencv.core.Core;

public class EnhancerAppDriver {
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new EnhancerApp().createGUI();
            }
        });
    }
}
