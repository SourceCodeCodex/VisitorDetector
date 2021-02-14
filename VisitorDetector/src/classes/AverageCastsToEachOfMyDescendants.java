package classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import visitordetector.metamodel.entity.MClass;

@PropertyComputer
public class AverageCastsToEachOfMyDescendants implements IPropertyComputer<Double, MClass> {

	@Override
	public Double compute(MClass arg0) {
		List<MClass> descendants = arg0.descendantsGroup().getElements();
		List<Integer> descendantsCasts = new ArrayList<>();
		descendants.forEach(descendent -> descendantsCasts.add(descendent.noOfCastsToMe()));
		Collections.sort(descendantsCasts);
		return computeMedian(descendantsCasts);
	}

	private Double computeMedian(List<Integer> casts) {
		int size = casts.size();
		switch (size) {
		case 0:
			return 0.0;
		case 1:
			return casts.get(0) * 1.0;
		case 2:
			return (casts.get(0) + casts.get(1)) / 2.0;
		default:
			Collections.sort(casts);
			return size % 2 != 0 ? casts.get(size / 2) : (casts.get(size / 2 - 1) + casts.get(size / 2)) / 2.0;
		}
	}

}
