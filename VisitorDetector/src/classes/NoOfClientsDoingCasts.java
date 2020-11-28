package classes;

import java.util.LinkedList;
import java.util.List;

import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import visitordetector.metamodel.entity.MClass;
import visitordetector.metamodel.entity.MMethod;

@PropertyComputer
public class NoOfClientsDoingCasts implements IPropertyComputer<Integer, MClass> {

	@Override
	public Integer compute(MClass arg0) {
		List<MMethod> methodsWithCasts = arg0.methodsWithCasts().getElements();
		return removeDuplicates(methodsWithCasts).size();
	}

	private List<String> removeDuplicates(List<MMethod> methodsWithCasts) {
		List<String> methods = new LinkedList<>();
		String name;
		for (MMethod method : methodsWithCasts) {
			name = method.parentClassName();
			if (!methods.contains(name)) {
				methods.add(name);
			}
		}
		return methods;
	}

}
