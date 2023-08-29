package csi213.lab02;

/**
 * A {@code Student} instance represents a student.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 */
public class Student extends Person{

	/**
	 * The number of {@code Student} instances constructed so far.
	 */
	static int studentCount = 0;
	int studentID;

	/**
	 * Constructs a {@code Student} instance.
	 * 
	 * @param name
	 *            the name of the {@code Student}
	 * @param studentID
	 *            the ID of the {@code Student}
	 */
	public Student(String name, int studentID) {
		super(name);
		studentCount++;
		this.studentID = studentID;
	}

	/**
	 * Returns the number of {@code Student} instances constructed so far.
	 * 
	 * @return the number of {@code Student} instances constructed so far
	 */
	public static int studentCount() {
		return studentCount;
	}
	/**
	 * Overrides the toString() method in the Person class
	 */
	public String toString() {
		return name + ", " + studentID;
	}
	/**
	 * The main method of the {@code Student} class.
	 * 
	 * @param args
	 *            the program arguments
	 */
	public static void main(String[] args) {
		new Student("Chloy", 1234); // construct a Student instance whose name is "Chloy" and student ID is 1234
		Student s = new Student("Ken", 2345); // construct a Student instance whose name is "Ken" and student ID is 2345
		System.out.println(s); // output the name and student ID of the last Student instance (i.e., "Ken, 2345"))
		System.out.println(s.studentCount()); // output the number of Student instances constructed so far (i.e., 2)
	}

}
