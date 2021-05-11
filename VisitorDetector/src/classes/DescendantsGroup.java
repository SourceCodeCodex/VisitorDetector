package classes;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;

import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import visitordetector.metamodel.entity.MClass;
import visitordetector.metamodel.factory.Factory;

@RelationBuilder
public class DescendantsGroup implements IRelationBuilder<MClass, MClass> {

	@Override
	public Group<MClass> buildGroup(MClass arg0) {
		Group<MClass> types = new Group<>();
		try {
			List<IType> subtypes = this.getSubtypes(arg0);
			for (IType type : subtypes) {
				MClass subtype = Factory.getInstance().createMClass(type);
				types.add(subtype);
			}
		} catch (JavaModelException e) {
			System.err.println("MClass - MClass -> Sub:" + e.getMessage());
		}
		return types;
	}

	private List<IType> getSubtypes(MClass arg0) throws JavaModelException {
		List<IType> types = new LinkedList<>();
		ITypeHierarchy hierarchy = arg0.getUnderlyingObject().newTypeHierarchy(new NullProgressMonitor());
		IType[] subtypes = hierarchy.getAllSubtypes(arg0.getUnderlyingObject());
		for (IType i : subtypes) {
			types.add(i);
		}
		return types;
	}

}
