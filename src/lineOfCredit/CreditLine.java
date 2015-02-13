package lineOfCredit;

import java.util.Date;
import java.util.ArrayList; //Dynamic array
import java.math.BigDecimal; //More accurate than double and can control rounding
import java.math.RoundingMode;
import java.lang.Exception;

public class CreditLine {
  
  /* creditLine variables */
  private BigDecimal apr;
  private BigDecimal creditLimit;
  private BigDecimal principalBalance;
  private Date creditLineOpened;
  private ArrayList<Transaction> transactions;
  private ArrayList<ArrayList<Transaction>> transactionHistory;
  
  /* creditLine class constructors */
  public CreditLine() {
    this.apr = new BigDecimal(".35");
    this.creditLimit = new BigDecimal("1000.00");
    init();
  };
  
  CreditLine(double apr, double creditLimit) throws Exception {
    if (apr >= 0)
      this.apr = BigDecimal.valueOf(apr);
    else
      throw new Exception("Setting apr not valid for apr " + apr);
    if (creditLimit >= 0) 
    this.creditLimit = BigDecimal.valueOf(creditLimit);
    else
      throw new Exception("Setting creditLimit not valid for creditLimit " + creditLimit);
    init();
  };
  
  private void init() {
    this.creditLineOpened = new Date();
    this.principalBalance = new BigDecimal("0.00");
    this.transactions = new ArrayList<Transaction>(30);
    this.transactions.add(new Transaction(this.creditLineOpened, this.principalBalance));
    this.transactionHistory = new ArrayList<ArrayList<Transaction>>(12);
  };
  
  /* creditLine set() and get() */
  public void setTransactions(ArrayList<Transaction> transactions) {
    this.transactions = transactions;
  }
  
  public BigDecimal getPrincipalBalance() {
    return this.principalBalance;
  }
  
  public void setPrincipalBalance(double principalBalance) {
	this.principalBalance = BigDecimal.valueOf(principalBalance);
  }
  
  /* creditLine methods */
  public void transact(double transactAmount) throws Exception{
    if (checkTransactAmount(BigDecimal.valueOf(transactAmount))){
      this.principalBalance = this.principalBalance.add(BigDecimal.valueOf(transactAmount));
      logTransaction();
    }
    else
      throw new Exception("Transaction not valid for transaction amount " + transactAmount);
  };
  
  public boolean checkTransactAmount(BigDecimal transactAmount) {
    if (-1 == transactAmount.signum() && 1 != transactAmount.abs().compareTo(this.principalBalance))
      return true; //if (making a payment && not paying more than you owe)
    else if (-1 != transactAmount.signum() && 1 != transactAmount.compareTo(getRemainingCreditLimit()))
      return true; //if (making a withdrawal && withdrawing within remaining creditLimit)
    else
      return false;
  };
  
  public BigDecimal getRemainingCreditLimit() {
    return this.creditLimit.subtract(this.principalBalance);
  };
  
  public void logTransaction() {
    Transaction currentTransaction = new Transaction(new Date(), this.principalBalance);
    this.transactions.add(currentTransaction);
  };
  
  public BigDecimal getTotalOwed() {
    BigDecimal totalOwed = this.principalBalance;
    long periodEndDate = this.transactions.get(0).date.getTime() + ((long)30*24*60*60*1000);
    for (int i=0; i < transactions.size(); i++) {
      Transaction t = transactions.get(i);
      long msElapsed;
      if (i == transactions.size() - 1)
        msElapsed = periodEndDate - t.date.getTime();
      else
        msElapsed = transactions.get(i+1).date.getTime() - t.date.getTime(); 
      long yearInMS = (long) 365*24*60*60*1000;
      BigDecimal interestAmount = t.postBalance.multiply(this.apr).divide(BigDecimal.valueOf(yearInMS), 99, RoundingMode.HALF_DOWN).multiply(BigDecimal.valueOf(msElapsed));
      totalOwed = totalOwed.add(interestAmount.setScale(2, BigDecimal.ROUND_UP));
    }
    return totalOwed;
  };
  
  public void closePayPeriod() {
    transactions.trimToSize();
    this.transactionHistory.add(transactions);
    this.principalBalance = getTotalOwed();
    transactions.clear();
    logTransaction();
  };
}