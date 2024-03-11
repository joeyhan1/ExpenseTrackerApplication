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

class ExpenseTrackerUI {
    public void run() {
        System.out.println("Welcome to Expense Tracker!");
    }

}

class Expense {
    //Variables
    private String category;
    private double amount;
    private String date;

    //Getter and Setters Methods
    public String getCategory() {
        return category;
    }

    public void setCategory(String newCategory) {
        this.category = newCategory;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double newAmount) {
        this.amount = newAmount;
    }

    public String getDate() {
         return date;
    }

    public void setDate(String newDate) {
        this.date = newDate;
    }
}