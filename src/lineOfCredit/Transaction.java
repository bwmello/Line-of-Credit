package lineOfCredit;

import java.math.BigDecimal;
import java.util.Date;

public class Transaction {
	
	public Date date;
    public BigDecimal postBalance;
    
    public Transaction(Date date, BigDecimal postBalance) {
      this.date = date;
      this.postBalance = postBalance;
    }   
}