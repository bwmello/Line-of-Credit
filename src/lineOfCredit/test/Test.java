package lineOfCredit.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import lineOfCredit.CreditLine;
import lineOfCredit.Transaction;

public class Test {

	public static void main(String[] args) {
		/* Test 1 */
	    CreditLine myCreditLine = new CreditLine();
	    try {
			myCreditLine.transact(500.00);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	    System.out.println("Principal balance during pay period: " + myCreditLine.getPrincipalBalance());
	    myCreditLine.closePayPeriod();
	    System.out.println("Principal balance at beginning of next pay period: " + myCreditLine.getPrincipalBalance());
	    
	    /* Test 2 */
	    CreditLine myCreditLine2 = new CreditLine();
	    ArrayList<Transaction> transactions = new ArrayList<Transaction>(6);
	    Date currentDate = new Date();
	    transactions.add(new Transaction(currentDate, new BigDecimal("500.00")));
	    Date day15 = new Date(currentDate.getTime() + ((long)14*24*60*60*1000));
	    transactions.add(new Transaction(day15, new BigDecimal("300.00")));
	    Date day25 = new Date(currentDate.getTime() + ((long)24*24*60*60*1000));
	    transactions.add(new Transaction(day25, new BigDecimal("400.00")));
	    myCreditLine2.setTransactions(transactions);
	    myCreditLine2.setPrincipalBalance(400.00);
	    System.out.println("Dates: " + currentDate + day15 + day25);
	    
	    System.out.println("Principal balance during pay period: " + myCreditLine2.getPrincipalBalance());
	    myCreditLine2.closePayPeriod();
	    System.out.println("Principal balance at beginning of next pay period: " + myCreditLine2.getPrincipalBalance());
	  }

}