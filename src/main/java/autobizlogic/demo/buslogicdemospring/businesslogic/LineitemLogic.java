package autobizlogic.demo.buslogicdemospring.businesslogic;

import com.autobizlogic.abl.businesslogic.annotations.*;

public class LineitemLogic {
	
	@Formula("productPrice * qtyOrdered")
	public void deriveAmount() { }

	@ParentCopy("product.price")
	public void deriveProductPrice() { }
}
