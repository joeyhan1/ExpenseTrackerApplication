import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;

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
 * @version 1.0
 * @since 1.0
 */
class ExpenseTrackerUI {
    Scanner scanner = new Scanner(System.in);
    public void run() {
        System.out.println("Welcome to Expense Tracker!");
        getExpenses();
    }

    /**
     * Asking user for all their expenses
     * @return A Arraylist of expenses that is asked from the user
     */
    private ArrayList<Expense> getExpenses() {
        ArrayList<Expense> expenses = new ArrayList<Expense>();
        boolean done = false;
        //Keep looping till user doesn't have any more expenses
        while (!done) {
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
            String userInput = scanner.next();
            switch (userInput.toLowerCase()) {
                case "yes":
                    done = false;
                    break;
                case "no":
                    done = true;
                    break;
                default:
                    done = false;
                    System.out.println("Please type either Yes or No!");
            }
        }
    
        //Testing purposes
        for (Expense expense : expenses) {
            System.out.println("Category: " + expense.getCategory() +
                    ", Amount: " + expense.getAmount() +
                    ", Date: " + expense.getDate());
        }
    
        return expenses;
    }

    /**
     * Ask user for expense category
     * @return A String representing the expense category that the user picked
     */
    private String pickCategory() {
        //Arrays and Arraylists
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
    
        //Loops until the user picks a category that is in the category array
        while(true) {
            System.out.print("Enter your choice: ");
            String choice = scanner.next();
            if(isValidInteger(choice)) {
            //Checking if the user inputs a category number that is array range
                int choiceNum = Integer.valueOf(choice);
                if (choiceNum < 1 || choiceNum > 13) {
                    System.out.println("Invalid choice. Please enter a number between 1 and 13.");
                } else {
                    return categoriesArray[choiceNum-1];
                }
            } else {
                System.out.println("Invalid choice. Please enter a number between 1 and 13.");
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
            String userAmount = scanner.next();
            if (isValidDouble(userAmount)) {
                return Math.round(Double.parseDouble(userAmount) * 100.0) / 100.0;
            } else {
                System.out.println(userAmount + " is not a valid currency number!");
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
            String userDate = scanner.next();
            Matcher matcher = datePattern.matcher(userDate);
            boolean matched = matcher.find();
            if(matched) {
                String[] dateParts = userDate.split("/");
                int day = Integer.parseInt(dateParts[0]);
                int month = Integer.parseInt(dateParts[1]);
                int year = Integer.parseInt(dateParts[2]);
                if (month < 1 || month > 12 || day < 1 || day > 31) {
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