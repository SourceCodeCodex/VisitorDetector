package classes;

import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import visitordetector.metamodel.entity.MClass;

@PropertyComputer
public class ToString implements IPropertyComputer<String, MClass> {

	@Override
	public String compute(MClass arg0) {
		return arg0.getUnderlyingObject().getFullyQualifiedName();
	}

}
