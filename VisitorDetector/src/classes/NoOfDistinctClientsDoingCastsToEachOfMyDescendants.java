package classes;

import java.util.List;

import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import visitordetector.metamodel.entity.MClass;
import visitordetector.metamodel.entity.MMethod;

@PropertyComputer
public class NoOfDistinctClientsDoingCastsToEachOfMyDescendants implements IPropertyComputer<Integer, MClass> {

	@Override
	public Integer compute(MClass arg0) {
		List<MMethod> methodsWithCasts = arg0.distinctMethodsWithCastsToMyDescendants().getElements();
		return methodsWithCasts.size();
	}

}
