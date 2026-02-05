package com.school.app.dialog;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class ProgressDialog {

    // نمایش دیالوگ پیشرفت
    public static <T> CompletableFuture<T> showProgress(
            Component parent,
            String message,
            Supplier<T> task) {

        CompletableFuture<T> future = new CompletableFuture<>();

        // ایجاد دیالوگ
        JDialog dialog = new JDialog(
                (Frame) SwingUtilities.getWindowAncestor(parent),
                "در حال پردازش",
                true
        );

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel label = new JLabel(message, JLabel.CENTER);
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);

        panel.add(label, BorderLayout.NORTH);
        panel.add(progressBar, BorderLayout.CENTER);

        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);

        // اجرای تسک در background
        SwingWorker<T, Void> worker = new SwingWorker<>() {
            @Override
            protected T doInBackground() throws Exception {
                return task.get();
            }

            @Override
            protected void done() {
                dialog.dispose();
                try {
                    T result = get();
                    future.complete(result);
                } catch (Exception e) {
                    future.completeExceptionally(e);
                }
            }
        };

        worker.execute();
        dialog.setVisible(true);

        return future;
    }

    // نمایش دیالوگ پیشرفت با امکان لغو
    public static <T> CompletableFuture<T> showProgressWithCancel(
            Component parent,
            String message,
            Supplier<T> task) {

        CompletableFuture<T> future = new CompletableFuture<>();

        JDialog dialog = new JDialog(
                (Frame) SwingUtilities.getWindowAncestor(parent),
                "در حال پردازش",
                true
        );

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel label = new JLabel(message, JLabel.CENTER);
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);

        JButton cancelButton = new JButton("لغو");

        panel.add(label, BorderLayout.NORTH);
        panel.add(progressBar, BorderLayout.CENTER);
        panel.add(cancelButton, BorderLayout.SOUTH);

        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);

        SwingWorker<T, Void> worker = new SwingWorker<>() {
            @Override
            protected T doInBackground() throws Exception {
                return task.get();
            }

            @Override
            protected void done() {
                dialog.dispose();
                try {
                    if (!isCancelled()) {
                        T result = get();
                        future.complete(result);
                    } else {
                        future.cancel(true);
                    }
                } catch (Exception e) {
                    future.completeExceptionally(e);
                }
            }
        };

        cancelButton.addActionListener(e -> {
            worker.cancel(true);
            dialog.dispose();
            future.cancel(true);
        });

        worker.execute();
        dialog.setVisible(true);

        return future;
    }
}