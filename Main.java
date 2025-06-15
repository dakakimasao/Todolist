import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class Main extends JFrame {
    private DefaultListModel<String> listModel; // 할 일(String)을 담는 리스트 모델 (추가/삭제 용이)
    private JList<String> todoList; //실제로 화면에 보여지는 목록 UI 컴포넌트
    private JTextField taskField; //사용자가 할 일을 입력하는 필드
    private static final String FILE_NAME = "todo.txt";

    public Main() { // gui 세팅
        setTitle("To-Do 리스트");
        setSize(400, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 화면 중앙에 배치

        listModel = new DefaultListModel<>();
        todoList = new JList<>(listModel);
        loadTasks(); // 파일에서 로드
/*        리스트 모델과 JList를 연결함
        loadTasks()로 프로그램 실행 시 기존 할 일 불러옴*/



        JScrollPane scrollPane = new JScrollPane(todoList); //JList가
        // 내용이 많아지면 스크롤 가능하도록 함

        taskField = new JTextField();
        JButton addButton = new JButton("추가");
        JButton deleteButton = new JButton("삭제");
        JButton saveButton = new JButton("저장");
        //할 일을 입력할 텍스트 필드와 버튼 3개

        addButton.addActionListener(e -> addTask());
        deleteButton.addActionListener(e -> deleteTask());
        saveButton.addActionListener(e -> saveTasks());
        //버튼 클릭 시 각각 addTask(), deleteTask(), saveTasks() 메서드 실행

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(taskField, BorderLayout.CENTER);
        inputPanel.add(addButton, BorderLayout.EAST);
        //버튼 위치
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(deleteButton);
        buttonPanel.add(saveButton);
        //삭제/저장 버튼을 담는 패널
        add(scrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);
        //배치
    }

    private void addTask() { // 할일 추가
        String task = taskField.getText().trim();
        if (!task.isEmpty()) {
            listModel.addElement(task);
            taskField.setText("");
        }
    }

    private void deleteTask() { // 삭제
        int selectedIndex = todoList.getSelectedIndex();
        if (selectedIndex != -1) {
            listModel.remove(selectedIndex);
        }
    }

    private void saveTasks() { // 투두에 저장 및 메시지 팝업
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (int i = 0; i < listModel.size(); i++) {
                writer.write(listModel.get(i));
                writer.newLine();
            }
            JOptionPane.showMessageDialog(this, "저장 완료!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "저장 실패: " + e.getMessage());
        }
    }

    private void loadTasks() { //시작할 때 기존 저장된 파일이 있다면 목록을 불러옴
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                listModel.addElement(line);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "불러오기 실패: " + e.getMessage());
        }
    }

    public static void main(String[] args) { //프로그램 시작 메인.
        SwingUtilities.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }
}
