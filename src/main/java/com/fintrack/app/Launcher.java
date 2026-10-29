package com.fintrack.app;

/**
 * Plain entry point that does not extend {@code Application}.
 * <p>
 * Running a class that extends {@code javafx.application.Application} directly
 * from an IDE on the classpath fails with "JavaFX runtime components are missing".
 * Starting from this class avoids that check.
 */
public class Launcher {

    /**
     * Delegates to {@link FinApp#main(String[])}.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        FinApp.main(args);
    }
}
