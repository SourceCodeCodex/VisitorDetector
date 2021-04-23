package classes;

import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import visitordetector.metamodel.entity.MClass;

@PropertyComputer
public class PercentageOfClientsDoingCastsToMyDescendants implements IPropertyComputer<Double, MClass> {

	@Override
	public Double compute(MClass arg0) {
		Integer noOfClients = arg0.myClients().getElements().size();
		if (noOfClients == 0 || noOfClients == 1)
			return 0.0;
		Integer noOfClientsWithCasts = arg0.myClientsWithAtLeastOneCastToMyDescendants().getElements().size();
		if (noOfClientsWithCasts == 0 || noOfClientsWithCasts == 1)
			return 0.0;
		return noOfClientsWithCasts * 100.0 / noOfClients;
	}
}
