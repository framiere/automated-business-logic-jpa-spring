package autobizlogic.demo.buslogicdemospring.businesslogic;

import com.autobizlogic.abl.businesslogic.annotations.*;

public class PurchaseOrderLogic {

	@Sum("lineitems.amount")
	public void deriveAmountTotal() { }
}
