
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
		Assert.assertEquals(21, noOfCasts);
	}

	@Test
	public void testSumOfCastsToEachOfMyDescendants() {
		int noOfCasts = getType(types, "OriginalTest").sumOfCastsToEachOfMyDescendants();
		Assert.assertEquals(32, noOfCasts);
	}

	@Test
	public void testAscendants() {
		MClass type = getType(types, "Test1");
		Assert.assertEquals(1, type.ascendantsGroup().getElements().size());
	}

	@Test
	public void testDescendants() {
		MClass type = getType(types, "Test1");
		Assert.assertEquals(10, type.descendantsGroup().getElements().size());
	}

	@Test
	public void testMethodsOfAType() {
		MClass type = getType(types, "SomeClass");
		Assert.assertEquals(5, type.methodGroup().getElements().size());
	}

	@Test
	public void testDistinctMethodsWithCastsToMe() {
		MClass type = getType(types, "Test1");
		Assert.assertEquals(18, type.distinctMethodsWithCastsToMe().getElements().size());
	}

	@Test
	public void testDistinctMethodsWithCastsToMyDescendants() {
		MClass type = getType(types, "OriginalTest");
		Assert.assertEquals(25, type.distinctMethodsWithCastsToMyDescendants().getElements().size());
	}

	@Test
	public void testNoOfTypesWithAtLeastTwoDescendantsCasts() {
		Assert.assertEquals(3, project.classGroupWithAtLeastTwoCastsToTheirDescendants().getElements().size());
	}

	@Test
	public void testNoOfDistinctClientsDoingCastsToMe() {
		MClass type = getType(types, "Test1");
		Assert.assertEquals(18, type.noOfDistinctClientsDoingCastsToMe().intValue());
	}

	@Test
	public void testNoOfDistinctClientsDoingCastsToEachOfMyDescendants() {
		MClass type = getType(types, "OriginalTest");
		Assert.assertEquals(25, type.noOfDistinctClientsDoingCastsToEachOfMyDescendants().intValue());
	}

	@Test
	public void testMedianOfDistinctClientsDoingCastsToMyDescendants() {
		MClass type = getType(types, "Test1");
		Assert.assertEquals(new Double(0.0), type.medianOfDistinctClientsDoingCastsToMyDescendants1());
	}

	@Test
	public void testMedianOfDistinctClientsDoingCastsToMyDescendants2() {
		MClass type = getType(types, "Test1");
		Assert.assertEquals(new Double(0.0), type.medianOfDistinctClientsDoingCastsToMyDescendants2());
	}

	@Test
	public void testMedianOfDistinctClientsDoingCastsToMyDescendants3() {
		MClass type = getType(types, "Test1");
		Assert.assertEquals(new Double(0.0), type.medianOfDistinctClientsDoingCastsToMyDescendants3());
	}

	@Test
	public void testAverageCastsToEachOfMyDescendants() {
		MClass type = getType(types, "OriginalTest");
		Assert.assertEquals(new Double(0.0), type.medianOfCastsToEachOfMyDescendants());
	}

	@Test
	public void testClientsOfAType() {
		MClass type = getType(types, "Test1");
		Assert.assertEquals(51, type.myClients1().getElements().size());
	}

	@Test
	public void testClientsOfAType2() {
		MClass type = getType(types, "Test1");
		Assert.assertEquals(26, type.myClients2().getElements().size());
	}

	@Test
	public void testClientsOfAType3() {
		MClass type = getType(types, "Test1");
		Assert.assertEquals(30, type.myClients3().getElements().size());
	}

	@Test
	public void testClientsWithAtLeastOneCastToMyDescendants1() {
		MClass type = getType(types, "OriginalTest");
		Assert.assertEquals(17, type.myClientsWithAtLeastOneCastToMyDescendants1().getElements().size());
	}

	@Test
	public void testClientsWithAtLeastOneCastToMyDescendants2() {
		MClass type = getType(types, "Test1");
		Assert.assertEquals(2, type.myClientsWithAtLeastOneCastToMyDescendants2().getElements().size());
	}

	@Test
	public void testClientsWithAtLeastOneCastToMyDescendants3() {
		MClass type = getType(types, "Test1");
		Assert.assertEquals(6, type.myClientsWithAtLeastOneCastToMyDescendants3().getElements().size());
	}

	@Test
	public void testArithmeticAverageOfDistinctClientsDoingCastsToMyDescendants() {
		MClass type = getType(types, "OriginalTest");
		Double expected = 21.0 / 19.0;
		Assert.assertEquals(expected, type.arithmeticAverageOfDistinctClientsDoingCastsToMyDescendants1());
	}

	@Test
	public void testArithmeticAverageOfDistinctClientsDoingCastsToMyDescendants2() {
		MClass type = getType(types, "Test1");
		Double expected = 5.0 / 26.0;
		Assert.assertEquals(expected, type.arithmeticAverageOfDistinctClientsDoingCastsToMyDescendants2());
	}

	@Test
	public void testArithmeticAverageOfDistinctClientsDoingCastsToMyDescendants3() {
		MClass type = getType(types, "Test1");
		Double expected = 9.0 / 30.0;
		Assert.assertEquals(expected, type.arithmeticAverageOfDistinctClientsDoingCastsToMyDescendants3());
	}
}
