package system;

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
import visitordetector.metamodel.entity.MSystem;

@RelationBuilder
public class ClassGroupWithAtLeastTwoDescendantsCasts implements IRelationBuilder<MClass, MSystem> {

	@Override
	public Group<MClass> buildGroup(MSystem arg0) {
		Group<MClass> types = new Group<>();
		List<MClass> systemTypes = arg0.classGroup().getElements();
		for (MClass type : systemTypes) {
			if (type.noOfDescendantsCasts() >= 2) {
				types.add(type);
			}
		}
		return types;
	}

	private boolean isBaseType(MClass type) throws JavaModelException {
		return binarySupertypes(type);
	}

	private boolean binarySupertypes(MClass arg0) throws JavaModelException {
		ITypeHierarchy hierarchy = arg0.getUnderlyingObject().newSupertypeHierarchy(new NullProgressMonitor());
		IType[] supertypes = hierarchy.getAllSupertypes(arg0.getUnderlyingObject());
		for (IType i : supertypes) {
			if (!i.isBinary())
				return false;
		}
		return true;
	}

}
