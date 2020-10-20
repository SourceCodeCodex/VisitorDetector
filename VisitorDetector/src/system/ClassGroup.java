package system;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import visitordetector.metamodel.entity.MClass;
import visitordetector.metamodel.entity.MSystem;
import visitordetector.metamodel.factory.Factory;

@RelationBuilder
public class ClassGroup implements IRelationBuilder<MClass, MSystem> {

	@Override
	public Group<MClass> buildGroup(MSystem arg0) {
		Group<MClass> systemClasses = new Group<>();
		try {
			List<IType> classes = getClasses(arg0);
			for(IType i:classes) {
				MClass c = Factory.getInstance().createMClass(i);
				systemClasses.add(c);
			}
			
		} catch (JavaModelException e) {
			System.err.println("MClass - MSystem:" + e.getMessage());
		}
		return systemClasses;
	}
	
	private List<IType> getClasses(MSystem arg0) throws JavaModelException {
		List<IType> classes = new LinkedList<>();
		for(IPackageFragment i:arg0.getUnderlyingObject().getPackageFragments()) {
			for(IJavaElement j: i.getChildren()) {
				if(j.getElementType() == IJavaElement.COMPILATION_UNIT) {
					IType[] types = ((ICompilationUnit)j).getAllTypes();
					for(IType type:types) {
						classes.add(type);
					}
				}
			}
		}
		return classes;
	}

}
