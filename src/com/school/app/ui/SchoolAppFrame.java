package com.school.app.ui;

import com.school.app.dialog.MessageDialog;
import com.school.app.dialog.StudentDialog;
import com.school.app.dialog.AboutDialog;
import com.school.app.dto.StudentRequest;
import com.school.app.model.Response;
import com.school.app.model.Student;
import com.school.app.service.StudentService;
import com.school.app.service.StudentServiceImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SchoolAppFrame extends JFrame {

    private final StudentService service;

    private final DefaultTableModel tableModel;
    private final JTable table;

    private final JTextField nationalCodeField = new JTextField();
    private final JTextField firstNameField = new JTextField();
    private final JTextField lastNameField = new JTextField();
    private final JTextField classNameField = new JTextField();

    public SchoolAppFrame(StudentService service) {
        this.service = service;

        if (service instanceof StudentServiceImpl) {
            ((StudentServiceImpl) service).setParentComponent(this);
        }

        setTitle("مدیریت دانش‌آموزان - برنامه مدرسه");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        createMenuBar();

        tableModel = new DefaultTableModel(
                new Object[]{"شناسه", "کد ملی", "نام", "نام خانوادگی", "کلاس"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(25);

        JScrollPane scroll = new JScrollPane(table);

        JPanel form = buildFormPanel();
        JPanel buttons = buildButtonsPanel();

        setLayout(new BorderLayout(10, 10));
        add(scroll, BorderLayout.CENTER);
        add(form, BorderLayout.EAST);
        add(buttons, BorderLayout.SOUTH);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fillFormFromSelectedRow();
            }
        });

        refreshTable();
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("فایل");
        JMenuItem refreshItem = new JMenuItem("بارگذاری مجدد");
        JMenuItem exitItem = new JMenuItem("خروج");

        refreshItem.addActionListener(e -> refreshTable());
        exitItem.addActionListener(e -> System.exit(0));

        fileMenu.add(refreshItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        JMenu studentMenu = new JMenu("دانش‌آموزان");
        JMenuItem addItem = new JMenuItem("دانش‌آموز جدید");
        JMenuItem editItem = new JMenuItem("ویرایش انتخاب شده");
        JMenuItem deleteItem = new JMenuItem("حذف انتخاب شده");
        JMenuItem detailsItem = new JMenuItem("نمایش جزئیات");

        addItem.addActionListener(e -> openAddStudentDialog());
        editItem.addActionListener(e -> performUpdate());
        deleteItem.addActionListener(e -> performDelete());
        detailsItem.addActionListener(e -> showStudentDetails());

        studentMenu.add(addItem);
        studentMenu.add(editItem);
        studentMenu.add(deleteItem);
        studentMenu.add(detailsItem);

        JMenu helpMenu = new JMenu("کمک");
        JMenuItem aboutItem = new JMenuItem("درباره برنامه");

        aboutItem.addActionListener(e -> AboutDialog.show(this));

        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(studentMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    private JPanel buildFormPanel() {
        JPanel p = new JPanel(new GridLayout(8, 1, 8, 8));
        p.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "فرم دانش‌آموز"
        ));

        nationalCodeField.setToolTipText("کد ملی ۱۰ رقمی");
        firstNameField.setToolTipText("نام دانش‌آموز");
        lastNameField.setToolTipText("نام خانوادگی دانش‌آموز");
        classNameField.setToolTipText("مثال: ۱۰-الف");

        p.add(new JLabel("کد ملی:"));
        p.add(nationalCodeField);

        p.add(new JLabel("نام:"));
        p.add(firstNameField);

        p.add(new JLabel("نام خانوادگی:"));
        p.add(lastNameField);

        p.add(new JLabel("کلاس (مثال: ۱۰-الف):"));
        p.add(classNameField);

        return p;
    }

    private JPanel buildButtonsPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton refreshBtn = new JButton(" بارگذاری مجدد");
        JButton addBtn = new JButton(" افزودن");
        JButton updateBtn = new JButton(" ویرایش");
        JButton deleteBtn = new JButton("️ حذف");
        JButton clearBtn = new JButton(" پاک کردن فرم");
        JButton detailsBtn = new JButton(" جزئیات");

        refreshBtn.setToolTipText("بارگذاری مجدد لیست دانش‌آموزان");
        addBtn.setToolTipText("افزودن دانش‌آموز جدید");
        updateBtn.setToolTipText("ویرایش دانش‌آموز انتخاب شده");
        deleteBtn.setToolTipText("حذف دانش‌آموز انتخاب شده");
        clearBtn.setToolTipText("پاک کردن فرم");
        detailsBtn.setToolTipText("نمایش جزئیات دانش‌آموز انتخاب شده");

        refreshBtn.addActionListener(e -> refreshTable());
        clearBtn.addActionListener(e -> clearForm());
        addBtn.addActionListener(e -> performAdd());
        updateBtn.addActionListener(e -> performUpdate());
        deleteBtn.addActionListener(e -> performDelete());
        detailsBtn.addActionListener(e -> showStudentDetails());

        p.add(refreshBtn);
        p.add(addBtn);
        p.add(updateBtn);
        p.add(deleteBtn);
        p.add(detailsBtn);
        p.add(clearBtn);

        return p;
    }

    private void refreshTable() {
        try {
            Response<List<Student>> response = service.list();

            if (response.getStatus() >= 300) {
                MessageDialog.showError(this,
                        "خطا در دریافت لیست دانش‌آموزان:\n" + response.getMessage());
                return;
            }

            tableModel.setRowCount(0);
            List<Student> students = response.getData();

            if (students.isEmpty()) {
                MessageDialog.showInfo(this, "هیچ دانش‌آموزی ثبت نشده است.");
            } else {
                for (Student s : students) {
                    tableModel.addRow(new Object[]{
                            s.getId(),
                            s.getNationalCode(),
                            s.getFirstName(),
                            s.getLastName(),
                            s.getClassName()
                    });
                }
            }
        } catch (Exception e) {
            MessageDialog.showError(this, "خطا در بارگذاری داده‌ها: " + e.getMessage());
        }
    }

    private void fillFormFromSelectedRow() {
        int row = table.getSelectedRow();
        if (row < 0) return;

        nationalCodeField.setText(String.valueOf(tableModel.getValueAt(row, 1)));
        firstNameField.setText(String.valueOf(tableModel.getValueAt(row, 2)));
        lastNameField.setText(String.valueOf(tableModel.getValueAt(row, 3)));
        classNameField.setText(String.valueOf(tableModel.getValueAt(row, 4)));
    }

    private Student readStudentFromForm() {
        String nc = nationalCodeField.getText().trim();
        String fn = firstNameField.getText().trim();
        String ln = lastNameField.getText().trim();
        String cn = classNameField.getText().trim();

        return new Student(null, nc, fn, ln, cn);
    }

    private Long getSelectedId() {
        int row = table.getSelectedRow();
        if (row < 0) return null;
        Object v = tableModel.getValueAt(row, 0);
        return (v instanceof Number) ? ((Number) v).longValue() : Long.parseLong(String.valueOf(v));
    }

    private void clearForm() {
        nationalCodeField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        classNameField.setText("");
        table.clearSelection();
    }

    private void performAdd() {
        try {
            Student student = readStudentFromForm();
            if (student == null) return;

            Response<Student> response = service.add(student);
            showResponse(response);

            if (response.getStatus() < 300) {
                clearForm();
                refreshTable();
            }
        } catch (Exception e) {
            MessageDialog.showError(this, "خطا در افزودن دانش‌آموز: " + e.getMessage());
        }
    }

    private void performUpdate() {
        Long id = getSelectedId();
        if (id == null) {
            MessageDialog.showWarning(this, "لطفاً ابتدا یک دانش‌آموز را از جدول انتخاب کنید!");
            return;
        }

        try {
            Student student = readStudentFromForm();
            if (student == null) return;

            Response<Student> response = service.update(id, student);
            showResponse(response);

            if (response.getStatus() < 300) {
                clearForm();
                refreshTable();
            }
        } catch (Exception e) {
            MessageDialog.showError(this, "خطا در ویرایش دانش‌آموز: " + e.getMessage());
        }
    }

    private void performDelete() {
        Long id = getSelectedId();
        if (id == null) {
            MessageDialog.showWarning(this, "لطفاً ابتدا یک دانش‌آموز را از جدول انتخاب کنید!");
            return;
        }

        boolean confirm = MessageDialog.showConfirm(
                this,
                "تایید حذف",
                "آیا از حذف دانش‌آموز با شناسه " + id + " اطمینان دارید؟\nاین عمل قابل برگشت نیست."
        );

        if (!confirm) return;

        try {
            Response<Boolean> response = service.delete(id);
            showResponse(response);

            if (response.getStatus() < 300) {
                clearForm();
                refreshTable();
            }
        } catch (Exception e) {
            MessageDialog.showError(this, "خطا در حذف دانش‌آموز: " + e.getMessage());
        }
    }

    private void showStudentDetails() {
        Long id = getSelectedId();
        if (id == null) {
            MessageDialog.showWarning(this, "لطفاً ابتدا یک دانش‌آموز را از جدول انتخاب کنید!");
            return;
        }

        try {
            Response<Student> response = service.findById(id);
            if (response.getStatus() < 300) {
                Student student = response.getData();
                StudentDialog.showDetailsDialog(this, student);
            } else {
                MessageDialog.showError(this, response.getMessage());
            }
        } catch (Exception e) {
            MessageDialog.showError(this, "خطا در دریافت اطلاعات دانش‌آموز: " + e.getMessage());
        }
    }

    private void openAddStudentDialog() {
        StudentRequest request = StudentDialog.showCreateDialog(this);
        if (request != null) {
            Student student = new Student(
                    null,
                    request.getNationalCode(),
                    request.getFirstName(),
                    request.getLastName(),
                    request.getClassName()
            );

            Response<Student> response = service.add(student);
            showResponse(response);

            if (response.getStatus() < 300) {
                refreshTable();
            }
        }
    }

    private void showResponse(Response<?> response) {
        if (response.getStatus() < 300) {
            MessageDialog.showSuccess(this, response.getMessage());
        } else {
            MessageDialog.showError(this,
                    "خطا:\n" + response.getMessage() +
                            "\nکد وضعیت: " + response.getStatus());
        }
    }
}