package utils;

import java.util.Arrays;

public class StringParser {

	public static PseudoMethod parse(String temp) {
		if (temp.contains("<lambda"))
			return parseLambdaString(temp);
		else if (temp.contains("{key"))
			return parseString(temp);
		return null;
	}

	private static PseudoMethod parseLambdaString(String temp) {
		temp = temp.substring(temp.indexOf("<lambda"));
		String args[] = getArgs(temp);
		String name = getContainingMethodName(temp);
		temp = temp.substring(temp.indexOf(name) + name.length() + 1);
		temp = temp.substring(temp.indexOf("[in"));
		String containingClass = getClassName(temp.split(" "));
		String containingPackage = getPackageName(temp);
		// System.out.println(name + ":" + containingClass + ":" + containingPackage);
		if (args == null || args[0].equals("")) {
			return new PseudoMethod(name, containingClass, containingPackage, null);
		}
		return new PseudoMethod(name, containingClass, containingPackage, args);
	}

	private static String[] getArgs(String temp) {
		int lpar = temp.indexOf('(');
		int rpar = temp.indexOf(')');
		if (lpar == -1 || rpar == -1)
			return null;
		String args[] = temp.substring(lpar + 1, rpar).split(" ");
		args = replaceComma(args);
		// for (String arg : args) {
		// System.out.println(arg);
		// }
		return args;
	}

	private static String getContainingMethodName(String temp) {
		int lpar = temp.indexOf('(');
		if (lpar == -1) {
			String[] parts = temp.split(" ");
			return parts[3];
		}
		char character = temp.charAt(lpar - 1);
		int space = lpar - 1;
		while (character != ' ') {
			space--;
			character = temp.charAt(space);
		}
		return temp.substring(space + 1, lpar);
	}

	private static PseudoMethod parseString(String info) {
		String name;
		String temp = info.substring(info.indexOf("{key"));
		name = getMethodName(temp);
		temp = info.substring(info.indexOf('(') + 1, info.indexOf(')'));
		String[] args = temp.split(" ");
		args = replaceComma(args);
		String containingClass = getClassName(info.substring(info.indexOf("[in")).split(" "));
		String containingPackage = getPackageName(info);
		if (args[0].equals("")) {
			return new PseudoMethod(name, containingClass, containingPackage, null);
		}
		return new PseudoMethod(name, containingClass, containingPackage, args);
	}

	private static String getMethodName(String temp) {
		int pos1 = temp.indexOf('<');
		int pos0 = temp.indexOf('.') + 1;
		int pos2 = temp.indexOf('(');
		if (pos1 != -1 && pos1 < pos2)
			return temp.substring(pos0, pos1);
		return temp.substring(pos0, pos2);
	}

	private static String[] replaceComma(String[] args) {
		String[] temp = new String[args.length];
		int nr = 0;
		for (int index = 0; index < args.length; index++) {
			if (args[index].length() > 1 && args[index].charAt(args[index].length() - 1) == ',')
				args[index] = args[index].substring(0, args[index].lastIndexOf(','));
			if (args[index].contains("<") && !args[index].contains(">")) {
				String aux = args[index];
				index++;
				while (!args[index].contains(">")) {
					aux += " " + args[index];
					index++;
				}
				if (args[index].indexOf(',') == args[index].length() - 1)
					args[index] = args[index].substring(0, args[index].length() - 1);
				args[index] = aux + " " + args[index];

			}
			if (args[index].equals("...")) {
				temp[nr - 1] += "[]";
				continue;
			}
			temp[nr++] = args[index];
		}
		return Arrays.copyOf(temp, nr);
	}

	private static String getClassName(String[] s) {
		int index = 1;
		String temp = "";
		while (!s[index].contains("[") && !s[index].contains(".java")) {
			temp = s[index] + "$" + temp;
			index = index + 2;
		}
		// if(temp.contains("<")) {
		// index = s.length - 1;
		// while(!s[index].contains(".java")) {
		// index--;
		// }
		// return s[index].substring(0, s[index].indexOf('.'));
		// }
		return temp.substring(0, temp.length() - 1);
	}

	private static String getPackageName(String temp) {
		String[] s = temp.substring(0, temp.lastIndexOf("]]")).split(" ");
		int index = s.length - 5;
		if (s[index].equals("[in"))
			index -= 1;
		return s[index];
	}

	public static String parseField(String field) {
		String temp = field;
		// System.out.println(field);
		if (field.contains("(not open)")) {
			// if (!field.contains("." + field.substring(0, field.indexOf(" ")))) {
			// return null;
			// }
			temp = field.replace("(not open) ", "");
			if (temp.contains("{key"))
				temp = replaceKey(temp);
			return temp;
		}
		if (field.contains("[Working copy]")) {
			temp = field.replace("[Working copy] ", "");
		}
		if (field.contains("{key"))
			temp = replaceKey(temp);
		temp = temp.substring(temp.indexOf(" ") + 1);
		return temp;
	}

	public static String replaceKey(String field) {
		int leftAccolade = field.indexOf("{");
		int rightAccolade = field.indexOf("}");
		return field.replace(field.substring(leftAccolade, rightAccolade + 2), "");
	}
}
