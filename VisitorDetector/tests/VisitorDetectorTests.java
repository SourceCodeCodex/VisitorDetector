
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

	// @Test
	// public void testMedianOfDistinctClientsDoingCastsToMyDescendants3() {
	// MClass type = getType(types, "Test1");
	// Assert.assertEquals(new Double(0.0),
	// type.medianOfDistinctClientsDoingCastsToMyDescendants3());
	// }

	@Test
	public void testClientsOfAType1() {
		MClass type = getType(types, "Test1");
		Assert.assertEquals(34, type.myClients().getElements().size());
	}

	@Test
	public void testClientsOfAType2() {
		MClass type = getType(types, "OriginalTest");
		Assert.assertEquals(22, type.myClients().getElements().size());
	}

	@Test
	public void testClientsWithAtLeastOneCastToMyDescendants1() {
		MClass type = getType(types, "Test1");
		Assert.assertEquals(7, type.myClientsWithAtLeastOneCastToMyDescendants().getElements().size());
	}

	@Test
	public void testClientsWithAtLeastOneCastToMyDescendants2() {
		MClass type = getType(types, "OriginalTest");
		Assert.assertEquals(21, type.myClientsWithAtLeastOneCastToMyDescendants().getElements().size());
	}

	@Test
	public void testArithmeticAverageOfDistinctClientsDoingCastsToMyDescendants1() {
		MClass type = getType(types, "Test1");
		Double expected = 7.0 / 34.0;
		Assert.assertEquals(expected, type.arithmeticAverageOfDistinctClientsDoingCastsToMyDescendants());
	}

	@Test
	public void testArithmeticAverageOfDistinctClientsDoingCastsToMyDescendants2() {
		MClass type = getType(types, "OriginalTest");
		Double expected = 25.0 / 22.0;
		Assert.assertEquals(expected, type.arithmeticAverageOfDistinctClientsDoingCastsToMyDescendants());
	}

	@Test
	public void testArithmeticAverageOfDistinctClientsDoingAtLeastOneCastToMyDescendants1() {
		MClass type = getType(types, "Test1");
		Double expected = 7.0 / 7.0;
		Assert.assertEquals(expected, type.arithmeticAverageOfDistinctClientsDoingAtLeastOneCastToMyDescendants());
	}

	@Test
	public void testArithmeticAverageOfDistinctClientsDoingAtLeastOneCastToMyDescendants2() {
		MClass type = getType(types, "OriginalTest");
		Double expected = 25.0 / 21.0;
		Assert.assertEquals(expected, type.arithmeticAverageOfDistinctClientsDoingAtLeastOneCastToMyDescendants());
	}
	//
	// @Test
	// public void testPercentageOfClientsDoingCastsToMyDescendants() {
	// MClass type = getType(types, "Test1");
	// Assert.assertEquals(new Double(20.0),
	// type.percentageOfClientsDoingCastsToMyDescendants());
	// }
	//
	// @Test
	// public void testClassGroupWithPercentageOfClientsDoingCastsAtLeastOne() {
	// Assert.assertEquals(2,
	// project.classGroupWithPercentageOfClientsDoingCastsAtLeastOne().getElements().size());
	// }
}
