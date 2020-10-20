package visitordetector.metamodel.entity;

public interface MSystem extends ro.lrg.xcore.metametamodel.XEntity {

	@ro.lrg.xcore.metametamodel.ThisIsAProperty
	public java.lang.String toString();

	@ro.lrg.xcore.metametamodel.ThisIsARelationBuilder
	public ro.lrg.xcore.metametamodel.Group<MClass> classGroup();

	org.eclipse.jdt.core.IJavaProject getUnderlyingObject();
}
