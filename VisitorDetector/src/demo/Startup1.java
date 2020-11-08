package demo;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.ui.IStartup;

import ro.lrg.insider.view.ToolRegistration;
import ro.lrg.insider.view.ToolRegistration.XEntityConverter;
import ro.lrg.xcore.metametamodel.XEntity;
import visitordetector.metamodel.factory.Factory;

public class Startup1 implements IStartup {

	@Override
	public void earlyStartup() {
		ToolRegistration.getInstance().registerXEntityConverter(new XEntityConverter() {

			@Override
			public XEntity convert(Object element) {
				if (element instanceof IType)
					return Factory.getInstance().createMClass((IType) element);
				else if (element instanceof IJavaProject)
					return Factory.getInstance().createMSystem((IJavaProject) element);
				else if (element instanceof IMethod)
					return Factory.getInstance().createMMethod((IMethod) element);
				return null;
			}

		});

	}

}
