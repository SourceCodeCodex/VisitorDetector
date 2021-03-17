package classes.clients;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.JavaModelException;

import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import utils.Utils;
import visitordetector.metamodel.entity.MClass;
import visitordetector.metamodel.entity.MMethod;

@RelationBuilder
public class MyDescendantsClients implements IRelationBuilder<MMethod, MClass> {

	@Override
	public Group<MMethod> buildGroup(MClass arg0) {
		Group<MMethod> clients = new Group<>();
		List<MClass> descendants = arg0.descendantsGroup().getElements();
		List<MMethod> methodsWithoutDuplicates = new LinkedList<>();
		for (MClass descendent : descendants) {
			try {
				methodsWithoutDuplicates = Utils.removeDuplicates(descendent.myClients1().getElements(),
						methodsWithoutDuplicates);
			} catch (JavaModelException e) {
				System.err.println("MMethod - MClass -> MyDescendantsClients:" + e.getMessage());
			}
		}
		clients.addAll(methodsWithoutDuplicates);
		return clients;
	}

}
