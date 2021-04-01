package classes.medians;

import java.util.ArrayList;
import java.util.List;

import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import utils.CastSearchingUtils;
import utils.MathUtils;
import visitordetector.metamodel.entity.MClass;
import visitordetector.metamodel.entity.MMethod;

@PropertyComputer
public class MedianOfDistinctClientsDoingAtLeastOneCastToMyDescendants1 implements IPropertyComputer<Double, MClass> {

	@Override
	public Double compute(MClass arg0) {
		List<MMethod> allClients = arg0.myClientsWithAtLeastOneCastToMyDescendants1().getElements();
		List<Integer> descendantsCasts = new ArrayList<>();
		CastSearchingUtils csu = new CastSearchingUtils(arg0);
		allClients.forEach(client -> descendantsCasts.add(csu.getNoOfDescendantsCasts(client.getUnderlyingObject())));
		return MathUtils.computeMedian(descendantsCasts);
	}

}
