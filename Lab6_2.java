import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

// Власне виключення
class MyArithmeticException extends ArithmeticException {
    public MyArithmeticException(String message) {
        super(message);
    }
}

public class Lab6_2 extends JFrame {

    private JTextField fileField;
    private JTable tableInput;
    private JTable tableOutput;
    private DefaultTableModel modelInput;
    private DefaultTableModel modelOutput;

    public Lab6_2() {
        super("Лабораторна робота 6 (частина 2)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Панель вводу
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Файл матриці:"));
        fileField = new JTextField(20);
        topPanel.add(fileField);

        JButton loadBtn = new JButton("Завантажити");
        JButton processBtn = new JButton("Обробити");

        topPanel.add(loadBtn);
        topPanel.add(processBtn);

        add(topPanel, BorderLayout.NORTH);

        // Таблиці
        modelInput = new DefaultTableModel();
        modelOutput = new DefaultTableModel();

        tableInput = new JTable(modelInput);
        tableOutput = new JTable(modelOutput);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, 
                new JScrollPane(tableInput), new JScrollPane(tableOutput));
        split.setDividerLocation(250);

        add(split, BorderLayout.CENTER);

        // Обробники кнопок
        loadBtn.addActionListener(e -> loadMatrix());
        processBtn.addActionListener(e -> processMatrix());

        setVisible(true);
    }

    // Завантаження матриці з файлу
    private void loadMatrix() {
        try {
            String fileName = fileField.getText();
            if (fileName.isEmpty()) {
                throw new FileNotFoundException("Не вказано ім'я файлу!");
            }

            Scanner sc = new Scanner(new File(fileName));

            ArrayList<int[]> rows = new ArrayList<>();
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] parts = line.trim().split("\\s+");

                int[] row = new int[parts.length];
                for (int i = 0; i < parts.length; i++) {
                    row[i] = Integer.parseInt(parts[i]); // може викликати NumberFormatException
                    if (row[i] == 0) {
                        throw new MyArithmeticException("У матриці знайдено нуль → заборонено!");
                    }
                }
                rows.add(row);
            }

            int n = rows.size();
            modelInput.setRowCount(0);
            modelInput.setColumnCount(rows.get(0).length);

            for (int[] r : rows) {
                modelInput.addRow(java.util.Arrays.stream(r).boxed().toArray());
            }

            JOptionPane.showMessageDialog(this, "Файл успішно завантажено!");

        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "Помилка: файл не знайдено!", 
                    "Помилка", JOptionPane.ERROR_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Помилка формату: не вдалося перетворити число!",
                    "Помилка", JOptionPane.ERROR_MESSAGE);

        } catch (MyArithmeticException ex) {
            JOptionPane.showMessageDialog(this, "Власне виключення: " + ex.getMessage(),
                    "Помилка", JOptionPane.ERROR_MESSAGE);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Невідома помилка: " + ex.getMessage(),
                    "Помилка", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Обробка матриці: пошук мінімуму та циклічний зсув
    private void processMatrix() {
        try {
            int rows = modelInput.getRowCount();
            int cols = modelInput.getColumnCount();

            if (rows == 0) {
                JOptionPane.showMessageDialog(this, "Матриця не завантажена!");
                return;
            }

            int[][] A = new int[rows][cols];

            for (int i = 0; i < rows; i++)
                for (int j = 0; j < cols; j++)
                    A[i][j] = (int) modelInput.getValueAt(i, j);

            // Знаходження мінімального елемента
            int minValue = A[0][0];
            int minCol = 0;

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (A[i][j] < minValue) {
                        minValue = A[i][j];
                        minCol = j;
                    }
                }
            }

            // Зсув стовпців
            int[][] shifted = new int[rows][cols];
            for (int i = 0; i < rows; i++) {
                int idx = 0;
                for (int j = 0; j < cols; j++) {
                    int srcCol = (minCol + j) % cols;
                    shifted[i][idx++] = A[i][srcCol];
                }
            }

            // Виведення у JTable
            modelOutput.setColumnCount(cols);
            modelOutput.setRowCount(0);

            for (int i = 0; i < rows; i++) {
                Object[] row = new Object[cols];
                for (int j = 0; j < cols; j++) {
                    row[j] = shifted[i][j];
                }
                modelOutput.addRow(row);
            }

            JOptionPane.showMessageDialog(this, "Обробку виконано!");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Помилка при обробці: " + ex.getMessage(),
                    "Помилка", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new Lab6_2();
    }
}
