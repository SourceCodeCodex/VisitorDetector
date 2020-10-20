package system;

import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import visitordetector.metamodel.entity.MSystem;

@PropertyComputer
public class ToString implements IPropertyComputer<String, MSystem> {

	@Override
	public String compute(MSystem arg0) {
		return arg0.getUnderlyingObject().getElementName();
	}

}
