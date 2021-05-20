
import java.util.*;
import java.io.*;

/**
 * ex. 12
 * @author Leonardo
 * 18 05 2021
 */
public class esame1 {

    public static class Exam {

        private final String course;
        private final int credits;
        private int mark;
        private final String lecturer;
            
        public Exam(String course, int credits, int mark, String lecturer){
            this. course = course;
            this.credits = credits;
            this.mark = mark;
            this.lecturer = lecturer;
        }
        
        public String getCourse() {
            return this.course;
        }
        public int getCredits() {
            return this.credits;
        }
        public int getMark() {
            return this.mark;
        }
        public String getLecturer() {
            return this.lecturer;
        }
        public void changeMark(int n) {
            this.mark = n;
        }
        @Override
        public String toString() {
            String themark = Integer.toString(this.mark);
            if (this.mark == 31)
                themark = "30L";
            
            return "- " 
                    + this.course 
                    + " [" + this.credits 
                    + "] [" + this.lecturer 
                    + "]: " + themark;
        }
    }
    public static class Person {
        protected String name;
        protected String surname;

        public String getName() {
            return this.name;
        }
        public String getSurname() {
            return this.surname;
        }
    }
    public static class Student extends Person {

        private final String serialNumber;
        private ArrayList<Exam> exams = new ArrayList<Exam>();
        
        public Student(String name, String surname, String serialNumber){
            this.name = name;
            this.surname = surname;
            this.serialNumber = serialNumber;
        }
        
        public String getSerialNumber() {
            return serialNumber;
        }
        public ArrayList<Exam> getExamList() {
            return exams;
        }
        public void addExam(Exam e) {
            exams.add(e);
        }
        public void printExamList() {
            for (Exam e : this.exams) {
                System.out.println(e);
            }
        }
        public int getNumberCreditsAcquired() {
            int tot = 0;
            for (Exam e : this.exams) {
                if (e.getMark() > 0) {
                    tot += e.getCredits();
                }
            }
            return tot;
        }
        public boolean checkGraduationEligibility() {
            // generic function, useless in the context of ex12
            if (this.getNumberCreditsAcquired() < 120) {
                return false;
            }

            boolean checkfis = false;
            boolean checkinf = false;
            for (Exam e : this.exams) {
                if (e.getCourse().equals("Fisica")) {
                    checkinf = true;
                } else if (e.getCourse().equals("Informatica")) {
                    checkfis = true;
                }
            }

            return checkfis && checkinf;
        }
        public float getArithmeticAverage() {
            int tot = 0;
            int totmark = 0;
            for (Exam e : this.exams) {
                if (e.getMark() < 18) {
                    System.err.println("non-valid mark found for exam " + e);
                } else {
                    tot++;
                }
                totmark += e.getMark();
            }
            if (tot == 0) {
                return 0;
            }
            return (float) totmark / tot;
        }
        public float getWeightedAverage() {
            int temp = 0;
            for (Exam e : this.exams) {
                if (e.getMark() < 18) {
                    System.err.println("non-valid mark found for exam " + e);
                } else {
                    temp += e.getMark() * e.getCredits();
                }
            }
            if (this.getNumberCreditsAcquired() <= 0) {
                return 0;
            }
            return (float) temp / this.getNumberCreditsAcquired();
        }
        @Override
        public String toString(){
            
            String aravg = String.format("%.2f", this.getArithmeticAverage());
            String weavg = String.format("%.2f", this.getWeightedAverage());
            aravg = aravg.replace(".",",");
            weavg = weavg.replace(".",",");
            
            return this.getName() + " " + this.getSurname() 
                    + " [" + this.getSerialNumber()
                    + "] [" + aravg
                    + "] [" + weavg
                    + "] [" + this.getNumberCreditsAcquired()
                    + "] [" + this.checkGraduationEligibility() + "]";
        }
    }
    public static Student[] readFromFile(File f){
        Student[] data = null;
        Scanner s;
        try{
            s = new Scanner(f);
            int n = Integer.parseInt(s.nextLine());
            data = new Student[n];
            
            for(int i = 0; s.hasNextLine(); i++){
                String thisline = s.nextLine();
                String[] input = thisline.split(";");
                Student st = new Student(input[0],input[1],input[2]);
                
                for (int j = 3; j < input.length; j++){
                    String[] factored = input[j].split("-");
                    Exam e = new Exam (factored[0], Integer.parseInt(factored[1]), 
                            Integer.parseInt(factored[2]), factored[3] + " " + factored[4]);
                    st.addExam(e);
                }
                data[i] = st;
            }
            s.close();
        } catch(Exception e){
            System.out.println("ERROR OCCURRED WHILE READING FILE:");
            System.out.println(e);
            e.printStackTrace();
        }
        return data;
        
    }
    public static void printStudentData(Student[] data){
        for(Student st  : data){
            System.out.println(st);
            for (Exam e : st.getExamList())
                System.out.println(e);
            System.out.println("");
        }
    }
    public static Student[] fixMarks(Student[] data, String exam, int fix4error){
        // nel caso del nostro esercizio exam = Programmazione, fix4error = +1
        for (Student st : data){
            for (Exam e : st.getExamList()){
                if (e.course.equals(exam) && e.getMark() < 31){
                    e.changeMark(e.getMark()+fix4error);
                }
            }
        }
        return data;
    }
    public static void saveStudentData(Student[] data, File f){
        FileWriter w;
        try{
            w = new FileWriter(f);
            for(Student std : data){
                w.write(std.toString());
                for (Exam e : std.getExamList())
                    w.write(e.toString());
            }
        }catch(Exception e){
            System.out.println("ERROR OCCURRED WHILE WRITING ON FILE:");
            System.out.println(e);
            e.printStackTrace();
        }
        
    }
    public static void metodoScarso() throws Exception{
        throw new Exception();
    }
    
    
    public static void main(String[] args) {
        Student[] data = readFromFile(new File("C:/Users/Leonardo/Desktop/input.txt"));
        fixMarks(data, "Programmazione", +1);
        printStudentData(data);
        saveStudentData(data, new File("C:/Users/Leonardo/Desktop/output2.txt"));
    }
}
