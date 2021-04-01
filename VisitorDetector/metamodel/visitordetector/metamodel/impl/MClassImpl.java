package visitordetector.metamodel.impl;

import visitordetector.metamodel.entity.*;
import classes.NoOfDistinctClientsDoingCastsToMe;
import classes.medians.MedianOfCastsToEachOfMyDescendants;
import classes.medians.MedianOfDistinctClientsDoingAtLeastOneCastToMyDescendants3;
import classes.means.ArithmeticAverageOfDistinctClientsDoingCastsToMyDescendants2;
import classes.means.ArithmeticAverageOfDistinctClientsDoingCastsToMyDescendants1;
import classes.means.ArithmeticAverageOfDistinctClientsDoingCastsToMyDescendants3;
import classes.PercentageOfClientsDoingCastsToMyDescendants;
import classes.means.ArithmeticAverageOfDistinctClientsDoingAtLeastOneCastToMyDescendants3;
import classes.means.ArithmeticAverageOfDistinctClientsDoingAtLeastOneCastToMyDescendants2;
import classes.SumOfCastsToEachOfMyDescendants;
import classes.means.ArithmeticAverageOfDistinctClientsDoingAtLeastOneCastToMyDescendants1;
import classes.NoOfCastsToMe;
import classes.medians.MedianOfDistinctClientsDoingAtLeastOneCastToMyDescendants2;
import classes.medians.MedianOfDistinctClientsDoingAtLeastOneCastToMyDescendants1;
import classes.medians.MedianOfDistinctClientsDoingCastsToMyDescendants1;
import classes.medians.MedianOfDistinctClientsDoingCastsToMyDescendants2;
import classes.medians.MedianOfDistinctClientsDoingCastsToMyDescendants3;
import classes.NoOfDistinctClientsDoingCastsToEachOfMyDescendants;
import classes.ToString;
import classes.clients.MyClientsWithZeroCastsToMyDescendants1;
import classes.clients.MyClientsWithZeroCastsToMyDescendants2;
import classes.clients.MyClientsWithZeroCastsToMyDescendants3;
import classes.clients.MyClients2;
import classes.DescendantsGroup;
import classes.clients.MyClients1;
import classes.DistinctMethodsWithCastsToMyDescendants;
import classes.clients.MyClients3;
import classes.DistinctMethodsWithCastsToMe;
import classes.clients.MyClientsWithAtLeastOneCastToMyDescendants3;
import classes.AscendantsGroup;
import classes.clients.MyClientsWithAtLeastOneCastToMyDescendants2;
import classes.clients.MyDescendantsClients;
import classes.clients.MyClientsWithAtLeastOneCastToMyDescendants1;
import classes.MethodGroup;
import classes.ShowInEditor;

public class MClassImpl implements MClass {

	private org.eclipse.jdt.core.IType underlyingObj_;

	private static final NoOfDistinctClientsDoingCastsToMe NoOfDistinctClientsDoingCastsToMe_INSTANCE = new NoOfDistinctClientsDoingCastsToMe();
	private static final MedianOfCastsToEachOfMyDescendants MedianOfCastsToEachOfMyDescendants_INSTANCE = new MedianOfCastsToEachOfMyDescendants();
	private static final MedianOfDistinctClientsDoingAtLeastOneCastToMyDescendants3 MedianOfDistinctClientsDoingAtLeastOneCastToMyDescendants3_INSTANCE = new MedianOfDistinctClientsDoingAtLeastOneCastToMyDescendants3();
	private static final ArithmeticAverageOfDistinctClientsDoingCastsToMyDescendants2 ArithmeticAverageOfDistinctClientsDoingCastsToMyDescendants2_INSTANCE = new ArithmeticAverageOfDistinctClientsDoingCastsToMyDescendants2();
	private static final ArithmeticAverageOfDistinctClientsDoingCastsToMyDescendants1 ArithmeticAverageOfDistinctClientsDoingCastsToMyDescendants1_INSTANCE = new ArithmeticAverageOfDistinctClientsDoingCastsToMyDescendants1();
	private static final ArithmeticAverageOfDistinctClientsDoingCastsToMyDescendants3 ArithmeticAverageOfDistinctClientsDoingCastsToMyDescendants3_INSTANCE = new ArithmeticAverageOfDistinctClientsDoingCastsToMyDescendants3();
	private static final PercentageOfClientsDoingCastsToMyDescendants PercentageOfClientsDoingCastsToMyDescendants_INSTANCE = new PercentageOfClientsDoingCastsToMyDescendants();
	private static final ArithmeticAverageOfDistinctClientsDoingAtLeastOneCastToMyDescendants3 ArithmeticAverageOfDistinctClientsDoingAtLeastOneCastToMyDescendants3_INSTANCE = new ArithmeticAverageOfDistinctClientsDoingAtLeastOneCastToMyDescendants3();
	private static final ArithmeticAverageOfDistinctClientsDoingAtLeastOneCastToMyDescendants2 ArithmeticAverageOfDistinctClientsDoingAtLeastOneCastToMyDescendants2_INSTANCE = new ArithmeticAverageOfDistinctClientsDoingAtLeastOneCastToMyDescendants2();
	private static final SumOfCastsToEachOfMyDescendants SumOfCastsToEachOfMyDescendants_INSTANCE = new SumOfCastsToEachOfMyDescendants();
	private static final ArithmeticAverageOfDistinctClientsDoingAtLeastOneCastToMyDescendants1 ArithmeticAverageOfDistinctClientsDoingAtLeastOneCastToMyDescendants1_INSTANCE = new ArithmeticAverageOfDistinctClientsDoingAtLeastOneCastToMyDescendants1();
	private static final NoOfCastsToMe NoOfCastsToMe_INSTANCE = new NoOfCastsToMe();
	private static final MedianOfDistinctClientsDoingAtLeastOneCastToMyDescendants2 MedianOfDistinctClientsDoingAtLeastOneCastToMyDescendants2_INSTANCE = new MedianOfDistinctClientsDoingAtLeastOneCastToMyDescendants2();
	private static final MedianOfDistinctClientsDoingAtLeastOneCastToMyDescendants1 MedianOfDistinctClientsDoingAtLeastOneCastToMyDescendants1_INSTANCE = new MedianOfDistinctClientsDoingAtLeastOneCastToMyDescendants1();
	private static final MedianOfDistinctClientsDoingCastsToMyDescendants1 MedianOfDistinctClientsDoingCastsToMyDescendants1_INSTANCE = new MedianOfDistinctClientsDoingCastsToMyDescendants1();
	private static final MedianOfDistinctClientsDoingCastsToMyDescendants2 MedianOfDistinctClientsDoingCastsToMyDescendants2_INSTANCE = new MedianOfDistinctClientsDoingCastsToMyDescendants2();
	private static final MedianOfDistinctClientsDoingCastsToMyDescendants3 MedianOfDistinctClientsDoingCastsToMyDescendants3_INSTANCE = new MedianOfDistinctClientsDoingCastsToMyDescendants3();
	private static final NoOfDistinctClientsDoingCastsToEachOfMyDescendants NoOfDistinctClientsDoingCastsToEachOfMyDescendants_INSTANCE = new NoOfDistinctClientsDoingCastsToEachOfMyDescendants();
	private static final ToString ToString_INSTANCE = new ToString();
	private static final MyClientsWithZeroCastsToMyDescendants1 MyClientsWithZeroCastsToMyDescendants1_INSTANCE = new MyClientsWithZeroCastsToMyDescendants1();
	private static final MyClientsWithZeroCastsToMyDescendants2 MyClientsWithZeroCastsToMyDescendants2_INSTANCE = new MyClientsWithZeroCastsToMyDescendants2();
	private static final MyClientsWithZeroCastsToMyDescendants3 MyClientsWithZeroCastsToMyDescendants3_INSTANCE = new MyClientsWithZeroCastsToMyDescendants3();
	private static final MyClients2 MyClients2_INSTANCE = new MyClients2();
	private static final DescendantsGroup DescendantsGroup_INSTANCE = new DescendantsGroup();
	private static final MyClients1 MyClients1_INSTANCE = new MyClients1();
	private static final DistinctMethodsWithCastsToMyDescendants DistinctMethodsWithCastsToMyDescendants_INSTANCE = new DistinctMethodsWithCastsToMyDescendants();
	private static final MyClients3 MyClients3_INSTANCE = new MyClients3();
	private static final DistinctMethodsWithCastsToMe DistinctMethodsWithCastsToMe_INSTANCE = new DistinctMethodsWithCastsToMe();
	private static final MyClientsWithAtLeastOneCastToMyDescendants3 MyClientsWithAtLeastOneCastToMyDescendants3_INSTANCE = new MyClientsWithAtLeastOneCastToMyDescendants3();
	private static final AscendantsGroup AscendantsGroup_INSTANCE = new AscendantsGroup();
	private static final MyClientsWithAtLeastOneCastToMyDescendants2 MyClientsWithAtLeastOneCastToMyDescendants2_INSTANCE = new MyClientsWithAtLeastOneCastToMyDescendants2();
	private static final MyDescendantsClients MyDescendantsClients_INSTANCE = new MyDescendantsClients();
	private static final MyClientsWithAtLeastOneCastToMyDescendants1 MyClientsWithAtLeastOneCastToMyDescendants1_INSTANCE = new MyClientsWithAtLeastOneCastToMyDescendants1();
	private static final MethodGroup MethodGroup_INSTANCE = new MethodGroup();
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
	public java.lang.Double medianOfDistinctClientsDoingAtLeastOneCastToMyDescendants3() {
		return MedianOfDistinctClientsDoingAtLeastOneCastToMyDescendants3_INSTANCE.compute(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Double arithmeticAverageOfDistinctClientsDoingCastsToMyDescendants2() {
		return ArithmeticAverageOfDistinctClientsDoingCastsToMyDescendants2_INSTANCE.compute(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Double arithmeticAverageOfDistinctClientsDoingCastsToMyDescendants1() {
		return ArithmeticAverageOfDistinctClientsDoingCastsToMyDescendants1_INSTANCE.compute(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Double arithmeticAverageOfDistinctClientsDoingCastsToMyDescendants3() {
		return ArithmeticAverageOfDistinctClientsDoingCastsToMyDescendants3_INSTANCE.compute(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Double percentageOfClientsDoingCastsToMyDescendants() {
		return PercentageOfClientsDoingCastsToMyDescendants_INSTANCE.compute(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Double arithmeticAverageOfDistinctClientsDoingAtLeastOneCastToMyDescendants3() {
		return ArithmeticAverageOfDistinctClientsDoingAtLeastOneCastToMyDescendants3_INSTANCE.compute(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Double arithmeticAverageOfDistinctClientsDoingAtLeastOneCastToMyDescendants2() {
		return ArithmeticAverageOfDistinctClientsDoingAtLeastOneCastToMyDescendants2_INSTANCE.compute(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Integer sumOfCastsToEachOfMyDescendants() {
		return SumOfCastsToEachOfMyDescendants_INSTANCE.compute(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Double arithmeticAverageOfDistinctClientsDoingAtLeastOneCastToMyDescendants1() {
		return ArithmeticAverageOfDistinctClientsDoingAtLeastOneCastToMyDescendants1_INSTANCE.compute(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Integer noOfCastsToMe() {
		return NoOfCastsToMe_INSTANCE.compute(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Double medianOfDistinctClientsDoingAtLeastOneCastToMyDescendants2() {
		return MedianOfDistinctClientsDoingAtLeastOneCastToMyDescendants2_INSTANCE.compute(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Double medianOfDistinctClientsDoingAtLeastOneCastToMyDescendants1() {
		return MedianOfDistinctClientsDoingAtLeastOneCastToMyDescendants1_INSTANCE.compute(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Double medianOfDistinctClientsDoingCastsToMyDescendants1() {
		return MedianOfDistinctClientsDoingCastsToMyDescendants1_INSTANCE.compute(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Double medianOfDistinctClientsDoingCastsToMyDescendants2() {
		return MedianOfDistinctClientsDoingCastsToMyDescendants2_INSTANCE.compute(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Double medianOfDistinctClientsDoingCastsToMyDescendants3() {
		return MedianOfDistinctClientsDoingCastsToMyDescendants3_INSTANCE.compute(this);
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
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MMethod> myClientsWithZeroCastsToMyDescendants1() {
		return MyClientsWithZeroCastsToMyDescendants1_INSTANCE.buildGroup(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MMethod> myClientsWithZeroCastsToMyDescendants2() {
		return MyClientsWithZeroCastsToMyDescendants2_INSTANCE.buildGroup(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MMethod> myClientsWithZeroCastsToMyDescendants3() {
		return MyClientsWithZeroCastsToMyDescendants3_INSTANCE.buildGroup(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MMethod> myClients2() {
		return MyClients2_INSTANCE.buildGroup(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MClass> descendantsGroup() {
		return DescendantsGroup_INSTANCE.buildGroup(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MMethod> myClients1() {
		return MyClients1_INSTANCE.buildGroup(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MMethod> distinctMethodsWithCastsToMyDescendants() {
		return DistinctMethodsWithCastsToMyDescendants_INSTANCE.buildGroup(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MMethod> myClients3() {
		return MyClients3_INSTANCE.buildGroup(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MMethod> distinctMethodsWithCastsToMe() {
		return DistinctMethodsWithCastsToMe_INSTANCE.buildGroup(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MMethod> myClientsWithAtLeastOneCastToMyDescendants3() {
		return MyClientsWithAtLeastOneCastToMyDescendants3_INSTANCE.buildGroup(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MClass> ascendantsGroup() {
		return AscendantsGroup_INSTANCE.buildGroup(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MMethod> myClientsWithAtLeastOneCastToMyDescendants2() {
		return MyClientsWithAtLeastOneCastToMyDescendants2_INSTANCE.buildGroup(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MMethod> myDescendantsClients() {
		return MyDescendantsClients_INSTANCE.buildGroup(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MMethod> myClientsWithAtLeastOneCastToMyDescendants1() {
		return MyClientsWithAtLeastOneCastToMyDescendants1_INSTANCE.buildGroup(this);
	}

	@Override
	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MMethod> methodGroup() {
		return MethodGroup_INSTANCE.buildGroup(this);
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
