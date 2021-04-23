package visitordetector.metamodel.impl;

import visitordetector.metamodel.entity.*;
import methods.ToString;
import methods.ParentClassName;
import methods.ShowInEditor;

public class MMethodImpl implements MMethod {

	private org.eclipse.jdt.core.IMethod underlyingObj_;

	private static final ToString ToString_INSTANCE = new ToString();
	private static final ParentClassName ParentClassName_INSTANCE = new ParentClassName();
	private static final ShowInEditor ShowInEditor_INSTANCE = new ShowInEditor();

	public MMethodImpl(org.eclipse.jdt.core.IMethod underlyingObj) {
		underlyingObj_ = underlyingObj;
	}

	@Override
	public org.eclipse.jdt.core.IMethod getUnderlyingObject() {
		return underlyingObj_;
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.String toString() {
		return ToString_INSTANCE.compute(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.String parentClassName() {
		return ParentClassName_INSTANCE.compute(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsAnAction
	public void showInEditor() {
		 ShowInEditor_INSTANCE.performAction(this, ro.lrg.xcore.metametamodel.HListEmpty.getInstance());
	}

	public boolean equals(Object obj) {
		if (null == obj || !(obj instanceof MMethodImpl)) {
			return false;
		}
		MMethodImpl iObj = (MMethodImpl)obj;
		return underlyingObj_.equals(iObj.underlyingObj_);
	}

	public int hashCode() {
		return 97 * underlyingObj_.hashCode();
	}
}
