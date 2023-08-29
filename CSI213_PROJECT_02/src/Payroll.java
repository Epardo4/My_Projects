/**
 * Class for running a payroll for employees
 * @author Eli_Pardo
 * Edited 3/4/2023
 */
import java.util.Arrays;
import java.util.Scanner;
public class Payroll {
		private static Employee[] employees = new Employee[100];
		public static void main(String [] args) {
			//Prints out the options menu
			Scanner keyboard = new Scanner(System.in);	
			String option1 = "1)      Create an employee";
			String option2 = "2)      Search for an employee by last name";
			String option3 = "3)	Display an employee by employee number";
			String option4 = "4)	Run payroll";
			String option5 = "5)	Quit";
			String option;
			//Keeps cycling through user's answers until user quits
			while(true) {
				System.out.println(option1 + "\n" + option2 + "\n" + option3 + "\n" + option4 + "\n" + option5);
				option = keyboard.next();
				if(option.charAt(0) == '1')
					createEmployee(); //creates an employee
				else if(option.charAt(0) == '2') {
					searchForEmployee(); //searches for an employee by last name
				}
				else if(option.charAt(0) == '3') {
					displayEmployeeByNumber();	//displays en employee by specified number			
				}
				else if(option.charAt(0) == '4') {
					runPayroll(); //runs payroll
				}
				else if(option.charAt(0) == '5')
					System.exit(0); //quits program
				else
					System.out.println("That was not an option, please choose a different option");	
				}
			}
		/**
		 * Checking that words the use inputs contain only letters, hyphens, and spaces
		 * @param word
		 * @return check
		 */
		public static boolean checkWord(String word) {
			char[] checkCharacters = new char[] {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s',
					't','u','v','w','x','y','z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S',
					'T','U','V','W','X','Y','Z','-',' '};
			boolean check = false;
			for(int i = 0; i < word.length(); i++) {
				check = false;
				for(int j = 0; j < checkCharacters.length; j++) {
					if(checkCharacters[j] == word.charAt(i))
						check = true;
				}
				if(check == false)
					return check;
			}
			return check;
		}
		/**
		 * Creates an employee and adds it to the employees array
		 */
		public static void createEmployee() {
			Scanner keyboard = new Scanner(System.in);
			String firstName = "";
			String lastName = "";
			char option;
			String input;
			boolean check = false;
			//continues to cycle through the input for the first name until the user enters an appropriate name
			while(check == false) {
				System.out.println("What is the employee's first name?");
				firstName = keyboard.nextLine();
				check = checkWord(firstName);
				if(check == false)
					System.out.println("Please enter a valid name with only letters, spaces, or hyphens");
			}
			//continues to cycle through the input for the last name until the user enters an appropriate name
			check = false;
			while(check == false) {
					System.out.println("What is the employee's last name?");
					lastName = keyboard.nextLine();
					check = checkWord(lastName);
					if(check == false)
						System.out.println("Please enter a valid name with only letters, spaces, or hyphens");
			}
			//if user wants to quit, they can quit here and will return back to starting menu
			if(firstName.equals("q") || lastName.equals("q"))
				return;
			//checks the employees type
			System.out.println("What is this employee's type?\n1)    Hourly\n2)    Salaried\n3)    Commissioned?");
			input = keyboard.next();
			option = input.charAt(0);
			check = false;
			//continues to cycle through until user either enters 1, 2, or 3 as the employee type
			while(check == false) {
			switch (option){
				case '1':
					System.out.println("What is the employee's hourly pay rate?");
					employees[getNumOfEmployees()] = new HourlyEmployee(firstName, lastName, keyboard.nextFloat());
					check = true;
					break;
				case '2':
					System.out.println("What is the employee's yearly salary?");
					employees[getNumOfEmployees()] = new SalariedEmployee(firstName, lastName, keyboard.nextFloat());
					check = true;
					break;
				case '3':
					System.out.println("What is the employee's base pay?");
					float basePay = keyboard.nextFloat();
					employees[getNumOfEmployees()] = new CommissionedEmployee(firstName, lastName, basePay, commissionSchedule());
					check = true;
					break;
				default:
					System.out.println("That was not a valid choice please try again.");
					break;
			}
			}
		}
		/**
		 * Creates a commission schedule for an employee of type CommissionedEmployee
		 * @return commissionSchedule
		 */
		public static float[][] commissionSchedule(){
			Scanner keyboard = new Scanner(System.in);
			String input = "";
			int index = 0;
			float itemsSold;
			System.out.println("How long is the commission schedule?");
			int length = keyboard.nextInt();
			float[][] commissionSchedule = new float[2][length];
			//continues to add to the commission schedule until the user quits and commissionSchedule gets returned
			while(true) {
				System.out.println("How many units must be sold for this commission?(to end commission "
						+ "schedule, enter \"q\")");
				input = keyboard.next();
				//adds as number of units that must be sold to the commission schedule
				if(input.charAt(0) != 'q') {
					if(index >= length) {
						System.out.println("Going over the commission schedule length is not allowed, commission schedule is ending");
						return commissionSchedule;
					}
				itemsSold = Float.parseFloat(input);
				commissionSchedule[0][index] = itemsSold;
				}
				else
					return commissionSchedule;
				//adds value per unit sold to the commission schedule
				System.out.println("What is the value for each unit sold?");
				commissionSchedule[1][index] = keyboard.nextFloat();
				index++;
			}
	}
		/**
		 * Used to compare two strings to each other to see which one has a "higher" value
		 * @return an integer
		 */
		public static int compareStrings(String word, String otherWord) {
			int length;
			String word1 = word.toUpperCase();
			String word2 = otherWord.toUpperCase();
			//sets length to be the specified length in the for-loop
			if(word.length() >= otherWord.length())
				length = otherWord.length();
			else
				length = word.length();
			//checks each character of each word until one has a greater value than the other, or they reach the end of length
			for(int i =0; i < length; i++) {
				if(word1.charAt(i) < word2.charAt(i))
					return -1;
				else if(word1.charAt(i) > word2.charAt(i))
					return 1;
			}
			//if not yet returned, the larger word had a greater value
			if(word.length() > otherWord.length())
				return 1;
			else if(word.length() < otherWord.length())
				return -1;
			//if not yet returned, word and otherWord are the same
			return 0;
		}
		/**
		 * Sorts employees by last name
		 */
		public static void quickSortLastName(Employee[] employ, int low, int high) {
			int first = low;
			int pivot = high;
			Employee temporary;
			//continues to execute until there is only one employee that low and high are referencing
			while(low < high) {
				while(low < high && compareStrings(employ[low].getLastName(), employ[pivot].getLastName()) <= 0){
					low++;
				}
				while(high > low && compareStrings(employ[high].getLastName(), employ[pivot].getLastName()) >= 0){
					high--;
				}
				//if low and high are pointing to the same employee, swap the employee at low and pivot
				if(low >= high) {
					temporary = employ[pivot];
					employ[pivot] = employ[low];
					employ[low] = temporary;
				}
				//if low and high are referencing to different employees, swap the employee at low and high
				else {
					temporary = employ[low];
					employ[low] = employ[high];
					employ[high] = temporary;
				}
				quickSortLastName(employ, first, low - 1);
				quickSortLastName(employ, high, pivot);
			}
		}
		/**
		 * Sorts employees by first name
		 */
		public static void quickSortFirstName(Employee[] employ, int low, int high) {
			int first = low;
			int pivot = high;
			Employee temporary;
			//continues to execute until there is only one employee that low and high are referencing
			while(low < high) {
				while(low < high && compareStrings(employ[low].getFirstName(), employ[pivot].getFirstName()) <= 0){
					low++;
				}
				while(high > low && compareStrings(employ[high].getFirstName(), employ[pivot].getFirstName()) >= 0){
					high--;
				}
				//if low and high are pointing to the same employee, swap the employee at low and pivot
				if(low >= high) {
					temporary = employ[pivot];
					employ[pivot] = employ[low];
					employ[low] = temporary;
				}
				//if low and high are referencing to different employees, swap the employee at low and high
				else {
					temporary = employ[low];
					employ[low] = employ[high];
					employ[high] = temporary;
				}
				quickSortFirstName(employ, first, low - 1);
				quickSortFirstName(employ, high, pivot);
			}
		}
		/**
		 * Is built to call search to the searchLastName() method
		 */
		public static void searchForEmployee() {
			Scanner keyboard = new Scanner(System.in);
			String lastName = "";
			boolean check = false;
			//repeats until the user quits and returns to the main menu or enters a last name with only letters, hyphens, and spaces
			while(check == false) {
				System.out.println("What last name would you like to search for? (To end, enter \"q\")");
				lastName = keyboard.nextLine();
				if(lastName.equals("q"))
					return;
				check = checkWord(lastName);
				if(check == false)
					System.out.println("Please enter a valid name with only letters, spaces, or hyphens");					
			}
			searchLastName(lastName);
		}
		/**
		 * Searches for the employees with the specified last name and displays them in alphabetical order
		 */
		public static void searchLastName(String lastName) {
			int max = getNumOfEmployees();
			//quick sorts by last name
			quickSortLastName(employees, 0, max);
			int start = 0;
			//sets low and high variables for the first name quick sort
			for(int last = 0; last <= max; last++) {
				if(employees[last].getLastName().equals(employees[start].getLastName()))
					quickSortFirstName(employees, start, last);
				else
					start = last;
			}
			//displays names with specified last name in alphabetical order
			for(int i = 0; i <= max; i++) {
				if(employees[i].getLastName().equals(lastName))
					System.out.printf("\n%s %s Employee %d\r", employees[i].getFirstName(),
							employees[i].getLastName(), employees[i].getEmployeeNumber());
			}
		}
		/**
		 * Sorts employees by employee number
		 */
		public static void quickSortNumber(Employee[] employ, int low, int high) {
			int first = low;
			int pivot = high;
			Employee temporary;
			//continues to loop until low and high referencing the same employee
			while(low < high) {
				while(low < high && employ[low].getEmployeeNumber() <= employ[pivot].getEmployeeNumber())
					low++;
				while(high > low && employ[high].getEmployeeNumber() >= employ[pivot].getEmployeeNumber())
					high--;
				//if low and high are referencing the same employee, swap employee at low with employee at pivot
				if(low >= high) {
					temporary = employ[pivot];
					employ[pivot] = employ[low];
					employ[low] = temporary;
				}
				//if low and high are referencing different employees, swap the employee at low with the employee at high
				else {
					temporary = employ[low];
					employ[low] = employ[high];
					employ[high] = temporary;
				}
				quickSortNumber(employ, first, low - 1);
				quickSortNumber(employ, high, pivot);
			}
		}
		/**
		 * Is built to check that the number is acceptable and then calls the findEmployeeByNumber() method using that number
		 */
		public static void displayEmployeeByNumber() {
			Scanner keyboard = new Scanner(System.in);
			int max = getNumOfEmployees();
			boolean check = false;
			String input = "";
			int employeeNumber = 0;
			//continues to ask for input for an employee number until the user either quits or it is a valid number
			while(check == false) {
				System.out.println("What employee number would you like to search for?(To end, enter \"q\")");
				input = keyboard.nextLine();
				if(input.equals("q"))
					return;
				if(checkNumbers(input) == false)
					System.out.println("That was not a valid option, please try again.");
				else {
				employeeNumber = Integer.parseInt(input);
				//checks that the employee number is between the given numbers
				if(employeeNumber >= 0 && employeeNumber < max)
					check = true;
				else
					System.out.println("That was not a valid entry, please try again.");
				}
			}
			findEmployeeByNumber(employeeNumber);
			}
		/**
		 * Checks if numbers inputed by the user are correct
		 * @param number
		 * @return check
		 */
		public static boolean checkNumbers(String number) {
			//all characters that can be in letters
			char[] checkNumbers = {'0','1','2','3','4','5','6','7','8','9','.'};
			boolean check = false;
			//continues to loop through the given number to see if it is one of the characters
			for(int i = 0; i < number.length(); i++) {
				check = false;
				for(int j = 0; j < checkNumbers.length; j++) 
					if(checkNumbers[j] == number.charAt(i))
						check = true;
				if(check == false)
					return check;
			}
			return check;
		}
		/**
		 * finds the employee by the given number by calling the binary search method
		 * @param employeeNumber
		 */
		public static void findEmployeeByNumber(int employeeNumber) {
			int length = getNumOfEmployees();
			//sorts the employees by number
			quickSortNumber(employees, 0, length-1);
			//searches for the employee by their number
			int index = searchEmployeeNumber(employees, 0, length, employeeNumber);
			System.out.println(employees[index].toString());
			}
		/**
		 * A binary search algorithm to find the employee by using the given number
		 * @param employ
		 * @param lower
		 * @param upper
		 * @param searchFor
		 * @return halfWay
		 */
		public static int searchEmployeeNumber(Employee[] employ, int lower, int upper, int searchFor) {
			if(lower > upper)
				return -1;
			//index of the point halfway through the array
			int halfWay = (lower + upper)/2;
			//checks which side of the array to search for
			if(employ[halfWay].getEmployeeNumber() > searchFor)
				return searchEmployeeNumber(employ, lower, halfWay - 1, searchFor);
			if(employ[halfWay].getEmployeeNumber() < searchFor)
				return searchEmployeeNumber(employ, halfWay + 1, upper, searchFor);
			//returns the index of item being searched for
			return halfWay;
		}
		/**
		 * Gets the total number of the employees
		 * @return length
		 */
		public static int getNumOfEmployees() {
			int length = 0;
			while(employees[length] != null) {
				length++;
			}
			return length;
		}
		/**
		 * Runs the payroll class
		 */
		public static void runPayroll() {
			Scanner keyboard = new Scanner(System.in);
			String input = "";
			String employeeType;
			float info;
			boolean check;
			int length = getNumOfEmployees();
			//goes through each employee and checks the employee type and asks questions accordingly
			for(int i = 0; i < length; i++) {
				check = false;
				employeeType = employees[i].getEmployeeType();
				if(employeeType.equals("Commissioned")) {
					//continues to loop until the user quits or inputs a valid number of units
					while(check == false) {
						System.out.println("How many units did this employee sell?(To end, enter \"q\". Ending will delete all previous info "
								+ "entered while running payroll)");
						input = keyboard.nextLine();
						if(input.equals("q")) {
							System.out.println("Are you sure you want to quit pay roll? This will delete all previous info entered while"
									+ "running payroll.\nEnter \"y\" for yes or \"n\" for no");
							input = keyboard.nextLine();
							if(input.equals("y"))
								return;
						}
						check = checkNumbers(input);
						if(check == false)
							System.out.println("Please enter a valid number of units sold.");
					}
					//sets the given number of units sold for this employee
					info = Float.parseFloat(input);
					((CommissionedEmployee) employees[i]).setUnitsSold(info);
				}
				else if(employeeType.equals("Hourly")) {
					//continues to loop until the user either quits or inputs a valid number of hours worked
					while(check == false) {
						System.out.println("How many hours did this employee work?(To end, enter \"q\". Ending will delete all previous info "
								+ "entered while running payroll)");
						input = keyboard.nextLine();
						if(input.equals("q"))
							return;
						check = checkNumbers(input);
					}
					//sets the given number of hours for this employee
					info = Float.parseFloat(input);
					((HourlyEmployee)employees[i]).setHoursWorked(info);
				}
			}
			selectionSort(employees);
		}
		/**
		 * Sorts the employees and prints them out
		 * @param employ
		 */
		public static void selectionSort(Employee[] employ) {
			int length = getNumOfEmployees();
			Employee temporary;
			//sorts employees by their pay checks
			for(int first = 0; first < length; first++) {
				int numOfChars = 32;
				int index = first;
				for(int i = first; i < length; i++)
					if(employ[index].getPayCheck() < employ[i].getPayCheck())
						index = i;
				temporary = employ[first];
				employ[first] = employ[index];
				employ[index] = temporary;
				//finds the correct formatting for each print statement for each employee
				numOfChars = numOfChars - employees[first].getLastName().length() - employees[first].getFirstName().length() - 2;
				String payCheck = "";
				payCheck = payCheck.format("$%.2f", employees[first].getPayCheck());
				numOfChars -= payCheck.length();
				//prints a formatted statement for each employee
				System.out.print(employees[first].getLastName() + ", " + employees[first].getFirstName());
				for(int spaces = 0; spaces < numOfChars; spaces++)
					System.out.print(" ");
				System.out.println(payCheck + "\n");
			}
		}
}