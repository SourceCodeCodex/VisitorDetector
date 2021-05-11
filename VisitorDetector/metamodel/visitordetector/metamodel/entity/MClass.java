package visitordetector.metamodel.entity;

public interface MClass extends ro.lrg.xcore.metametamodel.XEntity {

	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Double arithmeticAverageOfDistinctClientsDoingAtLeastOneCastToMyDescendants();

	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Double percentageOfClientsDoingCastsToMyDescendants();

	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Double medianOfDistinctClientsDoingAtLeastOneCastToMyDescendants();

	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.String shouldYouUseVisitorForThisType();

	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Double arithmeticAverageOfDistinctClientsDoingCastsToMyDescendants();

	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Double medianOfDistinctClientsDoingCastsToMyDescendants();

	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.String toString();

	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MClass> ascendantsGroup();

	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MClass> descendantsGroup();

	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MMethod> methodGroup();

	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MMethod> myClients();

	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MMethod> myClientsWithAtLeastOneCastToMyDescendants();

	@ro.lrg.xcore.metametamodel.ThisIsAnAction(numParams = 0) 
	public void showInEditor ();

	org.eclipse.jdt.core.IType getUnderlyingObject();
}
