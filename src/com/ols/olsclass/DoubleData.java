package com.ols.olsclass;

public class DoubleData {
	private String dataStr;
	private int length;
	/**
	 * DATA_STATE 1: 非数字 2：正整数 3：负整数 4：正实数 5：负实数
	 */
	private int DATA_STATE = 0;
	/**
	 * numP 小数点的个数
	 */
	private int numP = 0;
	/**
	 * numM 负号的个数
	 */
	private int numM = 0;
	/**
	 * numZ 0的个数
	 */
	private int numZ = 0;
	/**
	 * 从第一个非零数开始数字的长度
	 */
	private int num_nonO = 0;

	public void setDataStr(String dataStr) {
		this.dataStr = dataStr;
	}

	public DoubleData() {
		super();
	}

	public DoubleData(Double dataStr) {
		super();
		this.dataStr = dataStr + "";
	}

	public DoubleData(String dataStr) {
		super();
		this.dataStr = dataStr;
	}

	public boolean Check() {
//		System.out.println("******dataStr=" + dataStr + "*****");
		length = dataStr.length();
		char[] ch = dataStr.toCharArray();
		if (length < 1)
			return false;
		for (int i = 0; i < length; i++) {
			if ((int) ch[i] > 44 && (int) ch[i] < 58 && (int) ch[i] != 47) {
				if (ch[i] == '-')
					numM++;
				else if (ch[i] == '.')
					numP++;
				else if (ch[i] == '0')
					numZ++;
			}
		}
		for (int i = 0; i < length; i++) {
			if ((int) ch[i] > 44 && (int) ch[i] < 58 && (int) ch[i] != 47)
				continue;
			else {
				DATA_STATE = 1;
				return false;
			}
		}
		return true;
	}

	public int Identify() {
		if (Check()) {
			if (numM == 0 && numP == 0) {
				DATA_STATE = 2;
				return DATA_STATE;
			} else if (numM == 1 && numP == 0 && dataStr.indexOf("-") == 0) {
				DATA_STATE = 3;
				return DATA_STATE;
			} else if (numM == 0 && numP == 1 && dataStr.indexOf(".") > 0
					&& dataStr.indexOf(".") < length) {
				// System.out.println("******Identify:"+4+"*****");
				DATA_STATE = 4;
				while (dataStr.endsWith("0") || dataStr.endsWith(".")) {
					// System.out.println("******"+dataStr+"*****");
					dataStr = dataStr.substring(0, dataStr.length() - 1);
					// System.out.println("******"+dataStr+"*****");
				}
				if (dataStr.length() < 1) {
					dataStr = "0";
					DATA_STATE = 2;
				}
				return DATA_STATE;
			} else if (numM == 1 && numP == 1 && (int) dataStr.charAt(0) == 45
					&& dataStr.indexOf(".") > 0
					&& dataStr.indexOf(".") < length) {
				DATA_STATE = 5;
				while (dataStr.endsWith("0") || dataStr.endsWith(".")
						|| dataStr.endsWith("-")) {
					dataStr = dataStr.substring(0, dataStr.length() - 1);
				}
				if (dataStr.length() < 1) {
					dataStr = "0";
					DATA_STATE = 3;
				}
				return DATA_STATE;
			} else {
				DATA_STATE = 1;
				return DATA_STATE;
			}
		} else
			return 1;
	}

	public String DataFormat() {
		Identify();
//		System.out.println("******DATA_STATE=" + DATA_STATE + "*****");
		switch (DATA_STATE) {
		case 1:
			int Eposition = dataStr.indexOf("E");
			if (Eposition > 0 && Eposition < length) {
				if (Eposition > 5) {
//					System.out.println("*****flag=1:*****");
					return dataStr.substring(0, 5)
							+ dataStr.substring(Eposition, length);
				} else {
//					System.out.println("*****flag=2:*****");
					return dataStr;
				}
			}
			break;
		case 2:
			if (length > 6) {
//				System.out.println("*****flag=3:*****");
				return dataStr.charAt(0) + "." + dataStr.substring(1, 4) + "E"
						+ (length - 1);
			} else
				return dataStr;
		case 3:
			if (length > 7) {
//				System.out.println("*****flag=4:*****");
				return dataStr.substring(0, 2) + "." + dataStr.substring(2, 5)
						+ "E" + (length - 2);
			} else
				return dataStr;
		case 4:
			double a = Double.parseDouble(dataStr);
			dataStr = String.valueOf(a);
			length = dataStr.length();
			if (dataStr.contains("E")) {
				if (length > 7) {
//					System.out.println("*****flag=5:*****");
					return (dataStr.substring(0, 5) + dataStr.substring(dataStr
							.lastIndexOf("E")));
				} else
					return dataStr;
			} else if (a > 1) {
				if (length > 6) {
					int temp = 0;
					while (a > 10) {
						temp++;
						a /= 10;
					}
//					System.out.println("*****flag=6:*****");
					return (a + "").substring(0, 5)
							+ (temp > 0 ? ("E" + temp) : "");
				} else
					return a + "";
			} else if(a==1)
			{
				return "1.0";
			}
			else {
				boolean isZero = true;
				int i = 0;
				for (; i < length && isZero; i++) {
					if (dataStr.charAt(i) != '0' && dataStr.charAt(i) != '.') {
						isZero = false;
					}
				}
				num_nonO = length - i + 1;
				if (num_nonO > 5) {
					int temp = 0;
					while (a < 1) {
						temp++;
						a *=10;
					}
//					System.out.println("*****flag=7:*****");
					return (a + "").substring(0, 5) + "E-" + temp;
				} else if (num_nonO == 1) {
					if (dataStr.length() < 6) {
//						System.out.println("*****flag=8:*****");
						return dataStr;
					} else {
//						System.out.println("*****flag=9:*****");
						return (a + "").replace("0", "").replace(".", "")
								+ ".0E-" + (length - 2);
					}
				} else if (i > 3) {
//					System.out.println("*****flag=10:*****");
					int temp = 0;
					while (a < 1) {
						temp++;
						a *=10;
					}
					String str = a + "";
					int length = str.length();
					return str.substring(0, length > 4 ? 5 : length) + "E-"
							+ temp;
				} else {
//					System.out.println("*****flag=11:*****");
				}
			}
			break;
		case 5:
			double b = Double.parseDouble(dataStr.replace("-", ""));
			dataStr = String.valueOf(b);
			length = dataStr.length();
			if (dataStr.contains("E")) {
				if (length > 7) {
//					System.out.println("*****flag=5:*****");
					return "-"+(dataStr.substring(0, 5) + dataStr.substring(dataStr
							.lastIndexOf("E")));
				} else
					return "-"+dataStr;
			} else if (b > 1) {
				if (length > 6) {
					int temp = 0;
					while (b > 10) {
						temp++;
						b /= 10;
					}
//					System.out.println("*****flag=6:*****");
					return "-"+(b + "").substring(0, 5)
							+ (temp > 0 ? ("E" + temp) : "");
				} else
					return "-"+b;
			}else if(b==1)
			{
				return "-1.0";
			}
			else {
				boolean isZero = true;
				int i = 0;
				for (; i < length && isZero; i++) {
					if (dataStr.charAt(i) != '0' && dataStr.charAt(i) != '.') {
						isZero = false;
					}
				}
				num_nonO = length - i + 1;
				if (num_nonO > 5) {
					int temp = 0;
					while (b < 1) {
						temp++;
						b *=10;
					}
//					System.out.println("*****flag=7:*****");
					return "-"+(b + "").substring(0, 5) + "E-" + temp;
				} else if (num_nonO == 1) {
					if (dataStr.length() < 6) {
//						System.out.println("*****flag=8:*****");
						return "-"+dataStr;
					} else {
//						System.out.println("*****flag=9:*****");
						return "-"+(b + "").replace("0", "").replace(".", "")
								+ ".0E-" + (length - 2);
					}
				} else if (i > 3) {
//					System.out.println("*****flag=10:*****");
					int temp = 0;
					while (b < 1) {
						temp++;
						b *=10;
					}
					String str = b + "";
					int length = str.length();
					return "-"+str.substring(0, length > 4 ? 5 : length) + "E-"
							+ temp;
				} else {
//					System.out.println("*****flag=11:*****");
				}
			}
			break;
		default:
			break;
		}
		return "";
	}
}
