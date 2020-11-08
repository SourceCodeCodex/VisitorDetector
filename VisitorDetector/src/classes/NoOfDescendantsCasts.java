package classes;

import java.util.List;

import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import visitordetector.metamodel.entity.MClass;

@PropertyComputer
public class NoOfDescendantsCasts implements IPropertyComputer<Integer, MClass> {

	@Override
	public Integer compute(MClass arg0) {
		int noOfCasts = 0;
		List<MClass> descendants = arg0.subGroup().getElements();
		for (MClass descendent : descendants) {
			noOfCasts += descendent.noOfCasts();
		}
		return noOfCasts;
	}

}
