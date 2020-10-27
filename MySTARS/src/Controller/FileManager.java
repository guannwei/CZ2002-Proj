package Controller;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import Model.*;

public class FileManager {
	public static final String SEPARATOR = ",";

	public static ArrayList readAdmin() throws IOException {
		String filename = "data/user-admin.txt" ;
		// read String from text file
		ArrayList stringArray = (ArrayList)read(filename);
		ArrayList alr = new ArrayList() ;

        for (int i = 0 ; i < stringArray.size() ; i++) {
				String st = (String)stringArray.get(i);
				StringTokenizer star = new StringTokenizer(st , SEPARATOR);	
				String  username = star.nextToken().trim();	
				String  password = star.nextToken().trim();	
				Admin admin = new Admin(username,password);
				alr.add(admin) ;
			}
			return alr ;
	}
	public static ArrayList readStudents() throws IOException {
		String filename = "data/user-student.txt" ;
		ArrayList stringArray = (ArrayList)read(filename);
		ArrayList alr = new ArrayList() ;

        for (int i = 0 ; i < stringArray.size() ; i++) {
				String st = (String)stringArray.get(i);
				StringTokenizer star = new StringTokenizer(st , SEPARATOR);	
				String  username = star.nextToken().trim();	
				String  password = star.nextToken().trim();	
				String  email = star.nextToken().trim();	
				String  name = star.nextToken().trim();	
				String  matricNumber = star.nextToken().trim();	
				String  gender = star.nextToken().trim();	
				String  nationality = star.nextToken().trim();	
				LocalDateTime accessStartPeriod = LocalDateTime.parse(star.nextToken().trim());
				LocalDateTime accessEndPeriod = LocalDateTime.parse(star.nextToken().trim());
				Student student = new Student(username, password, email,name,matricNumber,gender,nationality,accessStartPeriod,accessEndPeriod);
				alr.add(student) ;
			}
			return alr ;
	}

	public static void saveStudent(List al) throws IOException {
			String filename = "data/user-student.txt" ;
			List alw = new ArrayList() ;
	        for (int i = 0 ; i < al.size() ; i++) {
					Student student = (Student)al.get(i);
					StringBuilder st =  new StringBuilder() ;
					st.append(student.getUsername().trim());
					st.append(SEPARATOR);
					st.append(student.hashPassword(student.getPassword()).trim());
					st.append(SEPARATOR);
					st.append(student.getEmail().trim());
					st.append(SEPARATOR);
					st.append(student.getName().trim());
					st.append(SEPARATOR);
					st.append(student.getMatricNumber().trim());
					st.append(SEPARATOR);
					st.append(student.getGender().trim());
					st.append(SEPARATOR);
					st.append(student.getNationality().trim());
					st.append(SEPARATOR);
					st.append(student.getAccessStartPeriod().toString().trim());
					st.append(SEPARATOR);
					st.append(student.getAccessEndPeriod().toString());
					
					alw.add(st.toString()) ;
				}
				write(filename,alw);
		}
	public static void saveAdmin(List al) throws IOException {
		String filename = "data/user-admin.txt" ;
		List alw = new ArrayList() ;// to store Professors data

        for (int i = 0 ; i < al.size() ; i++) {
				Admin admin = (Admin)al.get(i);
				StringBuilder st =  new StringBuilder() ;
				st.append(admin.getUsername().trim());
				st.append(SEPARATOR);
				st.append(admin.hashPassword(admin.getPassword()).trim());
				
				alw.add(st.toString()) ;
			}
			write(filename,alw);
	}
    public static void write(String fileName, List data) throws IOException  {
        PrintWriter out = new PrintWriter(new FileWriter(fileName));

        try {
    		for (int i =0; i < data.size() ; i++) {
          		out.println((String)data.get(i));
    		}
        }
        finally {
          out.close();
        }
      }
      public static List read(String fileName) throws IOException {
    	List data = new ArrayList() ;
        Scanner scanner = new Scanner(new FileInputStream(fileName));
        try {
          while (scanner.hasNextLine()){
            data.add(scanner.nextLine());
          }
        }
        finally{
          scanner.close();
        }
        return data;
      }

}
    