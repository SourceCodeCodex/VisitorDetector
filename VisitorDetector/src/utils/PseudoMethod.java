package utils;

import org.eclipse.jdt.core.IMethod;

public class PseudoMethod {
	private String name;
	private String containingClass;
	private String containingPackage;
	private String[] parameterTypes;

	public PseudoMethod(String name, String containingClass, String containingPackage, String[] parameterTypes) {
		this.name = assignName(name, containingClass);
		this.containingClass = containingClass;
		this.containingPackage = containingPackage;
		this.parameterTypes = assignParameterTypes(parameterTypes);
	}

	private String assignName(String name, String containingClass) {
		if (name.equals(""))
			name = containingClass;
		if (name.contains("$"))
			return name.substring(name.lastIndexOf('$') + 1);
		return name;
	}

	private String[] assignParameterTypes(String[] parameterTypes) {
		if (parameterTypes == null)
			return new String[0];
		return parameterTypes;
	}

	public boolean equals(Object o) {
		if (o instanceof PseudoMethod) {
			PseudoMethod p = (PseudoMethod) o;
			if (!name.equals(p.name) || !containingClass.equals(p.containingClass)
					|| !containingPackage.equals(p.containingPackage)
					|| !(parameterTypes.length == p.parameterTypes.length))
				return false;
			for (int index = 0; index < parameterTypes.length; index++) {
				if (!parameterTypes[index].equals(p.parameterTypes[index]))
					return false;
			}
			return true;
		}
		return false;
	}

	public String toString() {
		String s = "";
		s += name + ":" + containingClass + ":" + containingPackage + "\n";
		for (String param : parameterTypes) {
			s += param + " ";
		}
		return s;
	}

	public boolean verifyClass(String fullyQualifiedName) {
		String full;
		full = getFullyQualifiedName();
		// System.out.println(fullyQualifiedName);
		// System.out.println(full);
		return full.equals(fullyQualifiedName);
	}

	private String getFullyQualifiedName() {
		if (!containingPackage.equals("<default>"))
			return containingPackage + "." + containingClass;
		return containingClass;
	}

	public boolean verifyMethod(IMethod method) {
		// System.out.println(method.getElementName() + " - " + name);
		if (!verifyName(method)) {
			return false;
		}
		// System.out.println("NAME Passed!" + method.getElementName());
		// System.out.println(method.getParameterTypes().length + "-" +
		// this.parameterTypes.length);
		if (!verifyNoOfParams(method))
			return false;
		String[] types = method.getParameterTypes();
		// for (String type : types) {
		// System.out.print(type + " ");
		// }
		// System.out.println();
		return verifyParams(types);
	}

	private boolean verifyName(IMethod method) {
		return method.getElementName().equals(name);
	}

	private boolean verifyNoOfParams(IMethod method) {
		return method.getParameterTypes().length == parameterTypes.length;
	}

	private boolean verifyParams(String[] types) {
		for (int index = 0; index < parameterTypes.length; index++) {
			if (!verifyCase(types[index], parameterTypes[index]))
				return false;
		}
		return true;
	}

	private boolean verifyCase(String type, String paramType) {
		if (type.length() == 1)
			return verifyPrimitiveType(type, paramType);
		if (type.length() == 2 && type.indexOf('[') != -1)
			return verifyPrimitiveArray(type, paramType);
		return verifyType(type, paramType);
	}

	private boolean verifyPrimitiveType(String type, String paramType) {
		String temp = transformPrimitiveType(type);
		return paramType.equals(temp);
	}

	private boolean verifyPrimitiveArray(String type, String paramType) {
		String temp = transformArrayOfPrimitiveType(type);
		return paramType.equals(temp);
	}

	private boolean verifyType(String type, String paramType) {
		String temp = transform(type);
		return paramType.equals(temp);
	}

	public String transform(String s) {
		String temp = s.replace(';', ',');
		temp = removeTrailingComma(temp);
		temp = transformIntoArray(temp);
		temp = temp.replaceFirst("Q", "");
		temp = formatGenericType(temp);
		temp = removeTrailingComma(temp);
		return temp;
	}

	private String formatGenericType(String temp) {
		if (temp.contains("<")) {
			boolean closed = false;
			String s = closeGenericType(temp);
			if (!temp.equals(s)) {
				temp = s;
				closed = true;
			}
			String sub = temp.substring(temp.indexOf('<') + 1, temp.lastIndexOf('>'));
			String[] subs = sub.split(",");
			String aux = "";
			for (String i : subs) {
				aux = format(i, aux);
			}
			temp = concatTempParts(temp, aux, closed);
		}
		return temp;
	}

	private String removeTrailingComma(String temp) {
		if (temp.charAt(temp.length() - 1) == ',')
			temp = temp.substring(0, temp.lastIndexOf(','));
		return temp;
	}

	private String closeGenericType(String temp) {
		if (!temp.contains(">") && !temp.contains("[]")) {
			temp += ">";
		} else if (!temp.contains(">") && temp.contains("[]")) {
			temp = temp.substring(0, temp.indexOf("[]")) + ">" + temp.substring(temp.indexOf("[]"));
		}
		return temp;
	}

	private String concatTempParts(String temp, String aux, boolean closed) {
		String s = temp.substring(temp.lastIndexOf('>') + 1);
		if (!closed)
			temp = temp.substring(0, temp.indexOf('<') + 1) + aux + ">";
		else
			temp = temp.substring(0, temp.indexOf('<') + 1) + aux;
		temp += s;
		return temp;
	}

	private String format(String s, String aux) {
		String temp = s;
		String sub = "";
		if (temp.indexOf('*') == 0) {
			temp = "?" + temp.substring(temp.indexOf('*') + 1);
		} else if (temp.indexOf("-") == 0) {
			temp = "? super " + transform(temp.substring(temp.indexOf('-') + 1));
		} else if (temp.indexOf("+") == 0) {
			temp = "? extends " + transform(temp.substring(temp.indexOf('+') + 1));
		} else if (temp.contains("Q")) {
			while (temp.indexOf('Q') != 0 || temp.indexOf('?') != 0) {
				temp = transformIntoArray(temp);
				if (temp.indexOf('Q') != 0) {
					sub += transformPrimitiveType(temp.substring(0, 1)) + temp.substring(temp.indexOf("[]")) + ",";
					temp = temp.substring(1, temp.indexOf("[]"));
				} else
					break;
			}
			if (temp.indexOf('Q') == 0)
				temp = temp.replaceFirst("Q", "");
			if (temp.contains("<")) {
				temp = temp.substring(0, temp.indexOf("<") + 1) + format(temp.substring(temp.indexOf("<") + 1), "");
			}
		} else if (temp.length() >= 2) {
			while (!temp.equals("")) {
				temp = transformIntoArray(temp);
				sub += transformPrimitiveType(temp.substring(0, 1)) + temp.substring(temp.indexOf("[]")) + ",";
				temp = temp.substring(1, temp.indexOf("[]"));
				if (temp.equals(">")) {
					break;
				}
			}
			sub = removeTrailingComma(sub);
		}
		temp = sub + temp;
		if (temp.equals(">") || aux.equals(""))
			return String.join("", aux, temp);
		return String.join(",", aux, temp);
	}

	private String transformIntoArray(String temp) {
		while (temp.indexOf('[') == 0) {
			temp = temp.substring(1);
			temp += "[]";
		}
		return temp;
	}

	private String transformPrimitiveType(String s) {
		switch (s) {
		case "I":
			return "int";
		case "J":
			return "long";
		case "Z":
			return "boolean";
		case "B":
			return "byte";
		case "S":
			return "short";
		case "F":
			return "float";
		case "D":
			return "double";
		case "C":
			return "char";
		default:
			return s;
		}
	}

	private String transformArrayOfPrimitiveType(String s) {
		switch (s) {
		case "[I":
			return "int[]";
		case "[J":
			return "long[]";
		case "[Z":
			return "boolean[]";
		case "[B":
			return "byte[]";
		case "[S":
			return "short[]";
		case "[F":
			return "float[]";
		case "[D":
			return "double[]";
		case "[C":
			return "char[]";
		default:
			return s.charAt(1) + "[]";
		}
	}

}
