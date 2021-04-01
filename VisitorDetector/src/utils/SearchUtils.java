package utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;

import visitordetector.metamodel.entity.MClass;

public class SearchUtils {
	private List<String> pFields;
	private List<SearchMatch> fieldsMatches;

	public SearchUtils() {
		pFields = new ArrayList<>();
		fieldsMatches = new ArrayList<>();
	}

	public List<SearchMatch> getMethodsMatchesUsingTheseFields(List<IField> fields, List<PseudoMethod> pMethods)
			throws CoreException {
		List<SearchMatch> matches = new LinkedList<>();
		List<String> uniqueResources = new ArrayList<>();
		for (IField field : fields) {
			SearchPattern pattern = SearchPattern.createPattern(field, IJavaSearchConstants.REFERENCES);
			IJavaSearchScope scope = SearchEngine.createWorkspaceScope();
			SearchRequestor requestor = new SearchRequestor() {
				@Override
				public void acceptSearchMatch(SearchMatch arg0) throws CoreException {
					String element = arg0.getElement().toString();
					if (!isMethod(element.split(" ")))
						return;
					try {
						PseudoMethod method = StringParser.parse(element);
						if (method != null && !pMethods.contains(method)) {
							pMethods.add(method);
						}
						String path = arg0.getResource().getProjectRelativePath().toOSString();
						if (!uniqueResources.contains(path)) {
							uniqueResources.add(path);
							matches.add(arg0);
						}
					} catch (Exception e) {
//						System.err.println("String Parser error ->" + element + " : " + e.getMessage());

					}
				}
			};
			SearchEngine searchEngine = new SearchEngine();
			searchEngine.search(pattern, new SearchParticipant[] { SearchEngine.getDefaultSearchParticipant() }, scope,
					requestor, new NullProgressMonitor());
		}
		return matches;
	}

	public List<IField> getFields(IType baseType) throws JavaModelException {
		List<IField> fields = new LinkedList<>();
		for (SearchMatch match : fieldsMatches) {
			IJavaElement element = JavaCore.create(match.getResource());
			if (element.getElementType() == IJavaElement.COMPILATION_UNIT) {
				IType[] types = ((ICompilationUnit) element).getAllTypes();
				for (IType type : types) {
					List<String> remainingFields = new LinkedList<>();
					for (String pField : pFields) {
						boolean added = false;
						for (IField field : type.getFields()) {
							if (pField.equals(StringParser.parseField(field.toString()))) {
								String fieldInfo = field.getTypeSignature();
								if (fieldInfo.contains(baseType.getElementName()))
									fields.add(field);
								added = true;
								break;
							}
						}
						if (added == false) {
							remainingFields.add(pField);
						}
					}
					pFields = remainingFields;
					if (pFields.size() == 0)
						return fields;
				}
			}
		}
		return fields;
	}

	public List<SearchMatch> getMethodsMatches(MClass arg0, List<PseudoMethod> pMethods) throws CoreException {
		List<SearchMatch> matches = new LinkedList<>();
		List<String> uniqueResources = new ArrayList<>();
		List<String> uniqueFieldsResources = new ArrayList<>();
		SearchPattern pattern = SearchPattern.createPattern(arg0.getUnderlyingObject(),
				IJavaSearchConstants.REFERENCES);
		IJavaSearchScope scope = SearchEngine.createWorkspaceScope();
		SearchRequestor requestor = new SearchRequestor() {
			@Override
			public void acceptSearchMatch(SearchMatch arg0) throws CoreException {

				String element = arg0.getElement().toString();
				if (!isMethod(element.split(" ")))
					return;
				try {
					PseudoMethod method = StringParser.parse(element);
					if (method != null && !pMethods.contains(method)) {
						pMethods.add(method);
					}
					String path = arg0.getResource().getProjectRelativePath().toOSString();
					if (!uniqueResources.contains(path)) {
						uniqueResources.add(path);
						matches.add(arg0);
					}
				} catch (Exception e) {
					// System.err.println("Method parsing ->" + element);
					String field = StringParser.parseField(element);
					if (!pFields.contains(field))
						pFields.add(StringParser.parseField(element));
					String path = arg0.getResource().getProjectRelativePath().toOSString();
					if (!uniqueFieldsResources.contains(path)) {
						uniqueFieldsResources.add(path);
						fieldsMatches.add(arg0);
					}
				}
			}
		};
		SearchEngine searchEngine = new SearchEngine();
		searchEngine.search(pattern, new SearchParticipant[] { SearchEngine.getDefaultSearchParticipant() }, scope,
				requestor, new NullProgressMonitor());
		return matches;
	}

	public List<IMethod> getMethods(List<SearchMatch> matches, List<PseudoMethod> pMethods) throws JavaModelException {
		List<IMethod> methods = new LinkedList<>();
		for (SearchMatch match : matches) {
			IJavaElement element = JavaCore.create(match.getResource());
			if (element.getElementType() == IJavaElement.COMPILATION_UNIT) {
				IType[] types = ((ICompilationUnit) element).getAllTypes();
				for (IType type : types) {
					List<PseudoMethod> remainingMethods = new LinkedList<>();
					for (PseudoMethod pMethod : pMethods) {
						boolean added = false;
						if (pMethod.verifyClass(type.getFullyQualifiedName())) {
							for (IMethod method : type.getMethods()) {
								if (pMethod.verifyMethod(method)) {
									methods.add(method);
									added = true;
									break;
								}
							}
						}
						if (added == false) {
							remainingMethods.add(pMethod);
						}
					}
					pMethods = remainingMethods;
					if (pMethods.size() == 0)
						return methods;
				}
			}

		}

		return methods;
	}

	private boolean isMethod(String[] elements) {
		return !elements[0].equals("import") && !elements[0].equals("class");
	}

	public static String getFieldInfo(IField field) {
		String fieldInfo = field.toString();
		return fieldInfo.substring(fieldInfo.lastIndexOf(field.getElementName()));
	}

	public static boolean containsType(String type1Info, String type2Info) {
		if (!type1Info.contains(type2Info))
			return false;
		int pos = type1Info.indexOf(type2Info) + type2Info.length();
		char ch = type1Info.charAt(pos);
		return !Character.isLetterOrDigit(ch) && ch != '$';
	}

	public static boolean visitMethod(IMethod method, Expression expr, IType analyzedType, String fullyQualifiedName)
			throws JavaModelException {
		if (Flags.isStatic(method.getFlags()))
			return false;
		if (expr == null) {
			if (Utils.getParentFullyQualifiedName(method).equals(analyzedType.getFullyQualifiedName()))
				return true;
			return false;
		}
		IJavaElement element = expr.resolveTypeBinding().getJavaElement();
		if (SearchUtils.containsType(element.toString(), fullyQualifiedName))
			return true;
		return false;
	}
}
