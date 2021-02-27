package visitordetector.metamodel.impl;

import visitordetector.metamodel.entity.*;
import classes.NoOfDistinctClientsDoingCastsToMe;
import classes.MedianOfCastsToEachOfMyDescendants;
import classes.SumOfCastsToEachOfMyDescendants;
import classes.ArithmeticAverageOfDistinctClientsDoingCastsToMyDescendants;
import classes.NoOfCastsToMe;
import classes.MedianOfDistinctClientsDoingCastsToMyDescendants;
import classes.MedianOfDistinctClientsDoingCastsToMyDescendants2;
import classes.NoOfDistinctClientsDoingCastsToEachOfMyDescendants;
import classes.ToString;
import classes.ArithmeticAverageOfDistinctClientsDoingCastsToMyDescendants2;
import classes.DistinctMethodsWithCastsToMe;
import classes.MyClients2;
import classes.AscendantsGroup;
import classes.DescendantsGroup;
import classes.MyDescendantsClients;
import classes.DistinctMethodsWithCastsToMyDescendants;
import classes.MethodGroup;
import classes.MyClients;
import classes.ShowInEditor;

public class MClassImpl implements MClass {

	private org.eclipse.jdt.core.IType underlyingObj_;

	private static final NoOfDistinctClientsDoingCastsToMe NoOfDistinctClientsDoingCastsToMe_INSTANCE = new NoOfDistinctClientsDoingCastsToMe();
	private static final MedianOfCastsToEachOfMyDescendants MedianOfCastsToEachOfMyDescendants_INSTANCE = new MedianOfCastsToEachOfMyDescendants();
	private static final SumOfCastsToEachOfMyDescendants SumOfCastsToEachOfMyDescendants_INSTANCE = new SumOfCastsToEachOfMyDescendants();
	private static final ArithmeticAverageOfDistinctClientsDoingCastsToMyDescendants ArithmeticAverageOfDistinctClientsDoingCastsToMyDescendants_INSTANCE = new ArithmeticAverageOfDistinctClientsDoingCastsToMyDescendants();
	private static final NoOfCastsToMe NoOfCastsToMe_INSTANCE = new NoOfCastsToMe();
	private static final MedianOfDistinctClientsDoingCastsToMyDescendants MedianOfDistinctClientsDoingCastsToMyDescendants_INSTANCE = new MedianOfDistinctClientsDoingCastsToMyDescendants();
	private static final MedianOfDistinctClientsDoingCastsToMyDescendants2 MedianOfDistinctClientsDoingCastsToMyDescendants2_INSTANCE = new MedianOfDistinctClientsDoingCastsToMyDescendants2();
	private static final NoOfDistinctClientsDoingCastsToEachOfMyDescendants NoOfDistinctClientsDoingCastsToEachOfMyDescendants_INSTANCE = new NoOfDistinctClientsDoingCastsToEachOfMyDescendants();
	private static final ToString ToString_INSTANCE = new ToString();
	private static final ArithmeticAverageOfDistinctClientsDoingCastsToMyDescendants2 ArithmeticAverageOfDistinctClientsDoingCastsToMyDescendants2_INSTANCE = new ArithmeticAverageOfDistinctClientsDoingCastsToMyDescendants2();
	private static final DistinctMethodsWithCastsToMe DistinctMethodsWithCastsToMe_INSTANCE = new DistinctMethodsWithCastsToMe();
	private static final MyClients2 MyClients2_INSTANCE = new MyClients2();
	private static final AscendantsGroup AscendantsGroup_INSTANCE = new AscendantsGroup();
	private static final DescendantsGroup DescendantsGroup_INSTANCE = new DescendantsGroup();
	private static final MyDescendantsClients MyDescendantsClients_INSTANCE = new MyDescendantsClients();
	private static final DistinctMethodsWithCastsToMyDescendants DistinctMethodsWithCastsToMyDescendants_INSTANCE = new DistinctMethodsWithCastsToMyDescendants();
	private static final MethodGroup MethodGroup_INSTANCE = new MethodGroup();
	private static final MyClients MyClients_INSTANCE = new MyClients();
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
	public java.lang.Integer noOfDistinctClientsDoingCastsToMe() {
		return NoOfDistinctClientsDoingCastsToMe_INSTANCE.compute(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Double medianOfCastsToEachOfMyDescendants() {
		return MedianOfCastsToEachOfMyDescendants_INSTANCE.compute(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Integer sumOfCastsToEachOfMyDescendants() {
		return SumOfCastsToEachOfMyDescendants_INSTANCE.compute(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Double arithmeticAverageOfDistinctClientsDoingCastsToMyDescendants() {
		return ArithmeticAverageOfDistinctClientsDoingCastsToMyDescendants_INSTANCE.compute(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Integer noOfCastsToMe() {
		return NoOfCastsToMe_INSTANCE.compute(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Double medianOfDistinctClientsDoingCastsToMyDescendants() {
		return MedianOfDistinctClientsDoingCastsToMyDescendants_INSTANCE.compute(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Double medianOfDistinctClientsDoingCastsToMyDescendants2() {
		return MedianOfDistinctClientsDoingCastsToMyDescendants2_INSTANCE.compute(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Integer noOfDistinctClientsDoingCastsToEachOfMyDescendants() {
		return NoOfDistinctClientsDoingCastsToEachOfMyDescendants_INSTANCE.compute(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.String toString() {
		return ToString_INSTANCE.compute(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Double arithmeticAverageOfDistinctClientsDoingCastsToMyDescendants2() {
		return ArithmeticAverageOfDistinctClientsDoingCastsToMyDescendants2_INSTANCE.compute(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MMethod> distinctMethodsWithCastsToMe() {
		return DistinctMethodsWithCastsToMe_INSTANCE.buildGroup(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MMethod> myClients2() {
		return MyClients2_INSTANCE.buildGroup(this);
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
	public ro.lrg.xcore.metametamodel.Group<MMethod> myDescendantsClients() {
		return MyDescendantsClients_INSTANCE.buildGroup(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MMethod> distinctMethodsWithCastsToMyDescendants() {
		return DistinctMethodsWithCastsToMyDescendants_INSTANCE.buildGroup(this);
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
