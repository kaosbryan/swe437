package quizretakes;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;

public class sced extends JFrame {

	private JPanel contentPane;
	private JTextField scheduleTitle;
	 
	private JTextArea scheduleTXT;
	private JTextField txtName;
	private JTextArea quizzes;
	private JTextArea current;
	private JTextArea txtrPleaseEnterQuiz;
	private JButton confirm;
	private JTextArea instructorViewer;
	private JTextField titleViewer;
	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					sced frame = new sced();
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
	public sced() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//setBounds(800, 800, 875, 800);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		scheduleTitle = new JTextField();
		scheduleTitle.setForeground(new Color(0, 128, 0));
		scheduleTitle.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		scheduleTitle.setHorizontalAlignment(SwingConstants.CENTER);
		scheduleTitle.setEnabled(false);
		scheduleTitle.setVisible(false);
		scheduleTitle.setEditable(false);
		scheduleTitle.setBounds(12, 6, 841, 48);
		contentPane.add(scheduleTitle);
		scheduleTitle.setColumns(10);
		
		quizzes = new JTextArea();
		quizzes.setLineWrap(true);
		quizzes.setWrapStyleWord(true);
		quizzes.setEnabled(false);
		quizzes.setVisible(false);
		quizzes.setEditable(false);
		quizzes.setBounds(556, 73, 297, 669);
		contentPane.add(quizzes);
		
		txtrPleaseEnterQuiz = new JTextArea();
		txtrPleaseEnterQuiz.setVisible(false);
		txtrPleaseEnterQuiz.setEnabled(false);
		txtrPleaseEnterQuiz.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				txtrPleaseEnterQuiz.setText("");
			}
		});
		txtrPleaseEnterQuiz.setLineWrap(true);
		txtrPleaseEnterQuiz.setWrapStyleWord(true);
		txtrPleaseEnterQuiz.setText("Please Enter all Quiz ID and RetakeID. (Please put a space between IDS) ");
		txtrPleaseEnterQuiz.setBounds(12, 111, 200, 178);
		contentPane.add(txtrPleaseEnterQuiz);
		
		
		confirm = new JButton("Confirm");
		confirm.setVisible(false);
		confirm.setEnabled(false);
		confirm.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		confirm.setBounds(6, 301, 205, 29);
		contentPane.add(confirm);
		
		scheduleTXT = new JTextArea();
		scheduleTXT.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		scheduleTXT.setEnabled(false);
		scheduleTXT.setVisible(false);
		scheduleTXT.setEditable(false);
		scheduleTXT.setLineWrap(true);
		scheduleTXT.setWrapStyleWord(true);
		scheduleTXT.setBounds(224, 73, 320, 122);
		contentPane.add(scheduleTXT);
		
		txtName = new JTextField();
		txtName.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				txtName.setText("");
			}
		});
		txtName.setEnabled(false);
		txtName.setVisible(false);
		txtName.setHorizontalAlignment(SwingConstants.LEFT);
		txtName.setText("Please Type Your Name Here. ");
		txtName.setBounds(9, 73, 205, 36);
		contentPane.add(txtName);
		txtName.setColumns(10);
		
		current = new JTextArea();
		current.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		current.setWrapStyleWord(true);
		current.setLineWrap(true);
		current.setEnabled(false);
		current.setVisible(false);
		current.setEditable(false);
		current.setBounds(224, 209, 320, 532);
		contentPane.add(current);
		
		JTextArea scheduleError = new JTextArea();
		scheduleError.setForeground(new Color(255, 0, 0));
		scheduleError.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		scheduleError.setWrapStyleWord(true);
		scheduleError.setLineWrap(true);
		scheduleError.setEnabled(false);
		scheduleError.setEditable(false);
		scheduleError.setBounds(12, 342, 200, 395);
		contentPane.add(scheduleError);
		
		instructorViewer = new JTextArea();
		instructorViewer.setWrapStyleWord(true);
		instructorViewer.setLineWrap(true);
		instructorViewer.setEnabled(false);
		instructorViewer.setEditable(false);
		instructorViewer.setBounds(6, 56, 438, 216);
		contentPane.add(instructorViewer);
		
		titleViewer = new JTextField();
		titleViewer.setForeground(new Color(0, 128, 0));
		titleViewer.setFont(new Font("Lucida Grande", Font.BOLD, 20));
		titleViewer.setHorizontalAlignment(SwingConstants.CENTER);
		titleViewer.setEnabled(false);
		titleViewer.setEditable(false);
		titleViewer.setBounds(12, 6, 432, 39);
		contentPane.add(titleViewer);
		titleViewer.setColumns(10);
	}
}
