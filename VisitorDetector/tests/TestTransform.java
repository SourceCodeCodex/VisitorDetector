

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import utils.PseudoMethod;

public class TestTransform {
	private static PseudoMethod ps;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ps = new PseudoMethod("", "", "", null);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		ps = null;
	}

	@Test
	public void test1() {
		String s = "[QHashMap<[IQHashMap<[[IQMap<QInteger;[[D>;[IQDouble;>;>;";
		String expected = "HashMap<int[],HashMap<int[][],Map<Integer,double[][]>,int[],Double>>[]";
		String actual = ps.transform(s);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test2() {
		String s = "QInteger;";
		String expected = "Integer";
		String actual = ps.transform(s);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test3() {
		String s = "[QInteger;";
		String expected = "Integer[]";
		String actual = ps.transform(s);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test4() {
		String s = "QMap<[[[QInteger;[QString;>;";
		String expected = "Map<Integer[][][],String[]>";
		String actual = ps.transform(s);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test5() {
		String s = "QList<[I>;";
		String expected = "List<int[]>";
		String actual = ps.transform(s);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test6() {
		String s = "QHashMap<[[IQDouble;>;";
		String expected = "HashMap<int[][],Double>";
		String actual = ps.transform(s);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test7() {
		String s = "QMap<QDouble;QString;[I>;";
		String expected = "Map<Double,String,int[]>";
		String actual = ps.transform(s);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test8() {
		String s = "QMap<[I[D[S>;";
		String expected = "Map<int[],double[],short[]>";
		String actual = ps.transform(s);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test9() {
		String s = "QMap<[I[D[SQString;>;";
		String expected = "Map<int[],double[],short[],String>";
		String actual = ps.transform(s);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test11() {
		String s = "QSubscriber<-QT;QString;>;";
		String expected = "Subscriber<? super T,String>";
		String actual = ps.transform(s);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test12() {
		String s = "QSubscriber<+QT;>;";
		String expected = "Subscriber<? extends T>";
		String actual = ps.transform(s);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test13() {
		String s = "[QSubscriber<-[QT;+QT;>;";
		String expected = "Subscriber<? super T[],? extends T>[]";
		String actual = ps.transform(s);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test14() {
		String s = "[QSubscriber<QT;>;";
		String expected = "Subscriber<T>[]";
		String actual = ps.transform(s);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test15() {
		String s = "QFunction<-QT;+QObservableSource<+QR;>;>;";
		String expected = "Function<? super T,? extends ObservableSource<? extends R>>";
		String actual = ps.transform(s);
		Assert.assertEquals(expected, actual);
	}

}
