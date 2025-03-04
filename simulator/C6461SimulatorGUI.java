import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class C6461SimulatorGUI extends JFrame {

    public C6461SimulatorGUI() {
        setTitle("CSCI 6461 Machine Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(217, 232, 245));

        JLabel title = new JLabel("CSCI 6461 Machine Simulator", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);
//TOP
        JPanel topPanel = new JPanel(new GridLayout(1, 4, 20, 20));
        topPanel.setBackground(new Color(217, 232, 245));

        topPanel.add(createRegisterPanel("GPR", new String[]{"0", "1", "2", "3"}, true));
        topPanel.add(createRegisterPanel("IXR", new String[]{"1", "2", "3"}, true));
        topPanel.add(createRegisterPanel("Registers", new String[]{"PC", "MAR", "MBR", "IR"}, true));
        topPanel.add(createCachePrinterPanel("Cache Content", 100));

        mainPanel.add(topPanel);

//MID
        JPanel middlePanel = new JPanel(new GridLayout(1, 2, 20, 20));
        middlePanel.setBackground(new Color(217, 232, 245));

        middlePanel.add(createConversionPanel());
        middlePanel.add(createFlagPanel());

        mainPanel.add(middlePanel);


//control
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        String[] buttons = {"Load", "Load+", "Store", "Store+", "Run", "Step", "Halt", "IPL"};
        for (String btn : buttons) {
            JButton button = new JButton(btn);
            button.setBackground(new Color(0, 123, 255));
            button.setForeground(Color.BLUE);
            button.addActionListener(new ButtonClickListener(btn));
            controlPanel.add(button);
        }
        mainPanel.add(controlPanel);

//bottom
        JPanel bottomPanel = new JPanel(new GridLayout(1, 3, 20, 20));
        bottomPanel.setBackground(new Color(217, 232, 245));

        bottomPanel.add(createFileInputPanel());
        bottomPanel.add(createCachePrinterPanel("Printer", 100));
        bottomPanel.add(createConsolePanel());

        mainPanel.add(bottomPanel);

        add(mainPanel);
        setVisible(true);
    }

    private JPanel createRegisterPanel(String title, String[] labels, boolean hasCheckbox) {
        JPanel panel = createStyledPanel(title);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        for (String label : labels) {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
            row.add(new JLabel(label));
            JTextField tf = new JTextField(10);
            tf.setPreferredSize(new Dimension(120, 25));
            row.add(tf);
            if (hasCheckbox && !label.equals("IR")) row.add(new JCheckBox());
            panel.add(row);
        }
        return panel;
    }

    private JPanel createCachePrinterPanel(String title, int height) {
        JPanel panel = createStyledPanel(title);
        JTextArea ta = new JTextArea();
        ta.setPreferredSize(new Dimension(280, height));
        panel.add(new JScrollPane(ta));
        return panel;
    }

    private JPanel createConversionPanel() {
        JPanel panel = createStyledPanel("Input Conversion");
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(createFormRow("Binary", 120));
        panel.add(createFormRow("Octal", 120));
        return panel;
    }

    private JPanel createFlagPanel() {
        JPanel panel = createStyledPanel("");
        panel.add(createFlagRow("CC", "OUDE"));
        panel.add(createFlagRow("MFR", "MOTR"));
        return panel;
    }

    private JPanel createFlagRow(String label, String subtext) {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.add(new JLabel(label));
        JTextField tf = new JTextField(5);
        tf.setPreferredSize(new Dimension(60, 25));
        row.add(tf);
        panel.add(row, BorderLayout.NORTH);
        panel.add(new JLabel(subtext, JLabel.CENTER), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createFileInputPanel() {
        JPanel panel = createStyledPanel("Program Loader");
        panel.add(createFormRow("Program File", 200));
        return panel;
    }

    private JPanel createConsolePanel() {
        JPanel panel = createStyledPanel("Console Output");
        panel.add(createFormRow("", 200));
        return panel;
    }

    private JPanel createFormRow(String label, int width) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel(label));
        JTextField tf = new JTextField(width);
        tf.setPreferredSize(new Dimension(width, 25));
        panel.add(tf);
        return panel;
    }

    private JPanel createStyledPanel(String title) {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        if (!title.isEmpty()) {
            panel.setLayout(new BorderLayout());
            JLabel titleLabel = new JLabel(title, JLabel.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
            panel.add(titleLabel, BorderLayout.NORTH);
        }
        return panel;
    }
    private static class ButtonClickListener implements ActionListener {
        private final String buttonName;

        public ButtonClickListener(String buttonName) {
            this.buttonName = buttonName;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(null, buttonName + " clicked!", "Button Click", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new C6461SimulatorGUI();
    }
}