
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
	public void testNoOfCastsOne() {
		int noOfCasts = getType(types, "Test1").noOfCasts();
		Assert.assertEquals(12, noOfCasts);
	}

	@Test
	public void testNoOfDescendantCasts() {
		int noOfCasts = getType(types, "OriginalTest").noOfDescendantsCasts();
		Assert.assertEquals(13, noOfCasts);
	}

	@Test
	public void testSupertypesOfAType() {
		MClass type = getType(types, "Test1");
		Assert.assertEquals(1, type.superGroup().getElements().size());
	}

	@Test
	public void testSubtypesOfAType() {
		MClass type = getType(types, "Test1");
		Assert.assertEquals(2, type.subGroup().getElements().size());
	}

	@Test
	public void testMethodsOfAType() {
		MClass type = getType(types, "SomeClass");
		Assert.assertEquals(5, type.methodGroup().getElements().size());
	}

	@Test
	public void testMethodsWithCastsForAType() {
		MClass type = getType(types, "Test1");
		Assert.assertEquals(9, type.methodsWithCasts().getElements().size());
	}

	@Test
	public void testMethodsWithDescendantsCastsForAType() {
		MClass type = getType(types, "OriginalTest");
		Assert.assertEquals(9, type.methodsWithDescendantsCasts().getElements().size());
	}

	@Test
	public void testNoOfTypesWithAtLeastTwoDescendantsCasts() {
		Assert.assertEquals(2, project.classGroupWithAtLeastTwoDescendantsCasts().getElements().size());
	}

}
