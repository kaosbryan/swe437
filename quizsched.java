// JO, Jan 2019
package quizretakes;

// A command line interface (CLI) for the quizschedule servlet
// SWE 437, Assignment 2, Spring 2019
// This class is based on the quizschedule.java servlet
// Removed all the servlet stuff
// New code has the comment /* CLI */

import java.util.Scanner; /* CLI */

//=============================================================================

import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.PrintStream;
import javax.xml.parsers.ParserConfigurationException;
import java.util.ArrayList;
import java.time.*;
import java.lang.Long;
import java.lang.String;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

import java.util.Properties; /* CLI */

/**
 * @author Jeff Offutt
 *         Date: January, 2019
 *
 * Wiring the pieces together:
 *    quizschedule.java -- Servlet entry point for students to schedule quizzes
 *    quizReader.java -- reads XML file and stores in quizzes.
 *                       Used by quizschedule.java
 *    quizzes.java -- A list of quizzes from the XML file
 *                    Used by quizschedule.java
 *    quizBean.java -- A simple quiz bean
 *                     Used by quizzes.java and readQuizzesXML.java
 *    retakesReader.java -- reads XML file and stores in retakes.
 *                          Used by quizschedule.java
 *    retakes.java -- A list of retakes from the XML file
 *                    Used by quizschedule.java
 *    retakeBean.java -- A simple retake bean
 *                       Used by retakes.java and readRetakesXML.java
 *    apptBean.java -- A bean to hold appointments
 *
 *    quizzes.xml -- Data file of when quizzes were given
 *    retakes.xml -- Data file of when retakes are given
 *    course.xml -- Data file with information about the course
 */

public class quizsched
{
   // Used to pass retakeID,quizID pairs from printQuizScheduleForm() to readInputSave()
   private static Properties retakeQuizIDProps = new Properties(); /* CLI */

   // Data files
   /* CLI: All variables changed to static to use in main() */
   private static final String dataLocation = "/Users/Bryan/Desktop/quizretakes/"; /* CLI */
   private static final String separator    = ",";
   private static final String courseBase   = "course";
   private static final String quizzesBase  = "quiz-orig";
   private static final String retakesBase  = "quiz-retakes";
   private static final String apptsBase    = "quiz-appts";

   // Filenames to be built from above and the courseID
   private static String courseFileName;
   private static String quizzesFileName;
   private static String retakesFileName;
   private static String apptsFileName;

   // Stored in course.xml file, default 14
   // Number of days a retake is offered after the quiz is given
   static int daysAvailable = 14;

// ===============================================================
// Prints the form to schedule a retake
public static void main(String []argv) /* CLI */
{
   System.out.println("Would you like to access Instructor view (Type 1) or Student View (Type anything else except 1)?");
   Scanner accessNumber = new Scanner(System.in); 
   String number = accessNumber.next();
   if (number.equals("1")) {
	   quizretakeModifer();
   }
   else {
	   Scanner sc = new Scanner(System.in); /* CLI */
	
	   // Get course ID from user (could be passed as a command line parameter ...)
	   String courseID = readCourseID(sc); /* CLI */
	   buildFileNames(courseID); /* CLI */
	
	   // Get information about the course
	   courseBean course;
	   try {
	      course = readCourseFile(courseID);
	   } catch(Exception e) {
	      System.out.println("Can't find the data files for course ID " + courseID + ". You can try again with a different course ID.");
	      return;
	   }
	   daysAvailable = Integer.parseInt(course.getRetakeDuration());
	 
	   try { // Read the files and print the form
	      quizzes quizList; /* CLI */
	      retakes retakesList; /* CLI */
	      quizList    =  readQuizzes(courseID); /* CLI */
	      retakesList = readRetakes(courseID); /* CLI */
	      // Inside try-block so this won't print if files can't be read
	      //test(quizList, retakesList, course);
	      printQuizScheduleForm(quizList, retakesList, course);
	   } catch(Exception e) {
	      System.out.println("Can't read the data files for course ID " + courseID + ". You can try again with a different courseID.");
	      return;
	   }
	
	   // This replaces the submit-response (was doPost() )
	   readInputSave(sc, courseID); /* CLI */
   }
   accessNumber.close();
}  // end main()

// ===============================================================
// Was doPost()
// Called from main to read student's choice and save to file
static void readInputSave(Scanner sc, String courseID) /* CLI */
{
   // Get name and list of retake requests from user
   System.out.print("What is your name? ");
   String studentName = sc.next();

   System.out.print("Enter a number from the list to schedule a retake: "); /* CLI */
   String retake = sc.next(); /* CLI */
   String retakeQuizID = retakeQuizIDProps.getProperty(retake); /* CLI */

   // Append the new appointment to the file
   try {
      File file = new File(apptsFileName);
      if(retakeQuizID != null) /* CLI */
      {  // user must choose one of the numbers on screen
         if(!file.exists())
         {
            file.createNewFile();
         }
         FileWriter     fw = new FileWriter(file.getAbsoluteFile(), true); //append mode
         BufferedWriter bw = new BufferedWriter(fw);

         bw.write(retakeQuizID + separator + studentName + "\n");

         bw.flush();
         bw.close();

         // CLI: simplified the logic in this method somewhat from the servlet version.
         // Respond to the student
         System.out.println("");
         System.out.println(studentName + ", your appointment has been scheduled.");
         System.out.println("Please arrive in time to finish the quiz before the end of the retake period.");
         System.out.println("If you cannot make it, please cancel by sending email to your professor.");
      } else {
         System.out.println("");
         System.out.println("I don't have a retake time for that number. Please try again.");
      }
   } catch(IOException e) {
      System.out.println("");
      System.out.println("I failed and could not save your appointment.\nException message is: " + e);
   }
}

// ===============================================================
/* CLI: Dropped parameter "out", now we print to screen */
// Print the quiz retake choices (maybe should also change the method name?)
static void printQuizScheduleForm(quizzes quizList, retakes retakesList, courseBean course)
{
   // Check for a week to skip
   boolean skip = false;
   LocalDate startSkip = course.getStartSkip();
   LocalDate endSkip   = course.getEndSkip();

   System.out.println("");
   System.out.println("");
   System.out.println("******************************************************************************");
   System.out.println("      GMU quiz retake scheduler for class " + course.getCourseTitle());
   System.out.println("******************************************************************************");
   System.out.println("");
   System.out.println("");

   // print the main form
   System.out.println("You can sign up for quiz retakes within the next two weeks. ");
   System.out.println("Enter your name (as it appears on the class roster), ");
   System.out.println("then select which date, time, and quiz you wish to retake from the following list.");
   System.out.println("");

   LocalDate today  = LocalDate.now();
   LocalDate endDay = today.plusDays(new Long(daysAvailable));
   LocalDate origEndDay = endDay;
   // if endDay is between startSkip and endSkip, add 7 to endDay
   if(!endDay.isBefore(startSkip) && !endDay.isAfter(endSkip))
   {  // endDay is in a skip week, add 7 to endDay
      endDay = endDay.plusDays(new Long(7));
      skip = true;
   }

   System.out.print  ("Today is ");
   System.out.println((today.getDayOfWeek()) + ", " + today.getMonth() + " " + today.getDayOfMonth() );
   System.out.print  ("Currently scheduling quizzes for the next two weeks, until ");
   System.out.println((endDay.getDayOfWeek()) + ", " + endDay.getMonth() + " " + endDay.getDayOfMonth() );
   System.out.print("");

   // Unique integer for each retake and quiz pair
   int quizRetakeCount = 0; /* CLI */
   for(retakeBean r: retakesList)
   {
      LocalDate retakeDay = r.getDate();
      if(!(retakeDay.isBefore(today)) && !(retakeDay.isAfter(endDay)))
      {
         // if skip && retakeDay is after the skip week, print a message
         if(skip && retakeDay.isAfter(origEndDay))
         {  // A "skip" week such as spring break.
            System.out.println("      Skipping a week, no quiz or retakes.");
            // Just print for the FIRST retake day after the skip week
            skip = false;
         }
         // format: Friday, January 12, at 10:00am in EB 4430
         System.out.println("RETAKE: " + retakeDay.getDayOfWeek() + ", " +
                            retakeDay.getMonth() + " " +
                            retakeDay.getDayOfMonth() + ", at " +
                            r.timeAsString() + " in " +
                            r.getLocation());

         for(quizBean q: quizList)
         {
            LocalDate quizDay = q.getDate();
            LocalDate lastAvailableDay = quizDay.plusDays(new Long(daysAvailable));
            // To retake a quiz on a given retake day, the retake day must be within two ranges:
            // quizDay <= retakeDay <= lastAvailableDay --> (!quizDay > retakeDay) && !(retakeDay > lastAvailableDay)
            // today <= retakeDay <= endDay --> !(today > retakeDay) && !(retakeDay > endDay)
            if(!quizDay.isAfter(retakeDay) && !retakeDay.isAfter(lastAvailableDay) &&
                !today.isAfter(retakeDay) && !retakeDay.isAfter(endDay))
            {
               quizRetakeCount++; /* CLI */
               // Put in a properties structure for writing to retake schedule file (CLI)
               retakeQuizIDProps.setProperty(String.valueOf(quizRetakeCount), r.getID() + separator + q.getID()); /* CLI */
               System.out.print  ("    " + quizRetakeCount + ") "); /* CLI */
               System.out.println("Quiz " + q.getID() + " from " + quizDay.getDayOfWeek() + ", " + quizDay.getMonth() + " " + quizDay.getDayOfMonth() );
            }
         }
      }
   }
   System.out.println("");
}

// ===============================================================
// Build the file names in one place to make them easier to change
static void buildFileNames(String courseID) /* CLI */
{
   courseFileName  = dataLocation + courseBase  + "-" + courseID + ".xml"; /* CLI */
   quizzesFileName = dataLocation + quizzesBase + "-" + courseID + ".xml"; /* CLI */
   retakesFileName = dataLocation + retakesBase + "-" + courseID + ".xml"; /* CLI */
   apptsFileName   = dataLocation + apptsBase   + "-" + courseID + ".txt"; /* CLI */
}

public static void quizretakeModifer() /* CLI */
{
	boolean view = true;
	welcome();
	Scanner choices = new Scanner(System.in); 
	while (view) {
		printOptions();
		view = false;
		String choice = choices.next();
		view = switchChoices(choice);
	}
	choices.close();
}

public static void welcome() {
	System.out.println("******************************************************************************");
	System.out.println("           Instructor View to add and drop retakes and quizzes");
	System.out.println("******************************************************************************");
}

public static void printOptions() {
	System.out.println("Type 1 to add a retake");
    System.out.println("Type 2 to add a quiz");
    System.out.println("Type 3 to exit out of Instructor View");
}

public static boolean switchChoices(String choice) {
	Scanner choices = new Scanner(System.in); 
	String id, location, month = "", day = "", hour = "", minute = "";
	boolean view = true;
	switch (choice) {
	case "1":
		System.out.println("Please type the ID for the retake"); 
		id = choices.nextLine();
		System.out.println("Please type the location for the retake"); 
		location = choices.nextLine();
		System.out.println("Please type the month (01-12) for the retake"); 
		month = validMonthRetake(choices);
		System.out.println("Please type the day (1-31) for the retake"); 
		day = validDay(choices);
		System.out.println("Please type the hour (0-23) for the retake"); 
		hour = validHour(choices);
		System.out.println("Please type the minute (0-59) for the retake"); 
		minute = validMinute(choices);
		addRetake(id, location, month, day, hour, minute);
		break;
	case "2":
		System.out.println("Please type the ID for the retake"); 
		id = choices.nextLine();
		System.out.println("Please type the month (01-12) for the retake"); 
		month = validMonthQuiz(choices);
		System.out.println("Please type the day (1-31) for the retake"); 
		day = validDay(choices);
		System.out.println("Please type the hour (0-23) for the retake"); 
		hour = validHour(choices);
		System.out.println("Please type the minute (0-59) for the retake"); 
		minute = validMinute(choices);
		addQuiz(id, month, day, hour, minute);
		break;
	case "3":
		view = false;
		break;
	default:
		System.out.println("Please enter a valid choice\n\n");
		break;		
	}
	return view;
}

static String validMonthRetake(Scanner choices) {
	boolean valid = true;
	String month = "";
	while (valid) {
		month = choices.nextLine();
		valid = isNotMonthRetake(month);
	}
	return month;
}

static boolean isNotMonthRetake(String month) {
	if (month.equals("01") || month.equals("02") || month.equals("03") 
			|| month.equals("04") || month.equals("05") || month.equals("06") 
			|| month.equals("07") || month.equals("08") || month.equals("09")
			|| month.equals("10") || month.equals("11") || month.equals("12")) {
		return false;
	}
	else {
		System.out.println("Please enter a valid choice");
		return true;
	}
}

static String validDay(Scanner choices) {
	String day = "";
	boolean valid = true;
	while (valid) {
		day = choices.nextLine();
		valid = isNotDay(day);
	}
	return day;	
}

static boolean isNotDay(String day) {
	try {
		if (Integer.parseInt(day) < 32 && Integer.parseInt(day) > 0)  {
			return false;
		}
		else {
			System.out.println("Please enter a valid choice");	
			return true;
		}
	}
	catch (Exception io) {
			System.out.println("Please enter a valid choice");	
			return true;
	}
}

static String validHour(Scanner choices) {
	String hour = "";
	boolean valid = true;
	while (valid) {
		hour = choices.nextLine();
		valid = isNotHour(hour);
	}
	return hour;
}

static boolean isNotHour(String hour) {
	try {
		if (Integer.parseInt(hour) < 24 && Integer.parseInt(hour) > 0)  {
			return false;
		}
		else {
			System.out.println("Please enter a valid choice");	
			return true;
		}
	}
	catch (Exception io) {
			System.out.println("Please enter a valid choice");	
			return true;
	}
}

static String validMinute(Scanner choices) {
	String minute = "";
	boolean valid = true;
	while (valid) {
		minute = choices.nextLine();
		valid = isNotMinute(minute);
	}
	return minute;
}

static boolean isNotMinute(String minute) {
	try {
		if (Integer.parseInt(minute) < 60 && Integer.parseInt(minute) > -1)  {
			return false;
		}
		else {
			System.out.println("Please enter a valid choice");	
			return true;
		}
	}
	catch (Exception io) {
			System.out.println("Please enter a valid choice");	
			return true;
	}
}


static String validMonthQuiz(Scanner choices) {
	String month = "";
	boolean valid = true;
	while (valid) {
		month = choices.nextLine();
		valid = isNotMonthQuiz(month);
	}
	return month;
}

static boolean isNotMonthQuiz(String month) {
	try {
		if (Integer.parseInt(month) < 13 && Integer.parseInt(month) > 0)  {
			return false;
		}
		else {
			System.out.println("Please enter a valid choice");	
			return true;
		}
	}
	catch (Exception io) {
			System.out.println("Please enter a valid choice");	
			return true;
	}
	
}

static boolean addRetake(String ID, String location, String month, String day, String hour, String minute) {
	   try {
		      File file = new File("/Users/Bryan/Desktop/quizretakes/quiz-retakes-swe437.xml");
		         if(!file.exists())
		         {
		            file.createNewFile();
		         }
		         FileWriter     fw = new FileWriter(file.getAbsoluteFile(), true); //append mode
		         BufferedWriter bw = new BufferedWriter(fw);
		         RandomAccessFile removeLastLine = new RandomAccessFile("/Users/Bryan/Desktop/quizretakes/quiz-retakes-swe437.xml", "rw");
	             long length = removeLastLine.length();
	             removeLastLine.setLength(length - 10);
	             removeLastLine.close();
		         bw.write(" \n" + "<retake>\n" + 
		         		"    <id>" + ID + "</id>\n" + 
		         		"    <location>" + location + "</location> <!-- Prof's office hours -->\n" + 
		         		"    <dateGiven> <!-- Same format as in quiz-orig.xml -->\n" + 
		         		"      <month>"+ month + "</month> <!-- 01..12 -->\n" + 
		         		"      <day>" + day + "</day> <!-- 1..31 -->\n" + 
		         		"      <hour>" + hour +"</hour> <!-- 0..23 -->\n" + 
		         		"      <minute>"+ minute+ "</minute> <!--  0-59 -->\n" + 
		         		"    </dateGiven>\n" + 
		         		"  </retake>\n" + 
		         		"</retakes>");
		         bw.flush();
		         bw.close();
		         return true;
		 }
		 catch(IOException e) {
		          System.out.println("failure");
		          return false;
		 }  
}

static boolean addQuiz(String ID, String month, String day, String hour, String minute) {
	   try {
		      File file = new File("/Users/Bryan/Desktop/quizretakes/quiz-orig-swe437.xml");
		         if(!file.exists())
		         {
		            file.createNewFile();
		         }
		         FileWriter     fw = new FileWriter(file.getAbsoluteFile(), true); //append mode
		         BufferedWriter bw = new BufferedWriter(fw);
		         RandomAccessFile removeLastLine = new RandomAccessFile("/Users/Bryan/Desktop/quizretakes/quiz-orig-swe437.xml", "rw");
	             long length = removeLastLine.length();
	             removeLastLine.setLength(length - 10);
	             removeLastLine.close();
		         bw.write("\n <quiz>\n" + 
		         		"    <id>" + ID + "</id>\n" + 
		         		"    <dateGiven> <!-- Same format as in quiz-orig.xml -->\n" + 
		         		"      <month>"+ month + "</month> <!-- 01..12 -->\n" + 
		         		"      <day>" + day + "</day> <!-- 1..31 -->\n" + 
		         		"      <hour>" + hour +"</hour> <!-- 0..23 -->\n" + 
		         		"      <minute>"+ minute+ "</minute> <!--  0-59 -->\n" + 
		         		"    </dateGiven>\n" + 
		         		"  </quiz>\n" +
		         		"</quizzes>");
		         bw.flush();
		         bw.close();
		         return true;
		 }
		 catch(IOException e) {
		          System.out.println("failure");
		          return false;
		  }  
}



// ===============================================================
// Get the course ID from the user
private static String readCourseID(Scanner sc) /* CLI */
{
   System.out.print("Enter courseID: "); /* CLI */
   return(sc.next()); /* CLI */
}

// ===============================================================
// Read the course file
static courseBean readCourseFile(String courseID) throws Exception /* CLI */
{
   courseBean course; /* CLI */
   courseReader cr = new courseReader(); /* CLI */
   course          = cr.read(courseFileName);
   return(course); /* CLI */
}

// ===============================================================
// Read the quizzes file
static quizzes readQuizzes(String courseID) throws Exception /* CLI */
{
   quizzes quizList = new quizzes(); /* CLI */
   quizReader qr    = new quizReader(); /* CLI */
   quizList         = qr.read(quizzesFileName); /* CLI */
   return(quizList); /* CLI */
}

// ===============================================================
// Read the retakes file
static retakes readRetakes(String courseID) throws Exception /* CLI */
{
   retakes retakesList = new retakes();
   retakesReader rr    = new retakesReader();
   retakesList         = rr.read(retakesFileName);
   return(retakesList); /* CLI */
}

} // end quizschedule class
