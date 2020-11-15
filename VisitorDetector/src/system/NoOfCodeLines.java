package system;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;

import ro.lrg.xcore.metametamodel.IPropertyComputer;
import ro.lrg.xcore.metametamodel.PropertyComputer;
import visitordetector.metamodel.entity.MSystem;

@PropertyComputer
public class NoOfCodeLines implements IPropertyComputer<Long, MSystem> {

	@Override
	public Long compute(MSystem arg0) {
		try {
			return getNoOfCodeLines(arg0);
		} catch (JavaModelException e) {
			System.err.println("NoOfCodeLines:" + e.getMessage());
		}
		return (long) 0;
	}

	private long getNoOfCodeLines(MSystem arg0) throws JavaModelException {
		long nr = 0;
		for (IPackageFragment i : arg0.getUnderlyingObject().getPackageFragments()) {
			for (IJavaElement j : i.getChildren()) {
				if (j.getElementType() == IJavaElement.COMPILATION_UNIT) {
					ICompilationUnit k = (ICompilationUnit) j;
					nr += this.countLines(k.getSource());
				}
			}
		}
		return nr;
	}

	private long countLines(String s) {
		long nr = 0;
		nr = s.split("\n").length;
		return nr;
	}

}
