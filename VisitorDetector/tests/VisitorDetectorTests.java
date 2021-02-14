
import java.util.List;

import org.eclipse.jdt.core.IJavaProject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import utils.TestUtil;
import visitordetector.metamodel.entity.MClass;
import visitordetector.metamodel.entity.MSystem;
import visitordetector.metamodel.factory.Factory;

public class VisitorDetectorTests {
	private static List<MClass> types;
	private static MSystem project;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		TestUtil.importProject("Eval", "Eval.zip");
		IJavaProject javaProject = TestUtil.getProject("Eval");
		project = Factory.getInstance().createMSystem(javaProject);
		types = project.classGroup().getElements();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		TestUtil.deleteProject("Eval");
		types.clear();
	}

	public MClass getType(List<MClass> types, String typeName) {
		for (MClass type : types) {
			if (type.getUnderlyingObject().getElementName().equals(typeName))
				return type;
		}
		return null;
	}

	@Test
	public void testTypesInTheSystem() {
		Assert.assertEquals(14, types.size());
	}

	@Test
	public void testNoOfCastsToMe() {
		int noOfCasts = getType(types, "Test1").noOfCastsToMe();
		Assert.assertEquals(14, noOfCasts);
	}

	@Test
	public void testSumOfCastsToEachOfMyDescendants() {
		int noOfCasts = getType(types, "OriginalTest").sumOfCastsToEachOfMyDescendants();
		Assert.assertEquals(18, noOfCasts);
	}

	@Test
	public void testAscendants() {
		MClass type = getType(types, "Test1");
		Assert.assertEquals(1, type.ascendantsGroup().getElements().size());
	}

	@Test
	public void testDescendants() {
		MClass type = getType(types, "Test1");
		Assert.assertEquals(2, type.descendantsGroup().getElements().size());
	}

	@Test
	public void testMethodsOfAType() {
		MClass type = getType(types, "SomeClass");
		Assert.assertEquals(5, type.methodGroup().getElements().size());
	}

	@Test
	public void testdistinctMethodsWithCastsToMe() {
		MClass type = getType(types, "Test1");
		Assert.assertEquals(11, type.distinctMethodsWithCastsToMe().getElements().size());
	}

	@Test
	public void testdistinctMethodsWithCastsToMyDescendants() {
		MClass type = getType(types, "OriginalTest");
		Assert.assertEquals(12, type.distinctMethodsWithCastsToMyDescendants().getElements().size());
	}

	@Test
	public void testNoOfTypesWithAtLeastTwoDescendantsCasts() {
		Assert.assertEquals(3, project.classGroupWithAtLeastTwoCastsToTheirDescendants().getElements().size());
	}

	@Test
	public void testNoOfDistinctClientsDoingCastsToMe() {
		MClass type = getType(types, "Test1");
		Assert.assertEquals(11, type.noOfDistinctClientsDoingCastsToMe().intValue());
	}

	@Test
	public void testNoOfDistinctClientsDoingCastsToEachOfMyDescendants() {
		MClass type = getType(types, "OriginalTest");
		Assert.assertEquals(12, type.noOfDistinctClientsDoingCastsToEachOfMyDescendants().intValue());
	}

	@Test
	public void testAverageOfDistinctClietnsDoingCastsToMyDescendants() {
		MClass type = getType(types, "Test1");
		Assert.assertEquals(new Double(0.0), type.averageOfDistinctClientsDoingCastsToMyDescendants());
	}
	
	@Test
	public void testAverageOfDistinctClietnsDoingCastsToMyDescendants2() {
		MClass type = getType(types, "Test1");
		Assert.assertEquals(new Double(0.0), type.averageOfDistinctClientsDoingCastsToMyDescendants());
	}

	@Test
	public void testAverageCastsToEachOfMyDescendants() {
		MClass type = getType(types, "OriginalTest");
		Assert.assertEquals(new Double(3.0), type.averageCastsToEachOfMyDescendants());
	}

	@Test
	public void testClientsOfAType() {
		MClass type = getType(types, "Test1");
		Assert.assertEquals(33, type.myClients().getElements().size());
	}
	
	@Test
	public void testClientsOfAType2() {
		MClass type = getType(types, "Test1");
		Assert.assertEquals(14, type.myClients2().getElements().size());
		
	}
}
