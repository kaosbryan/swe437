package quizretakes;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingConstants;
import javax.swing.JTextPane;
import javax.swing.JTextArea;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;

public class quizGui extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField txtPleaseEnterYour;
	private JTextField courseTXT;
	private JTextField Error;
	private JTextArea error;
	private JTextArea errorTXT;
	private JLabel titleTXT;
	private JTextArea textArea;
	private JLabel lblNewLabel_3;
	private JTextField passwordTXT;
	private JButton instructorBT;
	private JTextField studentTXT;
	private JTextField txtInstructor;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					quizGui frame = new quizGui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public quizGui() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		quizschedule quiz = new quizschedule();
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{1, 75, 261, 88, 0};
		gbl_contentPane.rowHeights = new int[]{1, 50, 30, 26, 4, 26, 29, 55, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JTextPane textPane = new JTextPane();
		textPane.setEnabled(false);
		textPane.setEditable(false);
		GridBagConstraints gbc_textPane = new GridBagConstraints();
		gbc_textPane.anchor = GridBagConstraints.SOUTH;
		gbc_textPane.fill = GridBagConstraints.HORIZONTAL;
		gbc_textPane.insets = new Insets(0, 0, 5, 5);
		gbc_textPane.gridx = 0;
		gbc_textPane.gridy = 0;
		contentPane.add(textPane, gbc_textPane);
		
		titleTXT = new JLabel("GMU Quiz Retake Scheduler");
		titleTXT.setForeground(new Color(34, 139, 34));
		titleTXT.setFont(new Font("Al Bayan", Font.BOLD, 30));
		GridBagConstraints gbc_titleTXT = new GridBagConstraints();
		gbc_titleTXT.insets = new Insets(0, 0, 5, 0);
		gbc_titleTXT.gridwidth = 3;
		gbc_titleTXT.gridx = 1;
		gbc_titleTXT.gridy = 1;
		contentPane.add(titleTXT, gbc_titleTXT);
		
		studentTXT = new JTextField();
		studentTXT.setEditable(false);
		studentTXT.setForeground(new Color(0, 128, 0));
		studentTXT.setText("Student: ");
		GridBagConstraints gbc_studentTXT = new GridBagConstraints();
		gbc_studentTXT.fill = GridBagConstraints.HORIZONTAL;
		gbc_studentTXT.insets = new Insets(0, 0, 5, 5);
		gbc_studentTXT.gridx = 1;
		gbc_studentTXT.gridy = 2;
		contentPane.add(studentTXT, gbc_studentTXT);
		studentTXT.setColumns(10);
		
		courseTXT = new JTextField();
		courseTXT.setToolTipText("");
		courseTXT.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				courseTXT.setText("");
			}
		});
		courseTXT.setHorizontalAlignment(SwingConstants.CENTER);
		courseTXT.setText("Please Enter Your Course ID Here. ");
		GridBagConstraints gbc_courseTXT = new GridBagConstraints();
		gbc_courseTXT.fill = GridBagConstraints.HORIZONTAL;
		gbc_courseTXT.insets = new Insets(0, 0, 5, 5);
		gbc_courseTXT.gridwidth = 2;
		gbc_courseTXT.gridx = 1;
		gbc_courseTXT.gridy = 3;
		contentPane.add(courseTXT, gbc_courseTXT);
		courseTXT.setColumns(10);
		
		JButton studentBT = new JButton("Submit");
		studentBT.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String courseID = courseTXT.getText();
				quiz.doGet(courseID);
			}
		});
		GridBagConstraints gbc_studentBT = new GridBagConstraints();
		gbc_studentBT.anchor = GridBagConstraints.NORTH;
		gbc_studentBT.insets = new Insets(0, 0, 5, 0);
		gbc_studentBT.gridx = 3;
		gbc_studentBT.gridy = 3;
		contentPane.add(studentBT, gbc_studentBT);
		
		txtInstructor = new JTextField();
		txtInstructor.setForeground(new Color(0, 128, 0));
		txtInstructor.setText("Instructor:");
		GridBagConstraints gbc_txtInstructor = new GridBagConstraints();
		gbc_txtInstructor.insets = new Insets(0, 0, 5, 5);
		gbc_txtInstructor.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtInstructor.gridx = 1;
		gbc_txtInstructor.gridy = 4;
		contentPane.add(txtInstructor, gbc_txtInstructor);
		txtInstructor.setColumns(10);
		
		passwordTXT = new JTextField();
		passwordTXT.setHorizontalAlignment(SwingConstants.CENTER);
		passwordTXT.setText("Please Enter Instructor Password Here. ");
		GridBagConstraints gbc_passwordTXT = new GridBagConstraints();
		gbc_passwordTXT.fill = GridBagConstraints.HORIZONTAL;
		gbc_passwordTXT.insets = new Insets(0, 0, 5, 5);
		gbc_passwordTXT.gridwidth = 2;
		gbc_passwordTXT.gridx = 1;
		gbc_passwordTXT.gridy = 5;
		contentPane.add(passwordTXT, gbc_passwordTXT);
		passwordTXT.setColumns(10);
		
		instructorBT = new JButton("Submit");
		instructorBT.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		GridBagConstraints gbc_instructorBT = new GridBagConstraints();
		gbc_instructorBT.insets = new Insets(0, 0, 5, 0);
		gbc_instructorBT.gridx = 3;
		gbc_instructorBT.gridy = 5;
		contentPane.add(instructorBT, gbc_instructorBT);
	
		errorTXT = new JTextArea();
		errorTXT.setForeground(new Color(255, 0, 0));
		errorTXT.setLineWrap(true);
		errorTXT.setWrapStyleWord(true);
		errorTXT.setEnabled(false);
		errorTXT.setEditable(false);
		GridBagConstraints gbc_errorTXT = new GridBagConstraints();
		gbc_errorTXT.gridheight = 2;
		gbc_errorTXT.fill = GridBagConstraints.BOTH;
		gbc_errorTXT.insets = new Insets(0, 0, 0, 5);
		gbc_errorTXT.gridx = 2;
		gbc_errorTXT.gridy = 6;
		contentPane.add(errorTXT, gbc_errorTXT);
		
	}
}
