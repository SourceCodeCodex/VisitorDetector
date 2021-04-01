package classes;

import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import visitordetector.metamodel.entity.MClass;

@PropertyComputer
public class PercentageOfClientsDoingCastsToMyDescendants implements IPropertyComputer<Double, MClass> {

	@Override
	public Double compute(MClass arg0) {
		Integer noOfClients = arg0.myClients3().getElements().size();
		if (noOfClients == 0)
			return 0.0;
		Integer noOfClientsWithCasts = arg0.myClientsWithAtLeastOneCastToMyDescendants3().getElements().size();
		if (noOfClientsWithCasts == 0)
			return 0.0;
		return noOfClientsWithCasts * 100.0 / noOfClients;
	}
}
