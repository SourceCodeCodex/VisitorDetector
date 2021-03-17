package classes.means;

import java.util.List;

import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import utils.MathUtils;
import visitordetector.metamodel.entity.MClass;
import visitordetector.metamodel.entity.MMethod;

@PropertyComputer
public class ArithmeticAverageOfDistinctClientsDoingCastsToMyDescendants1 implements IPropertyComputer<Double, MClass> {

	@Override
	public Double compute(MClass arg0) {
		List<MMethod> allClients = arg0.myClients1().getElements();
		List<MClass> descendants = arg0.descendantsGroup().getElements();
		return MathUtils.computeArithmeticAverage(allClients, descendants);
	}

}
