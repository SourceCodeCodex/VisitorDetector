package system;

import java.util.List;

import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import visitordetector.metamodel.entity.MClass;
import visitordetector.metamodel.entity.MSystem;

@RelationBuilder
public class ClassGroupWithAtLeastTwoDescendantsCasts implements IRelationBuilder<MClass, MSystem> {

	@Override
	public Group<MClass> buildGroup(MSystem arg0) {
		Group<MClass> types = new Group<>();
		List<MClass> systemTypes = arg0.classGroup().getElements();
		for (MClass type : systemTypes) {
			if (type.noOfDescendantsCasts() >= 2) {
				types.add(type);
			}
		}
		return types;
	}

}
