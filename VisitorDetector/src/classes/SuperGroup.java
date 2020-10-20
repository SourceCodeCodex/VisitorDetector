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
public class SuperGroup implements IRelationBuilder<MClass,MClass> {

	@Override
	public Group<MClass> buildGroup(MClass arg0) {
		Group<MClass> types = new Group<>();
		try {
			List<IType> supertypes = this.getSupertypes(arg0);
			for(IType type:supertypes) {
				MClass supertype = Factory.getInstance().createMClass(type);
				types.add(supertype);
			}
		} catch (JavaModelException e) {
			System.err.println("MClass - MClass -> Super:" + e.getMessage());
		}
		return types;
	}
	
	public List<IType> getSupertypes(MClass arg0) throws JavaModelException {
		List<IType> types = new LinkedList<>();
		ITypeHierarchy hierarchy = arg0.getUnderlyingObject().newSupertypeHierarchy(new NullProgressMonitor());
		IType[] supertypes = hierarchy.getAllSupertypes(arg0.getUnderlyingObject());
		for(IType i:supertypes) {
			types.add(i);
		}
		return types;
	}

}
