package visitordetector.metamodel.entity;

public interface MClass extends ro.lrg.xcore.metametamodel.XEntity {

	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Integer noOfDistinctClientsDoingCastsToMe();

	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Double arithmeticAverageOfDistinctClientsDoingAtLeastOneCastToMyDescendants3();

	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Double arithmeticAverageOfDistinctClientsDoingAtLeastOneCastToMyDescendants2();

	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Integer sumOfCastsToEachOfMyDescendants();

	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Double arithmeticAverageOfDistinctClientsDoingAtLeastOneCastToMyDescendants1();

	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Double medianOfCastsToEachOfMyDescendants();

	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Double medianOfDistinctClientsDoingAtLeastOneCastToMyDescendants3();

	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Integer noOfCastsToMe();

	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Double medianOfDistinctClientsDoingAtLeastOneCastToMyDescendants2();

	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Double medianOfDistinctClientsDoingAtLeastOneCastToMyDescendants1();

	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Double medianOfDistinctClientsDoingCastsToMyDescendants1();

	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Double medianOfDistinctClientsDoingCastsToMyDescendants2();

	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Double medianOfDistinctClientsDoingCastsToMyDescendants3();

	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Integer noOfDistinctClientsDoingCastsToEachOfMyDescendants();

	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.String toString();

	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Double arithmeticAverageOfDistinctClientsDoingCastsToMyDescendants2();

	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Double arithmeticAverageOfDistinctClientsDoingCastsToMyDescendants1();

	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Double arithmeticAverageOfDistinctClientsDoingCastsToMyDescendants3();

	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MMethod> myClientsWithZeroCastsToMyDescendants1();

	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MMethod> myClientsWithZeroCastsToMyDescendants2();

	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MMethod> myClientsWithZeroCastsToMyDescendants3();

	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MMethod> myClients2();

	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MClass> descendantsGroup();

	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MMethod> myClients1();

	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MMethod> distinctMethodsWithCastsToMyDescendants();

	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MMethod> myClients3();

	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MMethod> distinctMethodsWithCastsToMe();

	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MMethod> myClientsWithAtLeastOneCastToMyDescendants3();

	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MClass> ascendantsGroup();

	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MMethod> myClientsWithAtLeastOneCastToMyDescendants2();

	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MMethod> myDescendantsClients();

	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MMethod> myClientsWithAtLeastOneCastToMyDescendants1();

	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MMethod> methodGroup();

	@ro.lrg.xcore.metametamodel.ThisIsAnAction(numParams = 0) 
	public void showInEditor ();

	org.eclipse.jdt.core.IType getUnderlyingObject();
}
