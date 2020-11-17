package Boundary;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import Controller.CourseController;
import Controller.FileManager;
import Controller.StudentController;
import Model.*;

public class AdminUI {
	public static void adminMenu(Admin admin) {
		StudentController stuCtrl = new StudentController();
		CourseController courseCtrl = new CourseController();
		
		int choice = 0;
		boolean validInput = false;
		String matric = "";
		LocalDateTime start = LocalDateTime.now();
		LocalDateTime end = LocalDateTime.now();
		LocalTime lessonStart = LocalTime.now();
		LocalTime lessonEnd = LocalTime.now();
		
		Scanner sc = new Scanner(System.in);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

		do {
			System.out.println("1. Edit student access period");
			System.out.println("2. Add a student (name, matric number, gender, nationality, etc)");
			System.out.println("3. Add/Update a course (course code, school, its index numbers and vacancy)");
			System.out.println("4. Check available slot for an index number (vacancy in a class)");
			System.out.println("5. Print student list by index number.");
			System.out.println("6. Print student list by course (all students registered for the selected course)");
			System.out.println("7. Logout");

			
			do {
				try {
					choice = sc.nextInt();
				if (choice >= 1) {
					validInput = true;
					sc.nextLine();
					}
				} catch (InputMismatchException e) {
					System.out.println("Enter a valid integer!");
					sc.nextLine();
				}
			} while (!validInput);
			validInput = false;

			switch (choice) {
			case 1:
				validInput = false;
				start = LocalDateTime.now();
				end = LocalDateTime.now();
				do {
					System.out.println("Enter student's matric number: ");
					matric = sc.nextLine();
					if(stuCtrl.checkMatricExists(matric) == true){
						validInput = true;
					}
					else {
						System.out.println("Student does not exist! Please enter a valid matric number!");
					}
				}
				while(!validInput);
				
				validInput = false;
				do {
					try {
						System.out.println("Enter start date time for access(YYYY-MM-DD HH:MM): ");
						start = LocalDateTime.parse(sc.nextLine(), formatter);
						validInput = true;
					}
					catch(DateTimeParseException e) {
						System.out.println("Please enter the valid date time format!");
					}
				}while(!validInput);
				
				validInput = false;
				do {
					try {
						System.out.println("Enter end date time for access(YYYY-MM-DD HH:MM): ");
						end = LocalDateTime.parse(sc.nextLine(), formatter);
						validInput = true;
					}
					catch(DateTimeParseException e) {
						System.out.println("Please enter the valid date time format!");
					}
					
				}while(!validInput);
				
				stuCtrl.editAccessPeriod(matric, start, end);
				System.out.println("Access period successfully edited!");
				
				break;
			case 2:
				start = LocalDateTime.now();
				end = LocalDateTime.now();
				
				System.out.println("Enter student's matric number: ");
				matric = sc.nextLine();
				System.out.println("Enter student's name: ");
				String name = sc.nextLine();
				System.out.println("Enter student's username: ");
				String username = sc.nextLine();
				System.out.println("Enter student's password: ");
				String password = sc.nextLine();
				System.out.println("Enter student's email: ");
				String email = sc.nextLine();
				System.out.println("Enter student's gender: (Male/Female)");
				String gender = sc.nextLine();
				System.out.println("Enter student's nationality: ");
				String nationality = sc.nextLine();
				
				validInput = false;	
				do {
					try {
						System.out.println("Enter start date time for access(YYYY-MM-DD HH:MM): ");
						start = LocalDateTime.parse(sc.nextLine(), formatter);
						validInput = true;
					}
					catch(DateTimeParseException e) {
						System.out.println("Please enter the valid date time format!");
					}
				}while(!validInput);
				
				validInput = false;
				do {
					try {
						System.out.println("Enter end date time for access(YYYY-MM-DD HH:MM): ");
						end = LocalDateTime.parse(sc.nextLine(), formatter);
						validInput = true;
					}
					catch(DateTimeParseException e) {
						System.out.println("Please enter the valid date time format!");
					}
					
				}while(!validInput);
				
				Student stu = new Student(username, password, email, name, matric, gender, nationality, start, end);
				
				//Check if student exists
				if(stuCtrl.checkStudentExists(stu) == true) {
					System.out.println("Student already exists! Please enter fields again!");
				}
				else{
					//Check if matric already exists
					if(stuCtrl.checkMatricExists(matric) == true) {
						System.out.println("Matric number already exists! Please enter fields again!");
					}
					else {
						if(stuCtrl.checkUserNameExists(username) == true) {
							System.out.println("Username already exists! Please enter fields again!");
						}
						else {
							//Check if email exists
							if(stuCtrl.checkEmailExists(email) == true) {
								System.out.println("Email already exists! Please enter fields again!");
							}
							else {
								//Check if gender was entered correctly
								if(gender.equals("Female") || gender.equals("Male")) {
									stuCtrl.addStudent(stu);
									System.out.println("Student successfully added!");	
								}
								else {
									System.out.println("Please enter correct format for gender(Male/Female)! Please enter fields again!");
								}
							}
						}
					}
				}
				

				break;
			case 3:
				System.out.println("1) Add a new course\n" +
								"2) Delete a course\n" +
								"3) Update a course");
				int courseChoice = sc.nextInt();
				sc.nextLine();
				switch (courseChoice) {
					case 1:
						System.out.println("Enter new Course Code:");
						String courseCode = sc.nextLine();
						
						if (courseCtrl.checkCourse(courseCode) == true) {
							System.out.println("This Course Code already exists");
							break;
						}
						else {
							System.out.println("Enter the Course name:");
							String courseName = sc.nextLine();
							
							System.out.println("Enter Course School:");
							String school = sc.nextLine();
							
							System.out.println("Enter number of AUs:");
							int au = sc.nextInt();
							
							courseCtrl.addCourse(courseCode, courseName, school, au);
							System.out.println("New Course successfully added!");
							System.out.println("All Courses(Course Code, Name, School, AU): ");
							HashMap<String,Course> allCourses = new HashMap<>();
							allCourses = courseCtrl.getAllCourses();
							for(Map.Entry<String, Course> entry : allCourses.entrySet()){
								System.out.println(entry.getValue().getCourseCode() + "	" + entry.getValue().getCourseName() 
										+ "	" + entry.getValue().getSchool() + "	" + entry.getValue().getAu());
							}
							
						}
						
						break;

					case 2:
						System.out.print("Enter Course Code to be deleted:");
						String delCourseCode = sc.next();
						if (courseCtrl.checkCourse(delCourseCode) == false) {
							System.out.println("This Course Code does not exists");
							return;
						}
						courseCtrl.delCourse(delCourseCode);
						System.out.println("Course successfully deleted!");
						break;


					case 3:
						System.out.print("Enter Course Code to update:");
						String updateCourseCode = sc.next();
						if (courseCtrl.checkCourse(updateCourseCode)) {
							System.out.println("Choose what to update: \n" +
									"1)Course Code\n" +
									"2)School\n" +
									"3)Index Numbers\n" +
									"4)Vacancy\n");

							switch (sc.nextInt()) {
								case 1:
									System.out.print("Enter new Course Code:");
									courseCtrl.updateCourseCode(updateCourseCode, sc.next());
									System.out.println("Course Code successfully updated!");
									break;

								case 2:

									System.out.print("Enter new School:");
									courseCtrl.updateCourseSchool(updateCourseCode, sc.next());
									System.out.println("School successfully updated!");
									break;

								case 3:
									System.out.println("Do you want to: \n" +
											"1)Update an index's number\n" +
											"2)Add an index\n"+
											"3)Add a lesson\n");

									switch (sc.nextInt()) {
										case 1:
											System.out.print("Enter the index you wish to change: ");
											int indexChange = sc.nextInt();
											if (courseCtrl.checkIndex(indexChange) == false) {
												System.out.println("This index does not exist.");
												break;
											}
											System.out.print("Enter the new index: ");
											int newChange = sc.nextInt();
											courseCtrl.updateIndex(indexChange, newChange);
											System.out.println("Index successfully changed!");
											break;

										case 2:
											System.out.print("Enter the index you wish to add: ");
											int indexNum = sc.nextInt();
											if (courseCtrl.checkIndex(indexNum) == true) {
												System.out.println("This index already exists.");
												break;
											}

											System.out.print("Enter the Vacancy: ");
											int vacancyInt = sc.nextInt();
											courseCtrl.newIndex(indexNum, updateCourseCode, vacancyInt);
											System.out.println("New Index successfully added!");											
											break;
											
										case 3:
											System.out.println("Enter index number: ");
											int lessonIndex = sc.nextInt();
											sc.nextLine();
											System.out.println("Enter lesson type: ");
											String lessonType = sc.nextLine();
											System.out.println("Enter lesson day: ");
											String lessonDay = sc.nextLine();
											System.out.println("Enter lesson venue: ");
											String lessonVenue = sc.nextLine();
											validInput = false;	
											do {
												try {
													System.out.println("Enter lesson start time(HH:MM): ");
													lessonStart = LocalTime.parse(sc.nextLine(), timeFormatter);
													validInput = true;
												}
												catch(DateTimeParseException e) {
													System.out.println("Please enter the valid date time format!");
												}
											}while(!validInput);
											
											validInput = false;
											do {
												try {
													System.out.println("Enter lesson end time(HH:MM): ");
													lessonEnd = LocalTime.parse(sc.nextLine(), timeFormatter);
													validInput = true;
												}
												catch(DateTimeParseException e) {
													System.out.println("Please enter the valid date time format!");
												}
												
											}while(!validInput);
											
											Lesson le = new Lesson(lessonIndex, lessonStart, lessonEnd, lessonDay, lessonType, lessonVenue);
											courseCtrl.addLesson(le);
											System.out.println("Lesson successfully added!");
											
											break;
									}
									break;

								case 4:
									System.out.print("Enter the index which vacancy you wish to change: ");
									int indexChange = sc.nextInt();
									System.out.print("Enter the new vacancy: ");
									int newChange = sc.nextInt();
									courseCtrl.updateVacancy(indexChange, newChange);
									System.out.println("Vacancy successfully changed.");
									break;

							}

						}
						else
							System.out.println("Course Code does not exist");
						break;
				}

				break;

			case 4:
				System.out.print("Enter the index of which you like to check vacancy for:");
				int index = sc.nextInt();
				int vacance= courseCtrl.checkVacant(index);
				if (vacance==-1){
					System.out.println("This index does not exist.");
				}
				else{
					System.out.println("This index has vacancy of "+vacance+" students.");
				}

				break;
			case 5:
				System.out.print("Enter Index number: ");
				int indexNum = sc.nextInt();
				if (courseCtrl.checkIndex(indexNum)==false){
					System.out.println("Index does not exist");
					break;
				}
				ArrayList<Student> nomRoll1 = stuCtrl.checkStudentsInIndex(indexNum);
				System.out.println("Student List for Index "+indexNum+" (Name, Gender, Nationality)");
				if (nomRoll1.size()==0){
					System.out.println("Empty");
					break;
				}
				for (int i=0;i<nomRoll1.size();i++){
					System.out.println((i+1)+") "+nomRoll1.get(i).getName()+", " + nomRoll1.get(i).getGender()+", " + nomRoll1.get(i).getNationality() );
				}
				System.out.println("");

				break;
			case 6:
				System.out.print("Enter Course code: ");
				String courseCode = sc.next();
				if (courseCtrl.checkCourse(courseCode)==false){
					System.out.println("Course does not exist");
					break;
				}
				ArrayList<Student> nomRoll2=stuCtrl.checkStudentsInCourse(courseCode);
				System.out.println("Student List for Course Code "+courseCode+" (Name, Gender, Nationality)");
				if (nomRoll2.size()==0){
					System.out.println("Empty");
					break;
				}
				for (int i=0;i<nomRoll2.size();i++){
					System.out.println((i+1)+") "+nomRoll2.get(i).getName()+", " + nomRoll2.get(i).getGender()+", " + nomRoll2.get(i).getNationality() );
				}
				System.out.println("");

				break;
			case 7:

				break;
			default:
				System.out.println("");
				break;
			}
		} while (choice > 0 && choice < 7);
		
	}
}
