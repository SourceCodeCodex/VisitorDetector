package visitordetector.metamodel.impl;

import visitordetector.metamodel.entity.*;
import system.ToString;
import system.ClassGroup;

public class MSystemImpl implements MSystem {

	private org.eclipse.jdt.core.IJavaProject underlyingObj_;

	private static final ToString ToString_INSTANCE = new ToString();
	private static final ClassGroup ClassGroup_INSTANCE = new ClassGroup();

	public MSystemImpl(org.eclipse.jdt.core.IJavaProject underlyingObj) {
		underlyingObj_ = underlyingObj;
	}

	@Override
	public org.eclipse.jdt.core.IJavaProject getUnderlyingObject() {
		return underlyingObj_;
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.String toString() {
		return ToString_INSTANCE.compute(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MClass> classGroup() {
		return ClassGroup_INSTANCE.buildGroup(this);
	}

	public boolean equals(Object obj) {
		if (null == obj || !(obj instanceof MSystemImpl)) {
			return false;
		}
		MSystemImpl iObj = (MSystemImpl)obj;
		return underlyingObj_.equals(iObj.underlyingObj_);
	}

	public int hashCode() {
		return 97 * underlyingObj_.hashCode();
	}
}
