package system;

import java.util.List;

import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import visitordetector.metamodel.entity.MClass;
import visitordetector.metamodel.entity.MSystem;

@RelationBuilder
public class TypesThatYouShouldUseVisitorFor implements IRelationBuilder<MClass, MSystem> {

	@Override
	public Group<MClass> buildGroup(MSystem arg0) {
		Group<MClass> types = new Group<>();
		List<MClass> systemTypes = arg0.classGroup().getElements();
		for (MClass type : systemTypes) {
			if (type.shouldYouUseVisitorForThisType().equals("Yes")) {
				types.add(type);
			}
		}
		return types;
	}

}
