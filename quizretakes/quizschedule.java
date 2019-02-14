// JO 3-Jan-2019
package quizretakes;

import javax.servlet.ServletException;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.Scanner;
import java.time.*;
import java.lang.Long;
import java.lang.String;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Jeff Offutt
 *         Date: January, 2019
 *
 * Wiring the pieces together:
 *    quizschedule.java -- Servlet entry point for students to schedule quizzes
 *    quizReader.java -- reads XML file and stores in quizzes.
                             Used by quizschedule.java
 *    quizzes.java -- A list of quizzes from the XML file
 *                    Used by quizschedule.java
 *    quizBean.java -- A simple quiz bean
 *                      Used by quizzes.java and readQuizzesXML.java
 *    retakesReader.java -- reads XML file and stores in retakes.
                             Used by quizschedule.java
 *    retakes.java -- A list of retakes from the XML file
 *                    Used by quizschedule.java
 *    retakeBean.java -- A simple retake bean
 *                      Used by retakes.java and readRetakesXML.java
 *    apptBean.java -- A bean to hold appointments

 *    quizzes.xml -- Data file of when quizzes were given
 *    retakes.xml -- Data file of when retakes are given
 */

public class quizschedule 
{
   // Data files
   // location maps to /webapps/offutt/WEB-INF/data/ from a terminal window.
   // These names show up in all servlets
   private static final String dataLocation    = "/Users/Bryan/Desktop/quizretakes/";
   static private final String separator = ",";
   private static final String courseBase   = "course";
   private static final String quizzesBase = "quiz-orig";
   private static final String retakesBase = "quiz-retakes";
   private static final String apptsBase   = "quiz-appts";

   // Filenames to be built from above and the courseID parameter
   private String courseFileName;
   private String quizzesFileName;
   private String retakesFileName;
   private String apptsFileName;

   // Passed as parameter and stored in course.xml file (format: "swe437")
   private String courseID;
   // Stored in course.xml file, default 14
   // Number of days a retake is offered after the quiz is given
   private int daysAvailable = 14;
   
    private JFrame gui;
	private JPanel contentPane;
	private JTextField courseTXT;
	private JTextArea scheduleTXT;
	private JTextField scheduleTitle;
	private JTextField txtName;
	private JTextArea quizzes;
	private JTextArea currentSchedule;
	private JTextArea txtrPleaseEnterQuiz;
	private JButton confirm;
	private JTextArea errorTXT;
	private JLabel titleTXT;
	private JTextField passwordTXT;
	private JButton instructorBT;
	private JTextField studentTXT;
	private JTextField intstructorTXT;
	private JTextArea scheduleError;
	private JButton studentBT;
	private JTextArea instructorViewer;
	private JTextField titleViewer;
	
public quizschedule() {
	gui = new JFrame();
	gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	gui.setBounds(100, 100, 450, 300);
	contentPane = new JPanel();
	contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	gui.setContentPane(contentPane);
	GridBagLayout gbl_contentPane = new GridBagLayout();
	gbl_contentPane.columnWidths = new int[]{1, 75, 261, 88, 0};
	gbl_contentPane.rowHeights = new int[]{1, 50, 30, 26, 4, 26, 29, 55, 0};
	gbl_contentPane.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
	gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
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
	
	intstructorTXT = new JTextField();
	intstructorTXT.setForeground(new Color(0, 128, 0));
	intstructorTXT.setText("Instructor:");
	GridBagConstraints gbc_txtInstructor = new GridBagConstraints();
	gbc_txtInstructor.insets = new Insets(0, 0, 5, 5);
	gbc_txtInstructor.fill = GridBagConstraints.HORIZONTAL;
	gbc_txtInstructor.gridx = 1;
	gbc_txtInstructor.gridy = 4;
	contentPane.add(intstructorTXT, gbc_txtInstructor);
	intstructorTXT.setColumns(10);
	
	passwordTXT = new JTextField();
	passwordTXT.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			passwordTXT.setText("");
		}
	});
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
			if (passwordTXT.getText().equals("offutt")) {
				courseTXT.setVisible(false);
				courseTXT.setEnabled(false);
				passwordTXT.setEnabled(false);
				passwordTXT.setVisible(false);
				studentBT.setEnabled(false);
				studentBT.setVisible(false);
				studentTXT.setVisible(false);
				studentTXT.setEnabled(false);
				intstructorTXT.setVisible(false);
				intstructorTXT.setEnabled(false);
				titleTXT.setEnabled(false);
				titleTXT.setVisible(false);
				instructorBT.setEnabled(false);
				instructorBT.setVisible(false);
				contentPane.setLayout(null);
				instructorViewer.setVisible(true);
				instructorViewer.setEnabled(true);
				titleViewer.setVisible(true);
				titleViewer.setEnabled(true);
				gui.setBounds(100, 100, 450, 800);
				String name = "quiz-appts-null";
				readFile(name);
			}
			else {
				errorTXT.setText("You have put in a incorrect password");
			}
		}
	});
	GridBagConstraints gbc_instructorBT = new GridBagConstraints();
	gbc_instructorBT.insets = new Insets(0, 0, 5, 0);
	gbc_instructorBT.gridx = 3;
	gbc_instructorBT.gridy = 5;
	contentPane.add(instructorBT, gbc_instructorBT);
	
	titleViewer = new JTextField();
	titleViewer.setForeground(new Color(0, 128, 0));
	titleViewer.setFont(new Font("Lucida Grande", Font.BOLD, 20));
	titleViewer.setText("Instructor Schedule View");
	titleViewer.setHorizontalAlignment(SwingConstants.CENTER);
	titleViewer.setEnabled(false);
	titleViewer.setEditable(false);
	titleViewer.setVisible(false);
	titleViewer.setBounds(12, 6, 432, 39);
	contentPane.add(titleViewer);
	titleViewer.setColumns(10);
	
	scheduleTitle = new JTextField();
	scheduleTitle.setEnabled(false);
	scheduleTitle.setEditable(false);
	scheduleTitle.setVisible(false);
	scheduleTitle.setForeground(new Color(0, 128, 0));
	scheduleTitle.setHorizontalAlignment(SwingConstants.CENTER);
	scheduleTitle.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
	GridBagConstraints gbc_scheduleTitle = new GridBagConstraints();
	gbc_scheduleTitle.anchor = GridBagConstraints.NORTH;
	gbc_scheduleTitle.gridwidth = 2;
	gbc_scheduleTitle.insets = new Insets(0, 0, 5, 0);
	gbc_scheduleTitle.fill = GridBagConstraints.HORIZONTAL;
	gbc_scheduleTitle.gridx = 0;
	gbc_scheduleTitle.gridy = 0;
	contentPane.add(scheduleTitle, gbc_scheduleTitle);
	scheduleTitle.setColumns(10);
	
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
	txtrPleaseEnterQuiz.setText("Please Type first Quiz ID followed by the RetakeID Here. (Please put one space between IDS) ");
	txtrPleaseEnterQuiz.setBounds(12, 111, 200, 178);
	contentPane.add(txtrPleaseEnterQuiz);
	
	confirm = new JButton("Confirm");
	confirm.setVisible(false);
	confirm.setEnabled(false);
	confirm.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			boolean f = false;
			if (txtrPleaseEnterQuiz.getText().equals("Please Type all Quiz ID and RetakeID Here. (Please put one space between IDS) ")) {
				 scheduleError.setText("You didn't write any quizzes to retake.");
			}
			else if(txtName.getText().split(" ").length > 2 || txtName.getText().split(" ").length == 1) {
				scheduleError.setText("Please only use your first and last name.");
			}
			else {
				int i = 0;
				int j = 0;
				String[] ids = new String[txtrPleaseEnterQuiz.getText().split("(?<!\\G\\w+)\\s").length];
				for (String b : txtrPleaseEnterQuiz.getText().split("(?<!\\G\\w+)\\s")) {
					for (String c: b.split(" ")) { 
						if (j == 1) {
							if (Integer.parseInt(c) > 39) {
								scheduleError.setText("Please enter a valid Quiz Retake ID");
								f = true;
							}
						}
						else {
							if (Integer.parseInt(c) > 13) {
								scheduleError.setText("Please entet a valid Quiz ID.");
								f = true;
							}
						}
						j++;
					}
					j = 0;
					ids[i] = b;
					i++;
				}
				if (!f) {
					try {
						doPost(txtName.getText(), ids, courseID);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	});
	confirm.setBounds(6, 301, 205, 29);
	contentPane.add(confirm);
	
	scheduleTXT = new JTextArea();
	scheduleTXT.setFont(new Font("Lucida Grande", Font.BOLD, 15));
	scheduleTXT.setEnabled(false);
	scheduleTXT.setVisible(false);
	scheduleTXT.setEditable(false);
	scheduleTXT.setLineWrap(true);
	scheduleTXT.setWrapStyleWord(true);
	scheduleTXT.setBounds(224, 73, 320, 122);
	contentPane.add(scheduleTXT);
	
	scheduleError = new JTextArea();
	scheduleError.setForeground(new Color(255, 0, 0));
	scheduleError.setFont(new Font("Lucida Grande", Font.BOLD, 13));
	scheduleError.setWrapStyleWord(true);
	scheduleError.setVisible(false);
	scheduleError.setEnabled(false);
	scheduleError.setLineWrap(true);
	scheduleError.setEnabled(false);
	scheduleError.setEditable(false);
	scheduleError.setBounds(12, 342, 200, 395);
	contentPane.add(scheduleError);
		
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
	
	txtName = new JTextField();
	txtName.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			txtName.setText("");
		}
	});
	txtName.setEnabled(false);
	txtName.setVisible(false);
	txtName.setHorizontalAlignment(SwingConstants.CENTER);
	txtName.setText("Please Type your Name Here");
	txtName.setBounds(9, 73, 205, 36);
	contentPane.add(txtName);
	txtName.setColumns(10);
	
	currentSchedule = new JTextArea();
	currentSchedule.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
	currentSchedule.setWrapStyleWord(true);
	currentSchedule.setLineWrap(true);
	currentSchedule.setEnabled(false);
	currentSchedule.setVisible(false);
	currentSchedule.setEditable(false);
	currentSchedule.setBounds(224, 209, 320, 532);
	contentPane.add(currentSchedule);
	
	studentBT = new JButton("Submit");
	studentBT.addMouseListener(new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			String courseID = courseTXT.getText();
			boolean a = doGet(courseID);
			if (!a) {
				courseTXT.setVisible(false);
				courseTXT.setEnabled(false);
				passwordTXT.setEnabled(false);
				passwordTXT.setVisible(false);
				studentBT.setEnabled(false);
				studentBT.setVisible(false);
				studentTXT.setVisible(false);
				studentTXT.setEnabled(false);
				intstructorTXT.setVisible(false);
				intstructorTXT.setEnabled(false);
				titleTXT.setEnabled(false);
				titleTXT.setVisible(false);
				instructorBT.setEnabled(false);
				instructorBT.setVisible(false);
				scheduleError.setVisible(true);
				scheduleError.setEnabled(true);
				scheduleTXT.setEnabled(true);
				scheduleTXT.setVisible(true);
				scheduleTitle.setEnabled(true);
				scheduleTitle.setVisible(true);
				txtName.setEnabled(true);
				txtName.setVisible(true);
				quizzes.setEnabled(true);
				quizzes.setVisible(true);
				currentSchedule.setEnabled(true);
				currentSchedule.setVisible(true);
				txtrPleaseEnterQuiz.setVisible(true);
				txtrPleaseEnterQuiz.setEnabled(true);
				confirm.setVisible(true);
				confirm.setEnabled(true);
				contentPane.setLayout(null);
				gui.setBounds(800, 800, 875, 800);
			}
		}
	});
	GridBagConstraints gbc_studentBT = new GridBagConstraints();
	gbc_studentBT.anchor = GridBagConstraints.NORTH;
	gbc_studentBT.insets = new Insets(0, 0, 5, 0);
	gbc_studentBT.gridx = 3;
	gbc_studentBT.gridy = 3;
	contentPane.add(studentBT, gbc_studentBT);
	
	instructorViewer = new JTextArea();
	
	instructorViewer.setWrapStyleWord(true);
	instructorViewer.setLineWrap(true);
	instructorViewer.setVisible(false);
	instructorViewer.setEnabled(false);
	instructorViewer.setEditable(false);
	instructorViewer.setBounds(6, 56, 438, 700);
	contentPane.add(instructorViewer);

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
	
	gui.setVisible(true);
}
public void readFile(String name) {
	File file = new File(dataLocation + "/" + name + ".txt");
	Scanner input;
	retakes retakesList = new retakes();
	retakesReader rr = new retakesReader();
	try {
		retakesList = rr.read(dataLocation + retakesBase + "-" + "swe437" + ".xml");
		try {
			input = new Scanner(file);
			while(input.hasNext()) {
				ArrayList<String> data = new ArrayList<String>();
			    String info = input.nextLine();
			    for (String a: info.split("[\\s,]+")) {
			    	data.add(a);
			    }
			    instructorViewer.append(data.get(2) + " " + data.get(3) + " has signed up for Quiz "
			    + data.get(0) + " on ");
			    for(retakeBean r: retakesList) {
			    	LocalDate retakeDay = r.getDate();
			    	if (r.getID() == Integer.parseInt((data.get(1)))) {
			    		 instructorViewer.append(retakeDay.getDayOfWeek() + ", " + 
			                      retakeDay.getMonth() + " " +
			                      retakeDay.getDayOfMonth() + ", at " +
			                      r.timeAsString() + " in " +
			                      r.getLocation() + "\n");
			    		 break;
			    	}
			    	
			    }
			}
			input.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	} catch (IOException | ParserConfigurationException | SAXException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
}

// doGet() : Prints the form to schedule a retake
public boolean doGet(String courseID)
{
	boolean failure = false;
   // CourseID must be a parameter (also in course XML file, but we need to know which course XML file ...)
   if (courseID != null && !courseID.isEmpty())
   {  // If not, ask for one.
      courseBean course;
      courseReader cr = new courseReader();
      courseFileName = dataLocation + courseBase + "-" + courseID + ".xml";
      try {
         course = cr.read(courseFileName);
      } catch (Exception e) {
         String message = "Can't find the data files for course ID " + courseID + ". You can try again.";
         errorTXT.setEnabled(true);
         errorTXT.setText(message);
         failure = true;
         return failure;
      }
      daysAvailable = Integer.parseInt(course.getRetakeDuration());

      // Filenames to be built from above and the courseID
      quizzesFileName = dataLocation + quizzesBase + "-" + courseID + ".xml";
      retakesFileName = dataLocation + retakesBase + "-" + courseID + ".xml";
      String apptsFileName   = dataLocation + apptsBase   + "-" + courseID + ".txt";

      // Load the quizzes and the retake times from disk
      quizzes quizList    = new quizzes();
      retakes retakesList = new retakes();
      quizReader    qr = new quizReader();
      retakesReader rr = new retakesReader();

      try { // Read the files and print the form
         quizList    = qr.read (quizzesFileName);
         retakesList = rr.read (retakesFileName);
         printQuizScheduleForm(quizList, retakesList, course);
      } catch (Exception e)
      {
         String message = "Can't find the data files for course ID " + courseID + ". You can try again.";
         errorTXT.setEnabled(true);
         errorTXT.setText(message);
         failure = true;
      }
   }
   if (!failure) {
	   errorTXT.setEnabled(false);
	   errorTXT.setVisible(false);
	   courseTXT.setEnabled(false);
	   courseTXT.setVisible(false);
   }
   return failure; 
}

// doPost saves an appointment in a file and prints an acknowledgement
protected void doPost(String studentName, String[] allIDs, String courseID) throws IOException
{
   // No saving if IOException
   boolean IOerrFlag = false;
   String IOerrMessage = "";

   // Filename to be built from above and the courseID
   apptsFileName   = dataLocation + apptsBase + "-" + courseID + ".txt";

   if(allIDs != null && studentName != null && studentName.length() > 0 && !studentName.equals("Please Type your Name Here"))
   {
      // Append the new appointment to the file
      try {
         File file = new File(apptsFileName);
         synchronized(file)
         { // Only one student should touch this file at a time.
            if (!file.exists())
            {
               file.createNewFile();
            }
            FileWriter     fw = new FileWriter(file.getAbsoluteFile(), true); //append mode
            BufferedWriter bw = new BufferedWriter(fw);

            for(String oneIDPair : allIDs)
            {
               bw.write(oneIDPair + separator + studentName + "\n");
            }

            bw.flush();
            bw.close();
         } // end synchronize block
      } catch (IOException e) {
         IOerrFlag = true;
         IOerrMessage = "I failed and could not save your appointment." + e;
      }

      // Respond to the student
      if (IOerrFlag)
      {
         scheduleError.setText(IOerrMessage);
      } else {
         if (allIDs.length == 1)
        	scheduleError.setText(studentName + ", your appointment has been scheduled. ");
         else
        	scheduleError.setText(studentName + ", your appointments have been scheduled. ");
            scheduleError.append("Please arrive in time to finish the quiz before the end of the retake period. ");
            scheduleError.append("If you cannot make it, please cancel by sending email to your professor.");
      }

   } else { // allIDs == null or name is null
      if(allIDs == null)
    	  scheduleError.setText("You didn't write any quizzes to retake.");
      if(studentName == null || studentName.length() == 0 || studentName.equals("Please Type your Name Here"))
    	  scheduleError.setText("You didn't give a name ... no anonymous quiz retakes.");   
   }
}

/**
 * Print the body of HTML
 * @param out PrintWriter
 * @throws ServletException
 * @throws IOException
*/
private void printQuizScheduleForm (quizzes quizList, retakes retakesList, courseBean course)
        throws IOException
{
   // Check for a week to skip
   boolean skip = false;
   LocalDate startSkip = course.getStartSkip();
   LocalDate endSkip   = course.getEndSkip();

   boolean retakePrinted = false;
   
   scheduleTitle.setText("GMU quiz retake scheduler for class " + course.getCourseTitle());
   
   // print the main form
   scheduleTXT.append("You can sign up for quiz retakes within the next two weeks.\n");
   scheduleTXT.append("Enter your name (as it appears on the class roster), ");
   scheduleTXT.append("then select which date, time, and quiz you wish to retake from the following list.\n\n");
   
   LocalDate today  = LocalDate.now();
   LocalDate endDay = today.plusDays(new Long(daysAvailable));
   LocalDate origEndDay = endDay;
   // if endDay is between startSkip and endSkip, add 7 to endDay
   if (!endDay.isBefore(startSkip) && !endDay.isAfter(endSkip))
   {  // endDay is in a skip week, add 7 to endDay
      endDay = endDay.plusDays(new Long(7));
      skip = true;
   }
   currentSchedule.append("Today is " + today.getDayOfWeek() + ", " + today.getMonth() + " " + today.getDayOfMonth()+ "\n");
   currentSchedule.append("Currently scheduling quizzes for the next two weeks, until ");
   currentSchedule.append((endDay.getDayOfWeek()) + ", " + endDay.getMonth() + " " + endDay.getDayOfMonth() + "\n\n");

   for(retakeBean r: retakesList)
   {
      LocalDate retakeDay = r.getDate();
      if (!(retakeDay.isBefore (today)) && !(retakeDay.isAfter (endDay)))
      {
         // if skip && retakeDay is after the skip week, print a white bg message
         if (skip && retakeDay.isAfter(origEndDay))
         {  // A "skip" week such as spring break.
        	currentSchedule.append("Skipping a week, no quiz or retakes.");
            // Just print for the FIRST retake day after the skip week
            skip = false;
         }
         retakePrinted = true;
         currentSchedule.append(retakeDay.getDayOfWeek() + ", " + 
                      retakeDay.getMonth() + " " +
                      retakeDay.getDayOfMonth() + ", at " +
                      r.timeAsString() + " in " +
                      r.getLocation() + "\n");

         for(quizBean q: quizList)
         {
            LocalDate quizDay = q.getDate();
            LocalDate lastAvailableDay = quizDay.plusDays(new Long(daysAvailable) - 1);
            // To retake a quiz on a given retake day, the retake day must be within two ranges:
            // quizDay <= retakeDay <= lastAvailableDay --> (!quizDay > retakeDay) && !(retakeDay > lastAvailableDay)
            // today <= retakeDay <= endDay --> !(today > retakeDay) && !(retakeDay > endDay)

            if (!quizDay.isAfter(retakeDay) && !retakeDay.isAfter(lastAvailableDay) &&
                !today.isAfter(retakeDay) && !retakeDay.isAfter(endDay))
            {
            	currentSchedule.append("'>Quiz " + q.getID() + " from " + quizDay.getDayOfWeek() + ", " + quizDay.getMonth() + " " + quizDay.getDayOfMonth() + "\n");
               // Value is "retakeID:quiziD"
            }
         }
      }
      if (retakePrinted)
      {
         retakePrinted = false;
      }
   }
   quizzes.setText("All quiz retake opportunities\n");
   for(retakeBean r: retakesList)
   {
	  quizzes.append(r.toString() + "\n");
   }
}

public static void main(String[] args) {
	quizschedule quizGui = new quizschedule();
} // end quizschedule class
}
