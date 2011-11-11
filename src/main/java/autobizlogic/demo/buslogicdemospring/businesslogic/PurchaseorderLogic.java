package autobizlogic.demo.buslogicdemospring.businesslogic;

import com.autobizlogic.abl.businesslogic.annotations.*;

public class PurchaseorderLogic {

	@Sum("lineitems.amount")
	public void deriveAmountTotal() { }
}
