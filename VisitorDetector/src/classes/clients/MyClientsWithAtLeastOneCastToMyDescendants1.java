package classes.clients;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.IType;

import ro.lrg.xcore.metametamodel.Group;
import ro.lrg.xcore.metametamodel.IRelationBuilder;
import ro.lrg.xcore.metametamodel.RelationBuilder;
import utils.CastSearchingUtils;
import visitordetector.metamodel.entity.MClass;
import visitordetector.metamodel.entity.MMethod;

@RelationBuilder
public class MyClientsWithAtLeastOneCastToMyDescendants1 implements IRelationBuilder<MMethod, MClass> {

	@Override
	public Group<MMethod> buildGroup(MClass arg0) {
		Group<MMethod> myClients = new Group<>();
		CastSearchingUtils csu = new CastSearchingUtils(arg0);
		List<MMethod> clients = arg0.myClients1().getElements().stream()
				.filter(client -> csu.containsDownCast(client.getUnderlyingObject())).collect(Collectors.toList());
		myClients.addAll(clients);
		return myClients;
	}
}
