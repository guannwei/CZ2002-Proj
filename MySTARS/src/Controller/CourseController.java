package Controller;
import Controller.FileManager;
import Model.Course;
import Model.Index;
import Model.Student;
import Model.StudentRegisteredCourses;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;

public class CourseController {
    private HashMap<String,Course> courses;
    private HashMap<Integer, Index> indexes;
    private static FileManager accessFile = new FileManager();
    
    public CourseController( HashMap courses, HashMap indexes ){
        this.courses=courses;
        this.indexes=indexes;
    }
    
    public CourseController() {
    	
    }


    public void updateCourse() throws IOException {
        //scan for Object with courseCode = CourseCode
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Course Code to update:");
        String CourseCode=sc.next();
        if (courses.get(CourseCode)!=null){
            Course course =courses.get(CourseCode);

            System.out.println("Choose what to update: \n" +
                    "1)Course Code\n" +
                    "2)School\n" +
                    "3)Index Numbers\n" +
                    "4)Vacancy\n");

            switch (sc.nextInt()) {
                case 1:
                    System.out.print("Enter new Course Code:");
                    course.setCourseCode(sc.next());
                    System.out.println("Course Code successfully updated!");
                    break;

                case 2:

                    System.out.print("Enter new School:");
                    course.setSchool(sc.next());
                    System.out.println("School successfully updated!");
                    break;

                case 3:
                    System.out.println("Do you want to: \n" +
                            "1)Update an index's number\n" +
                            "2)Add an index\n");

                    switch (sc.nextInt()) {
                        case 1:
                            System.out.print("Enter the index you wish to change: ");
                            String indexChange = sc.next();
                            System.out.print("Enter the new index: ");
                            int newChange = sc.nextInt();
                            indexes.get(indexChange).setIndexNumber(newChange);
                            break;

                        case 2:
                            System.out.print("Enter the index you wish to add: ");
                            int indexNum = sc.nextInt();
                            System.out.print("Enter the Course Code: ");
                            String courseCode = sc.next();
                            System.out.print("Enter the Vacancy: ");
                            int vacancyInt = sc.nextInt();
                            indexes.put(indexNum,new Index(indexNum, courseCode, vacancyInt));
                            break;
                    }
                    break;

                case 4:
                    System.out.print("Enter the index which vacancy you wish to change: ");
                    String indexChange = sc.next();
                    System.out.print("Enter the new vacancy: ");
                    int newChange = sc.nextInt();
                    indexes.get(indexChange).setVacancy(newChange);
                    break;

            }
            FileManager.saveCourse(courses);
            FileManager.saveIndex(indexes);

        }
        else
            System.out.println("Course Code does not exist");





    }


    public void addCourse() throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter new Course Code:");
        String courseCode=sc.next();
        if (courses.get(courseCode)!=null){
            System.out.println("This Course Code already exists");
            return;
        }
        System.out.print("Enter new Course Name:");
        String courseName=sc.next();
        System.out.print("Enter Course School:");
        String school=sc.next();
        courses.put(courseCode,new Course(courseCode,courseName,school));
        FileManager.saveCourse(courses);

        System.out.println("New Course successfully added!");

    }

    public void delCourse() throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Course Code to be deleted:");
        String courseCode=sc.next();
        if (courses.get(courseCode)==null){
            System.out.println("This Course Code does not exists");
            return;
        }

        courses.remove(courseCode);
        FileManager.saveCourse(courses);

        System.out.println("New Course successfully added!");
    }

    public void checkVacant(){
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the index of which you like to check vacancy for:");
        int index = sc.nextInt();
        if (indexes.get(index)==null){
            System.out.println("This index does not exist.");
            return;
        }
        int vacancy=indexes.get(index).getVacancy();
        System.out.println("This index has vacancy of "+vacancy+" students.");
    }


    public void printIndexNomRoll(ArrayList<Student> nomRoll, int index){
        System.out.println("Student List for Index "+index+" (Name, Gender, Nationality)");
        for (int i=0;i<nomRoll.size();i++){
            System.out.println((i+1)+") "+nomRoll.get(i).getName()+", " + nomRoll.get(i).getGender()+", " + nomRoll.get(i).getNationality() );
        }
    }

    public void printCourseNomRoll(ArrayList<Student> nomRoll, String courseName){
        System.out.println("Student List for Course Code "+courseName+" (Name, Gender, Nationality)");
        for (int i=0;i<nomRoll.size();i++){
            System.out.println((i+1)+") "+nomRoll.get(i).getName()+", " + nomRoll.get(i).getGender()+", " + nomRoll.get(i).getNationality() );
        }
    }

    
    
    /* For Students */
    public Boolean checkCourseRegistered(String matric, int index){
    	//Return false if student has not registered for course
    	//Return true if student already registered
    	Boolean exists = false;
    	try {
    		ArrayList<StudentRegisteredCourses> courseList = accessFile.readStudentRegisteredCourses();
    		for(int i = 0; i < courseList.size(); i++) {
    			if(courseList.get(i).getMatricNumber().equals(matric) && courseList.get(i).getIndexNumber() == index) {
    				exists = true;
    			}
    		}	
    	}
    	catch(Exception e) {
    		
    	}
		return exists;
		
    }
    
    public Boolean checkCompleteCourse(String matric, int index){
    	//Return false if student has not completed course
    	//Return true if student already completed course
    	Boolean exists = false;
    	try {
    		ArrayList<StudentRegisteredCourses> courseList = accessFile.readStudentRegisteredCourses();
    		for(int i = 0; i < courseList.size(); i++) {
    			if(courseList.get(i).getMatricNumber().equals(matric) && courseList.get(i).getIndexNumber() == index && courseList.get(i).getComplete() == true) {
    				exists = true;
    			}
    		}
    	}
    	catch(Exception e) {
    		
    	}
		return exists;
		
    }
    
    public ArrayList<Index> allIndexOfCourse(String courseCode) {
    	ArrayList<Index> allindexList = new ArrayList<Index>();
    	ArrayList<Index> indexList = new ArrayList<Index>();
    	try {
    		allindexList = accessFile.readIndexArray();
    		for(int i = 0; i < allindexList.size(); i++) {
    			if(allindexList.get(i).getCourseCode().equals(courseCode)) {
    				indexList.add(allindexList.get(i));
    			}
    		}
    	}
    	catch(Exception e){
    	}	
    	return indexList;
    }
    
    public Boolean checkVacancy(int index) {
    	Boolean vacant = true;
    	ArrayList<Index> allindexList = new ArrayList<Index>();
    	try {
    		allindexList = accessFile.readIndexArray();
    		for(int i = 0; i < allindexList.size(); i++) {
    			if(allindexList.get(i).getIndexNumber() == index) {
    				if(allindexList.get(i).getVacancy() <= 0) {
    					vacant = false;
    				}
    			}
    		}
    	}
    	catch(Exception e) {
    		
    	}
    	return vacant;
    }


	public void changeIndex(String matric, int index, int newIndex) {
	    try {
	    	//Get student registered courses
	    	ArrayList<StudentRegisteredCourses> stuRegCourses = new ArrayList<>();
	    	stuRegCourses = accessFile.readStudentRegisteredCourses();
	    	for(int i = 0; i < stuRegCourses.size(); i++) {
	    		if(stuRegCourses.get(i).getMatricNumber().equals(matric)) {
	    			//Change new index
	    			stuRegCourses.get(i).setIndexNumber(newIndex);
	    		}
	    	}
	    	//Save back into file
	    	accessFile.saveRegisteredCourses(stuRegCourses);
	    }
	    catch(Exception e){
	    		
	    }
	}
	
	public void swopIndex(String ownMatric, String otherMatric, String otherPassword, int ownIndex, int otherIndex) {
		try {
			
		}
		catch(Exception e) {
			
		}
	}


}



