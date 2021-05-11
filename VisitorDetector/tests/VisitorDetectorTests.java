
import java.util.List;

import org.eclipse.jdt.core.IJavaProject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
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
		Assert.assertEquals(
				"[SomeClass, another.Main, another.Cat, another.Animal, another.Dog, complex.Dummy, test.Test2, test.OriginalTest, test.Test3, test.Test1, test.Test1$Test5, test.Test1$Test5$Test4, test.random.Test3, test.random.Zuzu]",
				types.toString());
	}
	
	@Test
	public void testNoCodeLines() {
		Assert.assertEquals(new Long(517), project.noOfCodeLines());
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
		Assert.assertEquals(
				"[complex.Dummy$6, complex.Dummy$5, complex.Dummy$4$1, complex.Dummy$4, complex.Dummy$3$1, complex.Dummy$3, complex.Dummy$2, complex.Dummy$1, test.Test1$Test5$Test4, test.Test3]",
				type.descendantsGroup().getElements().toString());
	}

	@Test
	public void testMethodsOfAType() {
		MClass type = getType(types, "SomeClass");
		Assert.assertEquals(5, type.methodGroup().getElements().size());
	}

	@Test
	public void testMedianOfDistinctClientsDoingCastsToMyDescendants() {
		MClass type = getType(types, "Test1");
		Assert.assertEquals(new Double(0.0), type.medianOfDistinctClientsDoingCastsToMyDescendants());
	}
	
	@Test
	public void testMedianOfDistinctClientsDoingCastsToAtLeastOneOfMyDescendants() {
		MClass type = getType(types, "Test1");
		Assert.assertEquals(new Double(1.0), type.medianOfDistinctClientsDoingAtLeastOneCastToMyDescendants());
	}

	@Test
	public void testClientsOfAType1() {
		MClass type = getType(types, "Test1");
		Assert.assertEquals(34, type.myClients().getElements().size());
		Assert.assertEquals(
				"[SomeClass, castSomething, casts, method7, method1, method2, method3, method10, method14, method15, method24, method25, method26, method27, method28, method29, method30, metoda, method32, metd, metoda, metoda, methoodas, method36, method37, method39, method, met, testo, method20, bMethod, dMethod, castSomething, castSomething]",
				type.myClients().getElements().toString());
	}

	@Test
	public void testClientsOfAType2() {
		MClass type = getType(types, "OriginalTest");
		Assert.assertEquals(22, type.myClients().getElements().size());
		Assert.assertEquals(
				"[SomeClass, castSomething, casts, method1, method2, method3, method4, method10, method24, metoda, metoda, method32, metd, metoda, metoda, methoodas, method36, methdo38, method39, testo, castSomething, castSomething]",
				type.myClients().getElements().toString());
	}

	@Test
	public void testClientsWithAtLeastOneCastToMyDescendants1() {
		MClass type = getType(types, "Test1");
		Assert.assertEquals(7, type.myClientsWithAtLeastOneCastToMyDescendants().getElements().size());
		Assert.assertEquals("[SomeClass, method25, method27, method28, method30, method37, met]",
				type.myClientsWithAtLeastOneCastToMyDescendants().getElements().toString());
	}

	@Test
	public void testClientsWithAtLeastOneCastToMyDescendants2() {
		MClass type = getType(types, "OriginalTest");
		Assert.assertEquals(21, type.myClientsWithAtLeastOneCastToMyDescendants().getElements().size());
		Assert.assertEquals(
				"[SomeClass, castSomething, casts, method1, method2, method3, method4, method10, method24, metoda, method32, metd, metoda, metoda, methoodas, method36, methdo38, method39, testo, castSomething, castSomething]",
				type.myClientsWithAtLeastOneCastToMyDescendants().getElements().toString());
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
}
