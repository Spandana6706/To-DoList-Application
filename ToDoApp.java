import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class ToDoApp {
    private JFrame frame;
    private JPanel taskListPanel;
    private JTextField taskInputField;
    private JButton addTaskButton;
    private ArrayList<Task> tasks;

    public ToDoApp() {
        tasks = new ArrayList<>();
        frame = new JFrame("To-Do List");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Title label
        JLabel titleLabel = new JLabel("My To-Do List");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        frame.add(titleLabel, BorderLayout.NORTH);

        // Task list panel with scrollable area
        taskListPanel = new JPanel();
        taskListPanel.setLayout(new BoxLayout(taskListPanel, BoxLayout.Y_AXIS)); // Stack tasks vertically
        JScrollPane scrollPane = new JScrollPane(taskListPanel);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Input and add button panel
        JPanel inputPanel = new JPanel(new BorderLayout());
        taskInputField = new JTextField();
        taskInputField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        taskInputField.setBackground(Color.decode("#f7f7f7")); // Pastel input field
        taskInputField.setForeground(Color.decode("#333333"));
        inputPanel.add(taskInputField, BorderLayout.CENTER);

        addTaskButton = createButton("Add Task", "#a5d6a7"); // Pastel green
        inputPanel.add(addTaskButton, BorderLayout.EAST);

        frame.add(inputPanel, BorderLayout.SOUTH);

        // Add action listener for the "Add Task" button
        addTaskButton.addActionListener(e -> { 
            ((JButton) e.getSource()).setText("Add Task");
            addTask();
        }); 

        // Add hover effect to the button
        addTaskButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                addTaskButton.setBackground(Color.decode("#c8e6c9")); // Lighter pastel green on hover
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                addTaskButton.setBackground(Color.decode("#a5d6a7")); // Original pastel green
            }
        });

        loadTasks();
        frame.setVisible(true);
    }

    private JButton createButton(String text, String bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.PLAIN, 14));
        button.setBackground(Color.decode(bgColor));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }

    private void addTask() {
        String taskTitle = taskInputField.getText().trim();
        if (!taskTitle.isEmpty()) {
            Task newTask = new Task(taskTitle);
            tasks.add(newTask);
            addTaskToUI(newTask);
            taskInputField.setText("");
            saveTasks();
        }
    }

    private void addTaskToUI(Task task) {
        JPanel taskPanel = new JPanel(new BorderLayout()); // Use BorderLayout for easy positioning
        taskPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        taskPanel.setPreferredSize(new Dimension(600, 50)); // Make the task occupy full width but minimal height
        taskPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60)); // Ensure it takes up full width horizontally
    
        taskPanel.setBackground(task.isCompleted() ? new Color(204, 255, 204) : new Color(255, 204, 204)); // Pastel colors
    
        // Task Label (leftmost part)
        JLabel taskLabel = new JLabel(task.getTitle());
        taskLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        taskPanel.add(taskLabel, BorderLayout.CENTER); // Add the task label to the center
    
        // Panel for buttons (to align them right)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Right alignment for buttons
        buttonPanel.setOpaque(false); // Make button panel background transparent, so it blends with taskPanel
        taskPanel.add(buttonPanel, BorderLayout.EAST);
    
        // Done button - change color to green (#25d366)
        JButton doneButton = createButton("Done", "#25d366"); // Green color
        doneButton.addActionListener(e -> {
            ((JButton) e.getSource()).setText("Done");
            markTaskAsDone(task, taskPanel);
        });
        buttonPanel.add(doneButton);
    
        // Delete button - change color to blue (#4bbef3)
        JButton deleteButton = createButton("Delete", "#4bbef3"); // Blue color
        deleteButton.addActionListener(e -> {
            ((JButton) e.getSource()).setText("Delete");
            deleteTask(task, taskPanel);
        });
        buttonPanel.add(deleteButton);
    
        // Add task panel to the main list
        taskListPanel.add(taskPanel);
        taskListPanel.revalidate();
        taskListPanel.repaint();
    }
    

    private void markTaskAsDone(Task task, JPanel taskPanel) {
        task.toggleCompletion();
        taskPanel.setBackground(task.isCompleted() ? new Color(204, 255, 204) : new Color(255, 204, 204)); // Update color
        saveTasks();
    }

    private void deleteTask(Task task, JPanel taskPanel) {
        tasks.remove(task);
        taskListPanel.remove(taskPanel);
        taskListPanel.revalidate();
        taskListPanel.repaint();
        saveTasks();
    }

    @SuppressWarnings("unchecked")
    private void loadTasks() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("tasks.dat"))) {
            tasks = (ArrayList<Task>) ois.readObject();
            for (Task task : tasks) {
                addTaskToUI(task);
            }
        } catch (IOException | ClassNotFoundException e) {
            tasks = new ArrayList<>();
        }
    }

    private void saveTasks() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("tasks.dat"))) {
            oos.writeObject(tasks);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ToDoApp::new);
    }
}







 