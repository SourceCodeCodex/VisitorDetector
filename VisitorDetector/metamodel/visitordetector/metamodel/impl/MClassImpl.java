package visitordetector.metamodel.impl;

import visitordetector.metamodel.entity.*;
import classes.NoOfCasts;
import classes.ToString;
import classes.SubGroup;
import classes.SuperGroup;
import classes.ShowInEditor;

public class MClassImpl implements MClass {

	private org.eclipse.jdt.core.IType underlyingObj_;

	private static final NoOfCasts NoOfCasts_INSTANCE = new NoOfCasts();
	private static final ToString ToString_INSTANCE = new ToString();
	private static final SubGroup SubGroup_INSTANCE = new SubGroup();
	private static final SuperGroup SuperGroup_INSTANCE = new SuperGroup();
	private static final ShowInEditor ShowInEditor_INSTANCE = new ShowInEditor();

	public MClassImpl(org.eclipse.jdt.core.IType underlyingObj) {
		underlyingObj_ = underlyingObj;
	}

	@Override
	public org.eclipse.jdt.core.IType getUnderlyingObject() {
		return underlyingObj_;
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Integer noOfCasts() {
		return NoOfCasts_INSTANCE.compute(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.String toString() {
		return ToString_INSTANCE.compute(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MClass> subGroup() {
		return SubGroup_INSTANCE.buildGroup(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MClass> superGroup() {
		return SuperGroup_INSTANCE.buildGroup(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsAnAction
	public void showInEditor() {
		 ShowInEditor_INSTANCE.performAction(this, ro.lrg.xcore.metametamodel.HListEmpty.getInstance());
	}

	public boolean equals(Object obj) {
		if (null == obj || !(obj instanceof MClassImpl)) {
			return false;
		}
		MClassImpl iObj = (MClassImpl)obj;
		return underlyingObj_.equals(iObj.underlyingObj_);
	}

	public int hashCode() {
		return 97 * underlyingObj_.hashCode();
	}
}
