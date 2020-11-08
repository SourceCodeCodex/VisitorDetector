package methods;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;

import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import visitordetector.metamodel.entity.MMethod;

@PropertyComputer
public class ParentClassName implements IPropertyComputer<String, MMethod> {

	@Override
	public String compute(MMethod arg0) {
		IJavaElement parent = arg0.getUnderlyingObject().getParent();
		IType type = (IType) parent;
		return type.getFullyQualifiedName();
	}

}
