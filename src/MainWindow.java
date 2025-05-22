/**
 * 选课系统GUI主窗口
 * 提供登录界面和三种用户角色(管理员、教师、学生)的功能界面
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import model.*;
import manager.*;
import util.*;
import java.util.List;
import java.util.Date;
import javax.swing.table.DefaultTableModel;

public class MainWindow {
    /**
     * 程序入口
     * author: 信管 1243 202411671333
     */
    public static void main(String[] args) {
        initializeSystem();
        
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setResizable(false);
            loginFrame.setVisible(true);
        });
    }
    
    private static void initializeSystem() {
        if (UserManager.getUsersByRole("admin").isEmpty()) {
            Admin admin = new Admin(
                IDGenerator.generate("ADM"), 
                "admin", 
                "admin123"
            );
            UserManager.addUser(admin);
        }
    }
}

class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    
    public LoginFrame() {
        setTitle("选课系统登录");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        panel.add(new JLabel("用户名:"));
        usernameField = new JTextField();
        panel.add(usernameField);
        
        panel.add(new JLabel("密码:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);
        
        JButton loginButton = new JButton("登录");
        loginButton.addActionListener(this::handleLogin);
        panel.add(loginButton);
        
        JButton exitButton = new JButton("退出");
        exitButton.addActionListener(e -> System.exit(0));
        panel.add(exitButton);
        
        add(panel);
    }
    
    private void handleLogin(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        User user = UserManager.authenticate(username, password);
        if (user == null) {
            JOptionPane.showMessageDialog(this, "用户名或密码错误！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        dispose();
        switch (user.getRole()) {
            case "admin":
                AdminFrame adminFrame = new AdminFrame((Admin) user);
                adminFrame.setResizable(false);
                adminFrame.setVisible(true);
                break;
            case "teacher":
                TeacherFrame teacherFrame = new TeacherFrame((Teacher) user);
                teacherFrame.setResizable(false);
                teacherFrame.setVisible(true);
            case "student":
                StudentFrame studentFrame = new StudentFrame((Student) user);
                studentFrame.setResizable(false);
                studentFrame.setVisible(true);
                break;
        }
    }
}

class AdminFrame extends JFrame {
    private Admin admin;
    private JTable usersTable;
    private JTable coursesTable;
    
    public AdminFrame(Admin admin) {
        this.admin = admin;
        setTitle("管理员界面 - " + admin.getUsername());
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // 用户管理面板
        JPanel usersPanel = new JPanel(new BorderLayout());
        usersTable = new JTable();
        JScrollPane usersScroll = new JScrollPane(usersTable);
        
        JButton addUserBtn = new JButton("添加用户");
        JButton deleteUserBtn = new JButton("删除用户");
        JButton refreshUsersBtn = new JButton("刷新");
        JButton backBtn = new JButton("返回登录");
        
        addUserBtn.addActionListener(e -> showAddUserDialog());
        deleteUserBtn.addActionListener(e -> deleteSelectedUser());
        refreshUsersBtn.addActionListener(e -> refreshUsers());
        backBtn.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
        
        JPanel usersButtonPanel = new JPanel();
        usersButtonPanel.add(addUserBtn);
        usersButtonPanel.add(deleteUserBtn);
        usersButtonPanel.add(refreshUsersBtn);
        usersButtonPanel.add(backBtn);
        
        usersPanel.add(usersScroll, BorderLayout.CENTER);
        usersPanel.add(usersButtonPanel, BorderLayout.SOUTH);
        
        // 课程管理面板
        JPanel coursesPanel = new JPanel(new BorderLayout());
        coursesTable = new JTable();
        JScrollPane coursesScroll = new JScrollPane(coursesTable);
        
        JButton deleteCourseBtn = new JButton("删除课程");
        JButton refreshCoursesBtn = new JButton("刷新");
        
        deleteCourseBtn.addActionListener(e -> deleteSelectedCourse());
        refreshCoursesBtn.addActionListener(e -> refreshCourses());
        
        JPanel coursesButtonPanel = new JPanel();
        coursesButtonPanel.add(deleteCourseBtn);
        coursesButtonPanel.add(refreshCoursesBtn);
        
        coursesPanel.add(coursesScroll, BorderLayout.CENTER);
        coursesPanel.add(coursesButtonPanel, BorderLayout.SOUTH);
        
        tabbedPane.addTab("用户管理", usersPanel);  //子面板
        tabbedPane.addTab("课程管理", coursesPanel);
        
        add(tabbedPane);
        refreshUsers();
        refreshCourses();
    }
    
    private void showAddUserDialog() {
        JDialog dialog = new JDialog(this, "添加用户", true);
        dialog.setSize(300, 250);
        dialog.setLayout(new GridLayout(5, 2, 5, 5));
        
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JComboBox<String> roleCombo = new JComboBox<>(new String[]{"admin", "teacher", "student"});
        
        dialog.add(new JLabel("用户名:"));
        dialog.add(usernameField);
        dialog.add(new JLabel("密码:"));
        dialog.add(passwordField);
        dialog.add(new JLabel("角色:"));
        dialog.add(roleCombo);
        
        JButton confirmBtn = new JButton("确认");
        JButton cancelBtn = new JButton("取消");
        
        confirmBtn.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String role = (String) roleCombo.getSelectedItem();
            
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "用户名和密码不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            User newUser = null;
            switch (role) {
                case "admin":
                    newUser = new Admin(IDGenerator.generate("ADM"), username, password);
                    break;
                case "teacher":
                    newUser = new Teacher(IDGenerator.generate("TCH"), username, password);
                    break;
                case "student":
                    newUser = new Student(IDGenerator.generate("STU"), username, password);
                    break;
            }
            
            UserManager.addUser(newUser);
            JOptionPane.showMessageDialog(dialog, "用户添加成功！");
            dialog.dispose();
            refreshUsers();
        });
        
        cancelBtn.addActionListener(e -> dialog.dispose());
        
        dialog.add(confirmBtn);
        dialog.add(cancelBtn);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    private void deleteSelectedUser() {
        int row = usersTable.getSelectedRow();
        if (row >= 0) {
            String userId = (String) usersTable.getValueAt(row, 0);
            if (userId.equals(admin.getUserId())) {
                JOptionPane.showMessageDialog(this, "不能删除当前登录的管理员账号！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int confirm = JOptionPane.showConfirmDialog(
                this, 
                "确定要删除此用户吗？", 
                "确认删除", 
                JOptionPane.YES_NO_OPTION
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                UserManager.deleteUser(userId);
                refreshUsers();
            }
        }
    }
    
    private void deleteSelectedCourse() {
        int row = coursesTable.getSelectedRow();
        if (row >= 0) {
            String courseId = (String) coursesTable.getValueAt(row, 0);
            
            int confirm = JOptionPane.showConfirmDialog(
                this, 
                "确定要删除此课程吗？", 
                "确认删除", 
                JOptionPane.YES_NO_OPTION
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                CourseManager.deleteCourse(courseId);
                refreshCourses();
            }
        }
    }
    
    private void refreshUsers() {
        List<User> users = UserManager.getAllUsers();
        String[] columns = {"用户ID", "用户名", "角色", "密码"};    //用户的表头
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        
        for (User user : users) {   //每一行用户的信息
            Object[] row = {
                user.getUserId(),
                user.getUsername(),
                user.getRole(),
                user.getPassword()
            };
            model.addRow(row);
        }
        
        usersTable.setModel(model);
    }
    
    private void refreshCourses() {
        List<Course> courses = CourseManager.getAllCourses();
        String[] columns = {"课程ID", "课程名称", "教师", "容量", "已选人数", "开始时间", "结束时间"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        
        for (Course course : courses) {
            User teacher = UserManager.getUser(course.getTeacherId());
            Object[] row = {
                course.getCourseId(),
                course.getCourseName(),
                teacher != null ? teacher.getUsername() : "未知",
                course.getCapacity(),
                course.getEnrolledStudents().size(),
                TimeUtils.dateToStr(course.getBeginTime()),
                    TimeUtils.dateToStr(course.getEndTime())
            };
            model.addRow(row);
        }
        
        coursesTable.setModel(model);
    }
}

class TeacherFrame extends JFrame {
    private Teacher teacher;
    private JTable coursesTable;
    
    public TeacherFrame(Teacher teacher) {
        this.teacher = teacher;
        setTitle("教师界面 - " + teacher.getUsername());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // 创建课程面板
        JPanel createCoursePanel = new JPanel(new GridLayout(6, 2, 5, 5));
        createCoursePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextField courseNameField = new JTextField();
        JTextField capacityField = new JTextField();
        JSpinner beginTimeSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner endTimeSpinner = new JSpinner(new SpinnerDateModel());
        JButton createCourseBtn = new JButton("创建课程");
        
        // 设置时间选择器格式为MM-dd HH:mm
        JSpinner.DateEditor beginEditor = new JSpinner.DateEditor(beginTimeSpinner, "MM-dd HH:mm");
        JSpinner.DateEditor endEditor = new JSpinner.DateEditor(endTimeSpinner, "MM-dd HH:mm");
        beginTimeSpinner.setEditor(beginEditor);
        endTimeSpinner.setEditor(endEditor);
        
        createCoursePanel.add(new JLabel("课程名称:"));
        createCoursePanel.add(courseNameField);
        createCoursePanel.add(new JLabel("课程容量:"));
        createCoursePanel.add(capacityField);
        createCoursePanel.add(new JLabel("开始时间:"));
        createCoursePanel.add(beginTimeSpinner);
        createCoursePanel.add(new JLabel("结束时间:"));
        createCoursePanel.add(endTimeSpinner);
        createCoursePanel.add(new JLabel());
        createCoursePanel.add(createCourseBtn);
        
        createCourseBtn.addActionListener(e -> {
            String name = courseNameField.getText().trim();
            String capacityText = capacityField.getText().trim();
            
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "课程名称不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (capacityText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "课程容量不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                // 确保输入只包含数字
                if (!capacityText.matches("\\d+")) {
                    JOptionPane.showMessageDialog(this, "课程容量必须为正整数！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int capacity = Integer.parseInt(capacityText);
                if (capacity <= 0) {
                    JOptionPane.showMessageDialog(this, "课程容量必须大于0！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                Date beginTime = (Date) beginTimeSpinner.getValue();
                Date endTime = (Date) endTimeSpinner.getValue();
                
                if (endTime.before(beginTime)) {
                    JOptionPane.showMessageDialog(this, "结束时间不能早于开始时间！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                teacher.createCourse(name, capacity, beginTime, endTime);
                JOptionPane.showMessageDialog(this, "课程创建成功！");
                refreshCourses();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "课程容量必须是有效数字！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // 课程列表面板
        JPanel coursesPanel = new JPanel(new BorderLayout());
        coursesTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(coursesTable);
        
        JButton refreshBtn = new JButton("刷新");
        refreshBtn.addActionListener(e -> refreshCourses());
        
        JButton backBtn = new JButton("返回登录");
        backBtn.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshBtn);
        buttonPanel.add(backBtn);
        
        coursesPanel.add(scrollPane, BorderLayout.CENTER);
        coursesPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        tabbedPane.addTab("创建课程", createCoursePanel);
        tabbedPane.addTab("我的课程", coursesPanel);
        
        add(tabbedPane);
        refreshCourses();
    }
    
    private void refreshCourses() {
        List<Course> courses = CourseManager.getCoursesByTeacher(teacher.getUserId());
        String[] columnNames = {"课程ID", "课程名称", "容量", "已选人数", "开始时间", "结束时间"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        
        for (Course course : courses) {
            Object[] row = {
                course.getCourseId(),
                course.getCourseName(),
                course.getCapacity(),
                course.getEnrolledStudents().size(),
                TimeUtils.dateToStr(course.getBeginTime()),
                TimeUtils.dateToStr(course.getEndTime())
            };
            model.addRow(row);
        }
        
        coursesTable.setModel(model);
    }
}

class StudentFrame extends JFrame {
    private Student student;
    private JTable availableCoursesTable;
    private JTable enrolledCoursesTable;
    
    public StudentFrame(Student student) {
        this.student = student;
        setTitle("学生界面 - " + student.getUsername());
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // 可选课程面板
        JPanel availablePanel = new JPanel(new BorderLayout());
        availableCoursesTable = new JTable();
        JScrollPane availableScroll = new JScrollPane(availableCoursesTable);
        
        JButton enrollBtn = new JButton("选课");
        enrollBtn.addActionListener(e -> {
            int row = availableCoursesTable.getSelectedRow();
            if (row >= 0) {
                String courseId = (String) availableCoursesTable.getValueAt(row, 0);
                if (student.enrollCourse(courseId)) {
                    JOptionPane.showMessageDialog(this, "选课成功！");
                    refreshCourses();
                } else {
                    JOptionPane.showMessageDialog(this, "选课失败：时间冲突或容量已满！", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        // 已选课程面板
        JPanel enrolledPanel = new JPanel(new BorderLayout());
        enrolledCoursesTable = new JTable();
        JScrollPane enrolledScroll = new JScrollPane(enrolledCoursesTable);
        
        JButton dropBtn = new JButton("退课");
        dropBtn.addActionListener(e -> {
            int row = enrolledCoursesTable.getSelectedRow();
            if (row >= 0) {
                String courseId = (String) enrolledCoursesTable.getValueAt(row, 0);
                if (student.dropCourse(courseId)) {
                    JOptionPane.showMessageDialog(this, "退课成功！");
                    refreshCourses();
                } else {
                    JOptionPane.showMessageDialog(this, "退课失败！", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        JButton refreshBtn = new JButton("刷新");
        refreshBtn.addActionListener(e -> refreshCourses());
        
        JButton backBtn = new JButton("返回登录");
        backBtn.addActionListener(e -> {    //lambda表达式临时定义一个ActionListener来返回登录界面
            dispose();
            new LoginFrame().setVisible(true);
        });
        
        JPanel enbuttonPanel = new JPanel();    //已选课的按钮面板
        enbuttonPanel.add(dropBtn);
        
        JPanel avbuttonPanel = new JPanel();    //可选课的按钮面板
        avbuttonPanel.add(enrollBtn);    
        JPanel exitPanel = new JPanel();    //退出按钮面板
        exitPanel.add(refreshBtn);
        exitPanel.add(backBtn);
        

        availablePanel.add(availableScroll, BorderLayout.CENTER);
        availablePanel.add(avbuttonPanel, BorderLayout.SOUTH);    //将按钮面板添加到可选课程面板的下方

        enrolledPanel.add(enrolledScroll, BorderLayout.CENTER);
        enrolledPanel.add(enbuttonPanel, BorderLayout.SOUTH);    //将按钮面板添加到已选课程面板的下方
        
        tabbedPane.addTab("可选课程", availablePanel);
        tabbedPane.addTab("已选课程", enrolledPanel);
        tabbedPane.addTab("退出", exitPanel);    //将退出按钮面板添加到tabbedPane的最后一个tab
        // tabbedPane.add(buttonPanel, BorderLayout.SOUTH);
        
        add(tabbedPane);
        refreshCourses();
    }
    
    private void refreshCourses() {
        // 刷新可选课程表
        List<Course> availableCourses = CourseManager.getAvailableCourses(student.getUserId());
        String[] availableColumns = {"课程ID", "课程名称", "教师", "容量", "已选人数", "开始时间", "结束时间"};
        DefaultTableModel availableModel = new DefaultTableModel(availableColumns, 0);
        
        for (Course course : availableCourses) {
            User teacher = UserManager.getUser(course.getTeacherId());
            Object[] row = {
                course.getCourseId(),
                course.getCourseName(),
                teacher != null ? teacher.getUsername() : "未知",
                course.getCapacity(),
                course.getEnrolledStudents().size(),
                TimeUtils.dateToStr(course.getBeginTime()),
                TimeUtils.dateToStr(course.getEndTime())
            };
            availableModel.addRow(row);
        }
        availableCoursesTable.setModel(availableModel);
        
        // 刷新已选课程表
        List<Course> enrolledCourses = CourseManager.getEnrolledCourses(student.getUserId());
        String[] enrolledColumns = {"课程ID", "课程名称", "教师", "开始时间", "结束时间"};
        DefaultTableModel enrolledModel = new DefaultTableModel(enrolledColumns, 0);
        
        for (Course course : enrolledCourses) {
            User teacher = UserManager.getUser(course.getTeacherId());
            Object[] row = {
                course.getCourseId(),
                course.getCourseName(),
                teacher != null ? teacher.getUsername() : "未知",
                TimeUtils.dateToStr(course.getBeginTime()),
                TimeUtils.dateToStr(course.getEndTime())
            };
            enrolledModel.addRow(row);
        }
        enrolledCoursesTable.setModel(enrolledModel);
    }
}
