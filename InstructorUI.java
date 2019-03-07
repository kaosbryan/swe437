package quizretakes;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InstructorUI {
	quizsched quizSchedule;
	ArrayList<String> list;
	ArrayList<String> choiceList;

	@Test 
	public void welcomeBorder() {
		quizSchedule = new quizsched();
		list = new ArrayList<String>();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		PrintStream data = System.out;
		System.setOut(new PrintStream(stream));
		quizSchedule.welcome();
		System.setOut(data);
		String output = new String(stream.toByteArray());
		for (String out: output.split("\n")) {
			list.add(out);
		}
		assertEquals(list.get(0),"******************************************************************************");
	}	
	
	@Test 
	public void instructorViewTitle() {
		quizSchedule = new quizsched();
		list = new ArrayList<String>();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		PrintStream data = System.out;
		System.setOut(new PrintStream(stream));
		quizSchedule.welcome();
		System.setOut(data);
		String output = new String(stream.toByteArray());
		for (String out: output.split("\n")) {
			list.add(out);
		}
		assertEquals(list.get(1),"           Instructor View to add and drop retakes and quizzes");
	}	
	
	@Test 
	public void welcomeClosingBorder() {
		quizSchedule = new quizsched();
		list = new ArrayList<String>();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		PrintStream data = System.out;
		System.setOut(new PrintStream(stream));
		quizSchedule.welcome();
		System.setOut(data);
		String output = new String(stream.toByteArray());
		for (String out: output.split("\n")) {
			list.add(out);
		}
		assertEquals(list.get(2),"******************************************************************************");
	}	
	
	@Test
	public void addRetakeOption() {
		quizsched quizSchedule = new quizsched();
		ArrayList<String> choiceList = new ArrayList<String>();
		ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
		PrintStream data1 = System.out;
		System.setOut(new PrintStream(stream1));
		quizSchedule.printOptions();
		System.setOut(data1);
		String output1 = new String(stream1.toByteArray());
		for (String out: output1.split("\n")) {
			choiceList.add(out);
		}
		assertEquals(choiceList.get(0), "Type 1 to add a retake");
	}
	
	@Test
	public void addQuizOption() {
		quizsched quizSchedule = new quizsched();
		ArrayList<String> choiceList = new ArrayList<String>();
		ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
		PrintStream data1 = System.out;
		System.setOut(new PrintStream(stream1));
		quizSchedule.printOptions();
		System.setOut(data1);
		String output1 = new String(stream1.toByteArray());
		for (String out: output1.split("\n")) {
			choiceList.add(out);
		}
		assertEquals(choiceList.get(1), "Type 2 to add a quiz");
	}
	
	@Test
	public void exitOption() {
		quizsched quizSchedule = new quizsched();
		ArrayList<String> choiceList = new ArrayList<String>();
		ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
		PrintStream data1 = System.out;
		System.setOut(new PrintStream(stream1));
		quizSchedule.printOptions();
		System.setOut(data1);
		String output1 = new String(stream1.toByteArray());
		for (String out: output1.split("\n")) {
			choiceList.add(out);
		}
		assertEquals(choiceList.get(2), "Type 3 to exit out of Instructor View");
	}
	
	@Test
	public void validMonthQuizBoundary() {
		assertTrue(quizsched.isNotMonthQuiz("-1"));
	}
	
	@Test
	public void validMonthQuizBoundary2() {
		assertTrue(quizsched.isNotMonthQuiz("13"));
	}
	
	@Test
	public void validMonthRetakeBoundary() {
		assertTrue(quizsched.isNotMonthRetake("00"));
	}
	
	@Test
	public void validMonthRetakeBoundary2() {
		assertTrue(quizsched.isNotMonthRetake("13"));
	}
	
	@Test
	public void validDayBoundary() {
		assertTrue(quizsched.isNotDay("0"));
	}
	
	@Test
	public void validDayBoundary2() {
		assertTrue(quizsched.isNotDay("32"));
	}
	
	@Test
	public void validHourBroundary() {
		assertTrue(quizsched.isNotHour("00"));
	}
	
	@Test
	public void validHourBroundary1() {
		assertTrue(quizsched.isNotHour("-1"));
	}
	
	@Test
	public void validMinuteBroundary() {
		assertTrue(quizsched.isNotMinute("-1"));
	}
	
	@Test
	public void validMinuteBroundary1() {
		assertTrue(quizsched.isNotMinute("60"));
	}
	
	@Test
	public void addRetake() {
		quizsched quizSchedule = new quizsched();
		String id = "34";
		String location = "eng 4430";
		String month = "04";
		String day = "20";
		String hour = "1";
		String minute = "30";
		assertTrue(quizSchedule.addRetake(id, location, month, day, hour, minute));
	}
	
	@Test
	public void addQuiz() {
		quizsched quizSchedule = new quizsched();
		String id = "13";
		String month = "4";
		String day = "20";
		String hour = "1";
		String minute = "30";
		assertTrue(quizSchedule.addQuiz(id, month, day, hour, minute));
	}
	
	@Test
	public void switchExit() {
		quizsched quizSchedule = new quizsched();
		assertFalse(quizSchedule.switchChoices("3"));
	}
	@Test
	public void switchDefault() {
		quizsched quizSchedule = new quizsched();
		assertTrue(quizSchedule.switchChoices("4"));
	}
}
