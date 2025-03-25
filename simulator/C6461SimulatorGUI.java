import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Paths;

public class C6461SimulatorGUI extends JFrame {
    private C6461SimulatorMachine machine;
    private JTextField[] gprFields;
    private JTextField[] ixrFields;
    private JTextField[] registerFields;
    private JTextField binaryField;
    private JTextField octalField;
    private JTextField decimalField;
    private JTextField ccField;
    private JTextField mfrField;
    private boolean running;
    private String outputFormat = "decimal";
    private JTextField cacheHitsField;
    private JTextField cacheMissesField;
    private JTextField cacheHitRateField;
    private JTextField charField; // Add a new field for character input
    private JTextArea consoleTextArea; // Add a field for the console output text area
    private JTextArea cacheContentTextArea; // Add a field for the cache content text area

    public C6461SimulatorGUI() {
        machine = new C6461SimulatorMachine();
        machine.connectWorld(this);
        setTitle("CSCI 6461 Machine Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(217, 232, 245));

        JLabel title = new JLabel("CSCI 6461 Machine Simulator", JLabel.CENTER);
        title.setFont(new Font("Monospaced", Font.BOLD, 24));
        add(title, BorderLayout.NORTH);

        // Display Format Selection
        JPanel formatPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 2));
        formatPanel.setBackground(new Color(217, 232, 245));
        JLabel formatLabel = new JLabel("Display Format:");
        formatPanel.add(formatLabel);

        JRadioButton decimalButton = new JRadioButton("Decimal");
        decimalButton.setSelected(true);
        decimalButton.addActionListener(e -> setOutputFormat("decimal"));
        formatPanel.add(decimalButton);

        JRadioButton octalButton = new JRadioButton("Octal");
        octalButton.addActionListener(e -> setOutputFormat("octal"));
        formatPanel.add(octalButton);

        JRadioButton binaryButton = new JRadioButton("Binary");
        binaryButton.addActionListener(e -> setOutputFormat("binary"));
        formatPanel.add(binaryButton);

        JRadioButton hexButton = new JRadioButton("Hexadecimal");
        hexButton.addActionListener(e -> setOutputFormat("hex"));
        formatPanel.add(hexButton);

        ButtonGroup formatGroup = new ButtonGroup();
        formatGroup.add(decimalButton);
        formatGroup.add(octalButton);
        formatGroup.add(binaryButton);
        formatGroup.add(hexButton);

        mainPanel.add(formatPanel);

        // TOP
        JPanel topPanel = new JPanel(new GridLayout(1, 5, 20, 20));
        topPanel.setBackground(new Color(217, 232, 245));

        gprFields = new JTextField[4];
        topPanel.add(createRegisterPanel("GPR", new String[]{"GPR0", "GPR1", "GPR2", "GPR3"}, gprFields, true));

        ixrFields = new JTextField[3];
        topPanel.add(createRegisterPanel("IXR", new String[]{"IXR1", "IXR2", "IXR3"}, ixrFields, true));

        registerFields = new JTextField[4];
        topPanel.add(createRegisterPanel("Registers", new String[]{"PC", "MAR", "MBR", "IR"}, registerFields, true));

        topPanel.add(createCacheStatsPanel());

        topPanel.add(createCachePrinterPanel("Cache Content", 100));

        mainPanel.add(topPanel);

        // MID
        JPanel middlePanel = new JPanel(new GridLayout(1, 2, 20, 20));
        middlePanel.setBackground(new Color(217, 232, 245));

        middlePanel.add(createInputPanel());
        middlePanel.add(createFlagPanel());

        mainPanel.add(middlePanel);

        // Control
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

        // Bottom
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 20, 20));
        bottomPanel.setBackground(new Color(217, 232, 245));

        bottomPanel.add(createPrinterPanel("Printer", 100));
        bottomPanel.add(createConsolePanel());

        mainPanel.add(bottomPanel);

        add(mainPanel);
        setVisible(true);

        updateGUIFromMachine();
    }

    private void setOutputFormat(String format) {
        this.outputFormat = format;
        updateGUIFromMachine();
    }

    private JPanel createRegisterPanel(String title, String[] labels, JTextField[] fields, boolean hasButton) {
        JPanel panel = createStyledPanel(title);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        for (int i = 0; i < labels.length; i++) {
            JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
            JLabel label = new JLabel(labels[i]);
            label.setPreferredSize(new Dimension(50, 25));
            row.add(label);
            fields[i] = new JTextField(16); // Increased width to fit 16 binary digits
            fields[i].setPreferredSize(new Dimension(160, 25));
            fields[i].setEditable(false);
            fields[i].setBackground(Color.LIGHT_GRAY);
            row.add(fields[i]);
            if (hasButton && !labels[i].equals("IR")) {
                JButton button = new JButton("Set");
                button.addActionListener(new RegisterButtonClickListener(fields[i], labels[i]));
                row.add(button);
            }
            panel.add(row);
        }
        return panel;
    }

    private void updateGUIFromMachine() {
        for (int i = 0; i < gprFields.length; i++) {
            gprFields[i].setText(formatValue(machine.gprs[i], 16));
        }
        for (int i = 0; i < ixrFields.length; i++) {
            ixrFields[i].setText(formatValue(machine.idxs[i], 16));
        }
        registerFields[0].setText(formatValue(machine.pc, 12));
        registerFields[1].setText(formatValue(machine.mar, 12));
        registerFields[2].setText(formatValue(machine.mbr, 16));
        registerFields[3].setText(formatValue(machine.ir, 16));
        ccField.setText(Integer.toBinaryString((1 << 4) | machine.cc).substring(1));
        mfrField.setText(Integer.toBinaryString((1 << 4) | machine.mfr).substring(1));
        cacheHitsField.setText(String.valueOf(machine.memory.getCacheHits()));
        cacheMissesField.setText(String.valueOf(machine.memory.getCacheMisses()));
        cacheHitRateField.setText(String.format("%.2f%%", machine.memory.getCacheHitRate() * 100));
        updateCacheContent(); // Call the new method to update the cache content
    }

    private String formatValue(short value, int bits) {
        switch (outputFormat) {
            case "binary":
                return String.format("%" + bits + "s", Integer.toBinaryString(value & 0xFFFF)).replace(' ', '0');
            case "octal":
                return String.format("%" + (bits / 3) + "s", Integer.toOctalString(value & 0xFFFF)).replace(' ', '0');
            case "hex":
                return String.format("%" + (bits / 4) + "s", Integer.toHexString(value & 0xFFFF)).replace(' ', '0');
            default:
                return String.format("%" + (bits / 4) + "d", value & 0xFFFF);
        }
    }

    private JPanel createCachePrinterPanel(String title, int height) {
        JPanel panel = createStyledPanel(title);
        cacheContentTextArea = new JTextArea(); // Initialize the cache content text area
        cacheContentTextArea.setPreferredSize(new Dimension(280, height));
        cacheContentTextArea.setEditable(false);
        panel.add(new JScrollPane(cacheContentTextArea));
        return panel;
    }

    private JPanel createPrinterPanel(String title, int height) {
        JPanel panel = createStyledPanel(title);
        JTextArea printerTextArea = new JTextArea(); // Initialize the printer text area
        printerTextArea.setPreferredSize(new Dimension(280, height));
        printerTextArea.setEditable(false);
        panel.add(new JScrollPane(printerTextArea));
        return panel;
    }

    private void updateCacheContent() {
        short[][] cacheLines = machine.memory.getCacheLines();
        StringBuilder content = new StringBuilder();
        for (short[] line : cacheLines) {
            for (short value : line) {
                content.append(formatValue(value, 16)).append(" ");
            }
            content.append("\n");
        }
        cacheContentTextArea.setText(content.toString());
    }

    private JPanel createInputPanel() {
        JPanel panel = createStyledPanel("Input");
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(createFormRow("Binary", 120, binaryField = new JTextField(10)));
        panel.add(createFormRow("Octal", 120, octalField = new JTextField(10)));
        panel.add(createFormRow("Decimal", 120, decimalField = new JTextField(10)));
        panel.add(createFormRow("Char", 120, charField = new JTextField(1))); // Add char input field

        KeyAdapter keyAdapter = new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                JTextField source = (JTextField) e.getSource();
                String text = source.getText();
                try {
                    int value;
                    if (text.isEmpty()) {
                        value = 0;
                    } else if (source == binaryField) {
                        value = Integer.parseInt(text, 2);
                    } else if (source == octalField) {
                        value = Integer.parseInt(text, 8);
                    } else {
                        value = Integer.parseInt(text);
                    }
                    binaryField.setText(Integer.toBinaryString(value));
                    octalField.setText(Integer.toOctalString(value));
                    decimalField.setText(Integer.toString(value));
                } catch (NumberFormatException ex) {
                    // Ignore invalid input
                }
            }
        };

        binaryField.addKeyListener(keyAdapter);
        octalField.addKeyListener(keyAdapter);
        decimalField.addKeyListener(keyAdapter);

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
        tf.setEditable(false);
        tf.setBackground(Color.LIGHT_GRAY);
        row.add(tf);
        if (label.equals("CC")) {
            ccField = tf;
        } else if (label.equals("MFR")) {
            mfrField = tf;
        }
        panel.add(row, BorderLayout.NORTH);
        panel.add(new JLabel(subtext, JLabel.CENTER), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createConsolePanel() {
        JPanel panel = createStyledPanel("Console Output");
        consoleTextArea = new JTextArea(); // Initialize the console output text area
        consoleTextArea.setEditable(false);
        consoleTextArea.setLineWrap(true); // Enable line wrapping
        consoleTextArea.setWrapStyleWord(true); // Wrap at word boundaries

        JScrollPane scrollPane = new JScrollPane(consoleTextArea); // Wrap in JScrollPane
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // Always show vertical scrollbar
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); // Show horizontal scrollbar as needed
        scrollPane.setPreferredSize(new Dimension(280, 100)); // Set preferred size for the scroll pane

        panel.add(scrollPane);
        return panel;
    }

    private JPanel createFormRow(String label, int width, JTextField textField) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lbl = new JLabel(label);
        lbl.setPreferredSize(new Dimension(50, 25));
        panel.add(lbl);
        textField.setPreferredSize(new Dimension(width, 25));
        panel.add(textField);
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
            titleLabel.setFont(new Font("Monospaced", Font.BOLD, 14));
            panel.add(titleLabel, BorderLayout.NORTH);
        }
        return panel;
    }

    private class RegisterButtonClickListener implements ActionListener {
        @SuppressWarnings("unused") // TODO: actually handle the unused var 'field'
        private final JTextField field;
        private final String label;

        public RegisterButtonClickListener(JTextField field, String label) {
            this.field = field;
            this.label = label;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                short value;
                if (!binaryField.getText().isEmpty()) {
                    value = (short) Integer.parseInt(binaryField.getText(), 2);
                } else if (!octalField.getText().isEmpty()) {
                    value = (short) Integer.parseInt(octalField.getText(), 8);
                } else if (!decimalField.getText().isEmpty()) {
                    value = (short) Integer.parseInt(decimalField.getText());
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter a value in Binary, Octal, or Decimal field.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                switch (label) {
                    case "GPR0":
                        machine.gprs[0] = value;
                        break;
                    case "GPR1":
                        machine.gprs[1] = value;
                        break;
                    case "GPR2":
                        machine.gprs[2] = value;
                        break;
                    case "GPR3":
                        machine.gprs[3] = value;
                        break;
                    case "PC":
                        machine.pc = value;
                        break;
                    case "MAR":
                        machine.mar = value;
                        break;
                    case "MBR":
                        machine.mbr = value;
                        break;
                    case "IR":
                        machine.ir = value;
                        break;
                    case "IXR1":
                        machine.idxs[0] = value;
                        break;
                    case "IXR2":
                        machine.idxs[1] = value;
                        break;
                    case "IXR3":
                        machine.idxs[2] = value;
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Unknown register: " + label, "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                }

                updateGUIFromMachine();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid input format.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class ButtonClickListener implements ActionListener {
        private final String buttonName;

        public ButtonClickListener(String buttonName) {
            this.buttonName = buttonName;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (buttonName) {
                case "Load":
                    machine.mbr = machine.memory.fetch(machine.mar);
                    updateGUIFromMachine();
                    break;
                case "Load+":
                    machine.mbr = machine.memory.fetch(machine.mar);
                    machine.mar++;
                    updateGUIFromMachine();
                    break;
                case "Store":
                    machine.memory.store(machine.mar, machine.mbr);
                    updateGUIFromMachine();
                    break;
                case "Store+":
                    machine.memory.store(machine.mar, machine.mbr);
                    machine.mar++;
                    updateGUIFromMachine();
                    break;
                case "Run":
                    running = true;
                    machine.mfr = 0;
                    updateGUIFromMachine();
                    new Thread(() -> {
                        while (running) {
                            machine.step();
                            if (machine.mfr != 0) {
                                running = false;
                            }
                            updateGUIFromMachine();
                            // try {
                            //     Thread.sleep(1); // Adjust the sleep time as needed
                            // } catch (InterruptedException ex) {
                            //     ex.printStackTrace();
                            // }
                        }
                        machine.mar = machine.pc;
                        machine.mbr = machine.memory.fetch(machine.mar);
                        updateGUIFromMachine();
                    }).start();
                    break;
                case "Step":
                    machine.mfr = 0;
                    machine.step();
                    updateGUIFromMachine();
                    break;
                case "Halt":
                    running = false;
                    break;
                case "IPL":
                    machine.init();
                    consoleTextArea.setText(""); // Clear the console output
                    JFileChooser fileChooser = new JFileChooser();
                    int returnValue = fileChooser.showOpenDialog(null);
                    if (returnValue == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = fileChooser.getSelectedFile();
                        try {
                            byte[] byteContents = Files.readAllBytes(Paths.get(selectedFile.getAbsolutePath()));
                            short[] rom = new short[byteContents.length / 2];
                            ByteBuffer.wrap(byteContents).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(rom);
                            machine.loadRom(rom);
                            updateGUIFromMachine();
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(null, "Error loading ROM file.", "File Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    updateGUIFromMachine();
                    break;
                default:
                    JOptionPane.showMessageDialog(null, buttonName + " clicked!", "Button Click", JOptionPane.INFORMATION_MESSAGE);
                    break;
            }
        }
    }

    private JPanel createCacheStatsPanel() {
        JPanel panel = createStyledPanel("Cache Statistics");
        panel.setLayout(new GridLayout(3, 2, 5, 5));

        cacheHitsField = new JTextField(10);
        cacheMissesField = new JTextField(10);
        cacheHitRateField = new JTextField(10);
        
        cacheHitsField.setEditable(false);
        cacheMissesField.setEditable(false);
        cacheHitRateField.setEditable(false);

        panel.add(new JLabel("Cache Hits:"));
        panel.add(cacheHitsField);
        panel.add(new JLabel("Cache Misses:"));
        panel.add(cacheMissesField);
        panel.add(new JLabel("Hit Rate:"));
        panel.add(cacheHitRateField);

        return panel;
    }

    // Add a method to wait for character input
    public char waitForCharInput() {
        final Object lock = new Object();
        final char[] result = new char[1];

        charField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                synchronized (lock) {
                    result[0] = e.getKeyChar();
                    charField.setText(""); // Clear the input box
                    lock.notify(); // Notify the waiting thread
                }
            }
        });

        synchronized (lock) {
            try {
                lock.wait(); // Wait for input
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        return result[0];
    }   

    public void printToPrinter(char data) {
        consoleTextArea.append(String.format("%c", data));
        consoleTextArea.setCaretPosition(consoleTextArea.getDocument().getLength()); // Scroll to the bottom
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