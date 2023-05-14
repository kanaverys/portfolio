import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

public class MainForm {
    private JPanel mainPanel;
    private JButton button1;

    private JPanel panel1;
    private JLabel lastNameLabel;
    private JLabel patronymicLabel;
    private JTextField nameTextField;
    private JTextField lastNameTextField;
    private JTextField patronymicTextField;

    private JPanel panel2;
    private JLabel nlpLabel;
    private JTextField nlpTextField;
    private JLabel nameLabel;

    public JPanel getMainPanel() {
        panel2.setVisible(false);
        return mainPanel;
    }

    public MainForm(){
        button1.addActionListener(new Action() {
            @Override
            public Object getValue(String key) {
                return null;
            }

            @Override
            public void putValue(String key, Object value) {

            }

            @Override
            public void setEnabled(boolean b) {

            }

            @Override
            public boolean isEnabled() {
                return false;
            }

            @Override
            public void addPropertyChangeListener(PropertyChangeListener listener) {

            }

            @Override
            public void removePropertyChangeListener(PropertyChangeListener listener) {

            }

            @Override
            public void actionPerformed(ActionEvent e) {
                if (panel1.isVisible()) {
                    if (nameTextField.getText().isEmpty() || lastNameTextField.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(mainPanel, "Заполните обязательные поля", "Внимание!",
                                JOptionPane.WARNING_MESSAGE);
                        nameTextField.setBackground(Color.PINK);
                        lastNameTextField.setBackground(Color.PINK);
                    }
                    else {
                        String fio = lastNameTextField.getText()
                                .concat(" " + nameTextField.getText()
                                        .concat(" " + patronymicTextField.getText()));
                        nlpTextField.setText(fio);
                        nlpTextField.setBackground(Color.WHITE);
                        changePanelsVisibility();
                        button1.setText("Развернуть");
                    }
                }
                else {
                    String[] fio = nlpTextField.getText().split("\\s");
                    if (fio.length<2) {
                        JOptionPane.showMessageDialog(mainPanel, "Недостаточно данных для обработки!", "Внимание!",
                                JOptionPane.WARNING_MESSAGE);
                        nlpTextField.setBackground(Color.PINK);
                    }
                    else {
                        lastNameTextField.setText(fio[0]);
                        nameTextField.setText(fio[1]);
                        patronymicTextField.setText(fio.length<3 ? "" : fio[2]);
                        lastNameTextField.setBackground(Color.WHITE);
                        nameTextField.setBackground(Color.WHITE);
                        changePanelsVisibility();
                        button1.setText("Свернуть");
                    }
                }
            }
        });
    }

    private void changePanelsVisibility() {
        panel1.setVisible(!panel1.isVisible());
        panel2.setVisible(!panel2.isVisible());
    }
}
