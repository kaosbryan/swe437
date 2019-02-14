package quizretakes;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.Color;
import javax.swing.JRadioButton;
import javax.swing.JComboBox;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JList;

public class ScheduleGUI extends JFrame {

	private JPanel contentPane;
	private JTextField courseTXT;
	private JTextArea scheduleTXT;
	private JTextField scheduleTitle;
	private JTextField txtName;
	private JTextArea quizzes;
	private JTextArea current;
	private JTextArea txtrPleaseEnterQuiz;
	private String[] ids;
	private JButton confirm;
	
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
					ScheduleGUI frame = new ScheduleGUI();
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
	public ScheduleGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(800, 800, 600, 800);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{1, 75, 261, 88, 0};
		gbl_contentPane.rowHeights = new int[]{1, 50, 30, 26, 4, 26, 29, 55, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		
	}
}
