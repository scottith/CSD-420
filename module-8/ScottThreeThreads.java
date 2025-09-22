package com.mycompany.csd420_project01;
/*
 * Scott Macioce
 * 09/22/2025
 * Module 8 Assignment
 * Purpose: Launch three threads that each generate 10,000 random characters
 * and display them in a TextArea
 */

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ScottThreeThreads extends Application {

    // Constants
    private static final int PER_THREAD_COUNT = 10_000;       // minimum required
    private static final int BATCH_SIZE = 500;                // UI update batch size

    // Character sets
    private static final char[] LETTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final char[] DIGITS  = "0123456789".toCharArray();
    private static final char[] SYMBOLS = "!@#$%&*".toCharArray();

    // UI
    private final TextArea output = new TextArea();
    private final Label status = new Label("Ready");
    private final Button startBtn = new Button("Start (3 Threads)");

    // Validation counters
    private final AtomicInteger lettersCount = new AtomicInteger(0);
    private final AtomicInteger digitsCount = new AtomicInteger(0);
    private final AtomicInteger symbolsCount = new AtomicInteger(0);

    @Override
    public void start(Stage stage) {
        output.setEditable(false);
        output.setWrapText(true);
        output.setPrefRowCount(18);

        startBtn.setOnAction(e -> runThreeThreads());

        VBox root = new VBox(8,
                new Label("ThreeThreads — letters, digits, symbols (10,000 each)"),
                startBtn,
                new Separator(),
                output,
                new Separator(),
                status
        );
        root.setStyle("-fx-padding: 12; -fx-font-size: 12px;");

        stage.setTitle("Three Threads");
        stage.setScene(new Scene(root, 760, 460));
        stage.show();
    }

    /** Launch all three workers; validate counts when complete. */
    private void runThreeThreads() {
        startBtn.setDisable(true);
        output.clear();
        status.setText("Running…");
        lettersCount.set(0);
        digitsCount.set(0);
        symbolsCount.set(0);

        Instant t0 = Instant.now();

        ExecutorService pool = Executors.newFixedThreadPool(3);
        try {
            Future<?> f1 = pool.submit(makeWorker("letters", LETTERS, lettersCount));
            Future<?> f2 = pool.submit(makeWorker("digits", DIGITS, digitsCount));
            Future<?> f3 = pool.submit(makeWorker("symbols", SYMBOLS, symbolsCount));

            // Wait for all to finish
            List<Future<?>> futures = Arrays.asList(f1, f2, f3);
            for (Future<?> f : futures) f.get(); // propagate exceptions if any

            // Validate
            int L = lettersCount.get();
            int D = digitsCount.get();
            int S = symbolsCount.get();

            boolean ok = (L == PER_THREAD_COUNT) && (D == PER_THREAD_COUNT) && (S == PER_THREAD_COUNT);

            Instant t1 = Instant.now();
            long ms = Duration.between(t0, t1).toMillis();

            Platform.runLater(() -> {
                output.appendText("\n\n--- SUMMARY ---\n");
                output.appendText("letters: " + L + " | digits: " + D + " | symbols: " + S + "\n");
                output.appendText("total chars: " + (L + D + S) + "\n");
                status.setText((ok ? "Validation: OK" : "Validation: FAILED") + " • Elapsed " + ms + " ms");
                startBtn.setDisable(false);
            });

            if (!ok) {
                throw new AssertionError("Counts incorrect: letters=" + L + ", digits=" + D + ", symbols=" + S);
            }
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            status.setText("Interrupted");
            startBtn.setDisable(false);
        } catch (ExecutionException ee) {
            status.setText("Worker error: " + ee.getCause());
            startBtn.setDisable(false);
        } finally {
            pool.shutdownNow();
        }
    }

    /**
     * Create a worker that generates PER_THREAD_COUNT random characters from the given alphabet
     */
    private Callable<Void> makeWorker(String label, char[] alphabet, AtomicInteger counter) {
        return () -> {
            SecureRandom rng = new SecureRandom();
            StringBuilder batch = new StringBuilder(BATCH_SIZE + label.length() + 4);

            for (int i = 1; i <= PER_THREAD_COUNT; i++) {
                char c = alphabet[rng.nextInt(alphabet.length)];
                batch.append(c);
                counter.incrementAndGet();

                // Flush batched text periodically
                if (i % BATCH_SIZE == 0) {
                    final String toAppend = labelPrefix(label) + batch + "\n";
                    Platform.runLater(() -> output.appendText(toAppend));
                    batch.setLength(0);
                }
            }

            // Flush any remainder
            if (batch.length() > 0) {
                final String toAppend = labelPrefix(label) + batch + "\n";
                Platform.runLater(() -> output.appendText(toAppend));
            }
            return null;
        };
    }

    /* Small prefix so each batch shows which thread produced it. */
    private static String labelPrefix(String label) {
        return "[" + label + "] ";
    }

    public static void main(String[] args) { launch(args); }
}
