package system;

import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import visitordetector.metamodel.entity.MSystem;

@PropertyComputer
public class NoOfClasses implements IPropertyComputer<Integer, MSystem> {

	@Override
	public Integer compute(MSystem arg0) {
		return arg0.classGroup().getElements().size();
	}

}
