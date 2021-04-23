package visitordetector.metamodel.impl;

import visitordetector.metamodel.entity.*;
import classes.means.ArithmeticAverageOfDistinctClientsDoingAtLeastOneCastToMyDescendants;
import classes.PercentageOfClientsDoingCastsToMyDescendants;
import classes.medians.MedianOfDistinctClientsDoingAtLeastOneCastToMyDescendants;
import classes.medians.MedianOfDistinctClientsDoingCastsToMyDescendants;
import classes.ToString;
import classes.means.ArithmeticAverageOfDistinctClientsDoingCastsToMyDescendants;
import classes.AscendantsGroup;
import classes.DescendantsGroup;
import classes.MethodGroup;
import classes.clients.MyClients;
import classes.clients.MyClientsWithAtLeastOneCastToMyDescendants;
import classes.ShowInEditor;

public class MClassImpl implements MClass {

	private org.eclipse.jdt.core.IType underlyingObj_;

	private static final ArithmeticAverageOfDistinctClientsDoingAtLeastOneCastToMyDescendants ArithmeticAverageOfDistinctClientsDoingAtLeastOneCastToMyDescendants_INSTANCE = new ArithmeticAverageOfDistinctClientsDoingAtLeastOneCastToMyDescendants();
	private static final PercentageOfClientsDoingCastsToMyDescendants PercentageOfClientsDoingCastsToMyDescendants_INSTANCE = new PercentageOfClientsDoingCastsToMyDescendants();
	private static final MedianOfDistinctClientsDoingAtLeastOneCastToMyDescendants MedianOfDistinctClientsDoingAtLeastOneCastToMyDescendants_INSTANCE = new MedianOfDistinctClientsDoingAtLeastOneCastToMyDescendants();
	private static final MedianOfDistinctClientsDoingCastsToMyDescendants MedianOfDistinctClientsDoingCastsToMyDescendants_INSTANCE = new MedianOfDistinctClientsDoingCastsToMyDescendants();
	private static final ToString ToString_INSTANCE = new ToString();
	private static final ArithmeticAverageOfDistinctClientsDoingCastsToMyDescendants ArithmeticAverageOfDistinctClientsDoingCastsToMyDescendants_INSTANCE = new ArithmeticAverageOfDistinctClientsDoingCastsToMyDescendants();
	private static final AscendantsGroup AscendantsGroup_INSTANCE = new AscendantsGroup();
	private static final DescendantsGroup DescendantsGroup_INSTANCE = new DescendantsGroup();
	private static final MethodGroup MethodGroup_INSTANCE = new MethodGroup();
	private static final MyClients MyClients_INSTANCE = new MyClients();
	private static final MyClientsWithAtLeastOneCastToMyDescendants MyClientsWithAtLeastOneCastToMyDescendants_INSTANCE = new MyClientsWithAtLeastOneCastToMyDescendants();
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
	public java.lang.Double arithmeticAverageOfDistinctClientsDoingAtLeastOneCastToMyDescendants() {
		return ArithmeticAverageOfDistinctClientsDoingAtLeastOneCastToMyDescendants_INSTANCE.compute(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Double percentageOfClientsDoingCastsToMyDescendants() {
		return PercentageOfClientsDoingCastsToMyDescendants_INSTANCE.compute(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Double medianOfDistinctClientsDoingAtLeastOneCastToMyDescendants() {
		return MedianOfDistinctClientsDoingAtLeastOneCastToMyDescendants_INSTANCE.compute(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Double medianOfDistinctClientsDoingCastsToMyDescendants() {
		return MedianOfDistinctClientsDoingCastsToMyDescendants_INSTANCE.compute(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.String toString() {
		return ToString_INSTANCE.compute(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Double arithmeticAverageOfDistinctClientsDoingCastsToMyDescendants() {
		return ArithmeticAverageOfDistinctClientsDoingCastsToMyDescendants_INSTANCE.compute(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MClass> ascendantsGroup() {
		return AscendantsGroup_INSTANCE.buildGroup(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MClass> descendantsGroup() {
		return DescendantsGroup_INSTANCE.buildGroup(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MMethod> methodGroup() {
		return MethodGroup_INSTANCE.buildGroup(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MMethod> myClients() {
		return MyClients_INSTANCE.buildGroup(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MMethod> myClientsWithAtLeastOneCastToMyDescendants() {
		return MyClientsWithAtLeastOneCastToMyDescendants_INSTANCE.buildGroup(this);
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
