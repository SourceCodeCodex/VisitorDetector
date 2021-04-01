package classes.medians;

import java.util.ArrayList;
import java.util.List;

import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import utils.CastSearchingUtils;
import utils.MathUtils;
import utils.Utils;
import visitordetector.metamodel.entity.MClass;
import visitordetector.metamodel.entity.MMethod;

@PropertyComputer
public class MedianOfDistinctClientsDoingAtLeastOneCastToMyDescendants3 implements IPropertyComputer<Double, MClass> {

	@Override
	public Double compute(MClass arg0) {
		List<MMethod> allClients = arg0.myClientsWithAtLeastOneCastToMyDescendants3().getElements();
		List<Integer> descendantsCasts = new ArrayList<>();
		CastSearchingUtils csu = new CastSearchingUtils(arg0);
		allClients.forEach(client -> descendantsCasts.add(csu.getNoOfDescendantsCasts(client.getUnderlyingObject())));
//		for (int i = 0; i < allClients.size(); i++) {
//			if (descendantsCasts.get(i) == 0)
//				System.out.println("What the heck is going with this client->"
//						+ Utils.getElementName(allClients.get(i).getUnderlyingObject()));
//		}
		return MathUtils.computeMedian(descendantsCasts);
	}

}
