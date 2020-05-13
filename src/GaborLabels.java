import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum GaborLabels {
    KSIZE("Kernel Size: ", "21"),
    ANGLES("Number of Angles: ", "13"),
    SIGMA("Sigma(std): ", "4"),
    GAMA("Gamma: ", "11"),
    LAMBDA("Lambda: ", "9"),
    PSI("Phase offset: ", "0");

    private static final List<Pair<String, String>> VALUES;
    private final String text;
    private final String defaultValues;

    static {
        VALUES = new ArrayList<>();
        for (GaborLabels label : GaborLabels.values()) {
            VALUES.add(new Pair<>(label.text, label.defaultValues));
        }
    }
    GaborLabels(String s, String defaultValues) {
        this.text = s;
        this.defaultValues = defaultValues;
    }

    public String getText() {
        return text;
    }

    public static List<Pair<String, String>> getValues() {
        return Collections.unmodifiableList(VALUES);
    }

}

