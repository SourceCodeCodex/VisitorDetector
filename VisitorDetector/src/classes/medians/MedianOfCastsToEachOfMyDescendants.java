package classes.medians;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import utils.MathUtils;
import visitordetector.metamodel.entity.MClass;

@PropertyComputer
public class MedianOfCastsToEachOfMyDescendants implements IPropertyComputer<Double, MClass> {

	@Override
	public Double compute(MClass arg0) {
		List<MClass> descendants = arg0.descendantsGroup().getElements();
		List<Integer> descendantsCasts = new ArrayList<>();
		descendants.forEach(descendent -> descendantsCasts.add(descendent.noOfCastsToMe()));
		return MathUtils.computeMedian(descendantsCasts);
	}
}
