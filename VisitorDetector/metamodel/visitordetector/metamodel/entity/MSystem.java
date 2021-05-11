package visitordetector.metamodel.entity;

public interface MSystem extends ro.lrg.xcore.metametamodel.XEntity {

	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Long noOfCodeLines();

	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.Integer noOfClasses();

	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.String toString();

	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MClass> classGroup();

	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MClass> typesThatYouShouldUseVisitorFor();

	org.eclipse.jdt.core.IJavaProject getUnderlyingObject();
}
