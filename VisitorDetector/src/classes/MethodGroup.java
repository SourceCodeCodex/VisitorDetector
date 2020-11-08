package classes;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;

import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import visitordetector.metamodel.entity.MClass;
import visitordetector.metamodel.entity.MMethod;
import visitordetector.metamodel.factory.Factory;

@RelationBuilder
public class MethodGroup implements IRelationBuilder<MMethod, MClass> {

	@Override
	public Group<MMethod> buildGroup(MClass arg0) {
		Group<MMethod> res = new Group<>();
		try {
			IMethod[] methods = arg0.getUnderlyingObject().getMethods();
			for (IMethod method : methods) {
				MMethod m = Factory.getInstance().createMMethod(method);
				res.add(m);
			}
		} catch (JavaModelException e) {
			System.err.println("MMethod - MClass:" + e.getMessage());
		}
		return res;
	}

}
