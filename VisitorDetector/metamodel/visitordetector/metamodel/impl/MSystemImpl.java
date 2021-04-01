package visitordetector.metamodel.impl;

import visitordetector.metamodel.entity.*;
import system.NoOfCodeLines;
import system.NoOfClasses;
import system.ToString;
import system.ClassGroupWithAtLeastTwoCastsToTheirDescendants;
import system.ClassGroupWithPercentageOfClientsDoingCastsAtLeastOne;
import system.ClassGroup;

public class MSystemImpl implements MSystem {

	private org.eclipse.jdt.core.IJavaProject underlyingObj_;

	private static final NoOfCodeLines NoOfCodeLines_INSTANCE = new NoOfCodeLines();
	private static final NoOfClasses NoOfClasses_INSTANCE = new NoOfClasses();
	private static final ToString ToString_INSTANCE = new ToString();
	private static final ClassGroupWithAtLeastTwoCastsToTheirDescendants ClassGroupWithAtLeastTwoCastsToTheirDescendants_INSTANCE = new ClassGroupWithAtLeastTwoCastsToTheirDescendants();
	private static final ClassGroupWithPercentageOfClientsDoingCastsAtLeastOne ClassGroupWithPercentageOfClientsDoingCastsAtLeastOne_INSTANCE = new ClassGroupWithPercentageOfClientsDoingCastsAtLeastOne();
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
	public java.lang.Long noOfCodeLines() {
		return NoOfCodeLines_INSTANCE.compute(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Integer noOfClasses() {
		return NoOfClasses_INSTANCE.compute(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.String toString() {
		return ToString_INSTANCE.compute(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MClass> classGroupWithAtLeastTwoCastsToTheirDescendants() {
		return ClassGroupWithAtLeastTwoCastsToTheirDescendants_INSTANCE.buildGroup(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MClass> classGroupWithPercentageOfClientsDoingCastsAtLeastOne() {
		return ClassGroupWithPercentageOfClientsDoingCastsAtLeastOne_INSTANCE.buildGroup(this);
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
