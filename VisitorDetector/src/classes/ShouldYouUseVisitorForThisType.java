package classes;

import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import visitordetector.metamodel.entity.MClass;

@PropertyComputer
public class ShouldYouUseVisitorForThisType implements IPropertyComputer<String, MClass> {

	@Override
	public String compute(MClass arg0) {
		int noOfClients = arg0.myClients().getElements().size();
		if (noOfClients == 0 || noOfClients == 1)
			return "No";
		int noOfClientsWithAtLeastOneCastToMyDescendants = arg0.myClientsWithAtLeastOneCastToMyDescendants()
				.getElements().size();
		if (noOfClientsWithAtLeastOneCastToMyDescendants == 0 || noOfClientsWithAtLeastOneCastToMyDescendants == 1)
			return "No";
		double percentageOfClientsDoingCasts = noOfClientsWithAtLeastOneCastToMyDescendants * 100.0 / noOfClients;
		if (percentageOfClientsDoingCasts < 1)
			return "No";
		int noOfDescendants = arg0.descendantsGroup().getElements().size();
		if (noOfDescendants > noOfClients)
			return "No";
		double arithmeticAverageOfDistinctClientsDoingAtLeastOneCastToMyDescendants = arg0
				.arithmeticAverageOfDistinctClientsDoingAtLeastOneCastToMyDescendants();
		if (arithmeticAverageOfDistinctClientsDoingAtLeastOneCastToMyDescendants < 1)
			return "No";
		return "Yes";
	}

}
