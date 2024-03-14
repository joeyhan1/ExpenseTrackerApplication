import java.util.Scanner;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.io.*;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * The ExpenseTrackerApp implements an console based application 
 * that helps you track and categorises your expenses. 
 * It also generates reports.
 * 
 * @author Joey Han
 * @version 1.0
 * @since 11-03-2024
 * 
 */
public class ExpenseTrackerApp {
    public static void main(String[] args) {
        //Initialise the application
        ExpenseTrackerUI ui = new ExpenseTrackerUI();
        //Run the application
        ui.run();
    }
}

/**
 * Represents the console user interface for the Expense Tracker App
 * @author Joey Han
 * @version 1.2
 * @since 1.0
 */
class ExpenseTrackerUI {
    //Variables and objects
    Scanner scanner = new Scanner(System.in);
    boolean done = false;

    /**
     * Runs the expense tracker application
     */
    public void run() {
        System.out.println("Welcome to Expense Tracker!");
    
        while(!done) {
            System.out.println("What would you like to do?");
            System.out.println("1. Add Expenses into an Excel file");
            System.out.println("2. Generate total expenses for a specific period");
            System.out.println("3. Exit Program");
            System.out.print("Enter your choice: ");

            String userInput = scanner.nextLine().trim();
            //Checking whether the input is a integer
            if(isValidInteger(userInput)) {
                int choice = Integer.parseInt(userInput);
                switch(choice) {
                    case 1:
                        generateExcelFile(getExpenses());
                    break;
                    case 2:
                        generateStatistics();
                        break;
                    case 3:
                        done = true;
                        System.out.println("Exiting Expense Tracker. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number from the list of options.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    /**
     * Asking user for all their expenses
     * @return A Arraylist of expenses that is asked from the user
     */
    private ArrayList<Expense> getExpenses() {
        //Variables and objects
        ArrayList<Expense> expenses = new ArrayList<Expense>();
        boolean done = false;
        //Keep looping till user doesn't have any more expenses
        while(!done) {
            String category = pickCategory();
            Double amount = getExpenseAmount();
            String date = getExpenseDate();
    
            //Create a new Expense object with the obtained values
            Expense expense = new Expense();
            expense.setCategory(category);
            expense.setAmount(amount);
            expense.setDate(date);
    
            //Add the Expense object to the expenses list
            expenses.add(expense);

            //Ask the user for more expenses
            System.out.print("Is there any more expenses? ");
            String userInput = scanner.nextLine().trim().toLowerCase();
            //Loop till user say Yes or No
            while(!userInput.equals("yes") && !userInput.equals("no")) {
                System.out.println("Please type either Yes or No!");
                userInput = scanner.nextLine().trim().toLowerCase();
            }
            if(userInput.equals("no")) {
                done = true;
            }
        }
    
        //Testing purposes
        // for(Expense expense : expenses) {
        //     System.out.println("Category: " + expense.getCategory() +
        //         ", Amount: " + expense.getAmount() +
        //         ", Date: " + expense.getDate());
        // }
    
        return expenses;
    }

    /**
     * Ask user for expense category
     * @return A String representing the expense category that the user picked
     */
    private String pickCategory() {
        //Arraylists
        String[] categoriesArray = {
            "Housing", "Transportation", "Food", "Utilities", "Healthcare",
            "Debt Payments", "Entertainment", "Personal Care", "Education",
            "Savings and Investments", "Insurance", "Taxes", "Miscellaneous"
        };
    
        //Ask the user to pick a category
        System.out.println("Pick the categories: ");
        for(int i = 0; i < categoriesArray.length; i++) {
            int counter = i + 1;
            System.out.println(counter + ": " + categoriesArray[i]);
        }
    
        //Loop until the user picks a category that is in the category array
        while(true) {
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine().trim();
            //Checking whether user input is an integer
            if(isValidInteger(choice)) {
                //Checking if the user inputs a category number that is in the array range
                int choiceNum = Integer.valueOf(choice);
                if(choiceNum < 1 || choiceNum > categoriesArray.length) {
                    System.out.println("Invalid choice. Please enter a number between 1 and " + categoriesArray.length + ".");
                } else {
                    return categoriesArray[choiceNum - 1];
                }
            } else {
                System.out.println("Invalid choice. Please enter a number between 1 and " + categoriesArray.length + ".");
            }
        }
    }

    /**
     * Ask user for the expense amount
     * @return A double representing the expense amount that the user given in 2dp
     */
    private Double getExpenseAmount() {
        while(true) {
            System.out.print("Expense Amount: ");
            String userAmount = scanner.nextLine().trim();
            //Checking whether the user inputted a double or negatives
            if(isValidDouble(userAmount) && Double.parseDouble(userAmount) >= 0) {
                return Math.round(Double.parseDouble(userAmount) * 100.0) / 100.0;
            } else {
                System.out.println("Invalid input. Please enter a valid currency number!");
            }
        }
    }

    /**
     * Ask user for the expense date
     * @return A string representing the date that the user given
     */
    private String getExpenseDate() {
        Pattern datePattern = Pattern.compile("^\\d{2}/\\d{2}/\\d{4}$");
        while(true) {
            System.out.print("Date of Expenses: ");
            String userDate = scanner.nextLine().trim();
            //Checking for empty inputs
            if(userDate.isEmpty()) {
                System.out.println("Date cannot be empty. Please enter a valid date.");
                continue;
            }
            Matcher matcher = datePattern.matcher(userDate);
            boolean matched = matcher.find();
            //Checking whether the user input date format is correct
            if(matched) {
                String[] dateParts = userDate.split("/");
                int day = Integer.parseInt(dateParts[0]);
                int month = Integer.parseInt(dateParts[1]);
                if(month < 1 || month > 12 || day < 1 || day > 31) {
                    System.out.println("You are only allowed to input days between 1-31 and months between 1-12!");
                    continue;
                } else {
                    return userDate;
                }
            } else {
                System.out.println("Please enter in the format dd/mm/yyyy!");
            }
        }
    }

    /**
     * Generates an Excel file containing the user's expenses.
     * @param expenses An ArrayList of Expense objects representing the user's expenses.
     */
    public void generateExcelFile(ArrayList<Expense> expenses) {
        //Sort expenses based on category before writing to the file
        expenses.sort(Comparator.comparing(Expense::getCategory));

        while(true) {
            //Ask the user to choose the location for saving the file
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save As");
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel files", "xlsx");
            fileChooser.setFileFilter(filter);

            int userSelection = fileChooser.showSaveDialog(null);
            if(userSelection == JFileChooser.APPROVE_OPTION) {
                java.io.File fileToSave = fileChooser.getSelectedFile();
                String filePath = fileToSave.getAbsolutePath();

                //Check if the file extension is provided, if not add .xlsx extension
                if(!filePath.toLowerCase().endsWith(".xlsx")) {
                    filePath += ".xlsx";
                }

                try {
                    //Create BufferedWriter to write data to the Excel file
                    BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));

                    //Write header row
                    writer.write("Category,Amount,Date\n");

                    //Write each expense to the file
                    for(Expense expense : expenses) {
                        writer.write(expense.getCategory() + "," + expense.getAmount() + "," + expense.getDate() + "\n");
                    }

                    //Close the writer
                    writer.close();

                    System.out.println("Excel file saved successfully at: " + filePath);
                    break; // Exit the loop if the file is saved successfully
                } catch(IOException e) {
                    System.out.println("An error occurred while saving the Excel file.");
                    e.printStackTrace();
                }
            } else if(userSelection == JFileChooser.CANCEL_OPTION) {
                //Testing purposes
                // System.out.println("Save command cancelled by the user.");
                //Checking if user wants to save or not in case of misclicks
                int option = JOptionPane.showConfirmDialog(null, "Do you want to try saving again?", "Save Again", JOptionPane.YES_NO_OPTION);
                if(option == JOptionPane.NO_OPTION) {
                    break;
                }
            }
        }
    }

    /**
     * Generates total expenses from start and end period that the user specifies using the user picked expenses excel file
     */
    public void generateStatistics() {
        //Ask the user to select an Excel file
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Excel File");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel files", "xlsx");
        fileChooser.setFileFilter(filter);
    
        int userSelection = fileChooser.showOpenDialog(null);
        if(userSelection == JFileChooser.APPROVE_OPTION) {
            //Read expenses from the selected file
            java.io.File selectedFile = fileChooser.getSelectedFile();
            String filePath = selectedFile.getAbsolutePath();
            try(BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                ArrayList<Expense> expenses = new ArrayList<>();
                String line;
                //Skip header row
                reader.readLine();
                //Getting all the expenses in the excel file and making them into expenses
                while((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    String category = parts[0];
                    double amount = Double.parseDouble(parts[1]);
                    String date = parts[2];
                    Expense expense = new Expense();
                    expense.setCategory(category);
                    expense.setAmount(amount);
                    expense.setDate(date);
                    expenses.add(expense);
                }
    
                //Regex pattern for date in the format dd/MM/yyyy
                Pattern datePattern = Pattern.compile("^\\d{2}/\\d{2}/\\d{4}$");
    
                //Ask the user for start and end dates
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date startDate, endDate;
                while(true) {
                    System.out.print("Enter start date (dd/MM/yyyy): ");
                    String startDateStr = scanner.nextLine().trim();
                    System.out.print("Enter end date (dd/MM/yyyy): ");
                    String endDateStr = scanner.nextLine().trim();
    
                    //Validate start date format using regex
                    Matcher startMatcher = datePattern.matcher(startDateStr);
                    //Validate end date format using regex
                    Matcher endMatcher = datePattern.matcher(endDateStr);
    
                    if(startMatcher.matches() && endMatcher.matches()) {
                        try {
                            startDate = dateFormat.parse(startDateStr);
                            endDate = dateFormat.parse(endDateStr);
                            break;
                        } catch(ParseException e) {
                            System.out.println("Invalid date format. Please enter dates in the format dd/MM/yyyy.");
                        }
                    } else {
                        System.out.println("Invalid date format. Please enter dates in the format dd/MM/yyyy.");
                    }
                }
    
                //Calculate total expenses within the specified date range
                double totalExpenses = 0;
                for(Expense expense : expenses) {
                    Date expenseDate = dateFormat.parse(expense.getDate());
                    if(!expenseDate.before(startDate) && !expenseDate.after(endDate)) {
                        totalExpenses += expense.getAmount();
                    }
                }

                totalExpenses = Math.round(totalExpenses * 100.0) / 100.0;
    
                //Display total expenses to the user
                System.out.println("Total expenses from " + dateFormat.format(startDate) + " to " + dateFormat.format(endDate) + ": $" + totalExpenses);
            } catch(IOException | ParseException e) {
                System.out.println("An error occurred while reading the Excel file.");
                e.printStackTrace();
            }
        }
    }

    /**
     * Method to check whether the String is a valid double
     * @param str
     * @return A boolean representing true for valid double and false for not double
     */
    public boolean isValidDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Method to check whether the String is a valid integer
     * @param str
     * @return A boolean representing true for valid integer and false for not integer
     */
    public boolean isValidInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

/**
 * Represents an expense 
 * @author Joey Han
 * @version 1.0
 * @since 1.0
 */
class Expense {
    //Variables
    private String category;
    private double amount;
    private String date;

    /**
     * Gets the category of the expense
     * @return A string representing the expense category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the category of the expense
     * @param newCategory A string containing the expense category
     */
    public void setCategory(String newCategory) {
        this.category = newCategory;
    }

    /**
     * Gets the amount of the expense
     * @return A double representing the expense amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Sets the amount of the expense
     * @param newAmount A double containing the expense amount
     */
    public void setAmount(double newAmount) {
        this.amount = newAmount;
    }

    /**
     * Gets the date of the expense
     * @return A string representing the expense date
     */
    public String getDate() {
         return date;
    }

    /**
     * Sets the date of the expense
     * @param newDate A string representing the expense date
     */
    public void setDate(String newDate) {
        this.date = newDate;
    }
}