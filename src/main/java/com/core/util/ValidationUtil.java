package com.core.util;

import java.util.regex.Pattern;

import org.apache.commons.lang3.math.NumberUtils;

/**
 * 
 * @author Bonjour
 *
 */
public class ValidationUtil {
	
	private static final Pattern EMAIL_PATTERN = Pattern.compile("^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z]+$");
	private static final Pattern NDIGIT_PATTERN = Pattern.compile("^[0-9]*$");
	private static final Pattern MOBILE_PATTERN = Pattern.compile("^09[0-9]{8}$");

	/**
	 * 
	 * @param input : 須驗證資料是否為空值
	 * @return : boolean
	 */
	public static boolean notEmpty(String input) {
		return (input != null) && (!input.trim().equals(""));
	}
	
	/**
	 *
	 * @param input : 須驗證資料是否"不"為空值
	 * @return : boolean
	 *
	 */
	public static boolean isEmpty(String input) {
		return !notEmpty(input);
	}

	/**
	 * 
	 * @param email : 須驗證的電子信箱
	 * @return : boolean
	 */
	public static boolean email(String email) {
		boolean resulte = false;

		if (email != null) {
			resulte = EMAIL_PATTERN.matcher(email).matches();
		}

		return resulte;
	}

	/**
	 * 
	 * @param email : 電子信箱驗證是否為空值
	 * @return : boolean
	 */
	public static boolean emailNotEmpty(String email) {
		return notEmpty(email) && email(email);
	}

	/**
	 *
	 * @param input : 驗證參數是否為負數
	 * @return : boolean
	 */
	@SuppressWarnings("deprecation")
	public static boolean positive(String input) {
		if (isEmpty(input))
			return true;
		if (! NumberUtils.isNumber(input))
			return false;
		// 不包含負號則return true, 否則return false
		return (input.indexOf("-") == -1);
	}

	/**
	 *
	 * @param input : 驗證參數為正數 and 非空值
	 * @return : boolean
	 */
	public static boolean positiveNotEmpty(String input) {
		return notEmpty(input) && positive(input);
	}

	/**
	 * 
	 * @param input : 驗證參數是否可表示為數字, 包含負數. 小數
	 * @return : boolean
	 */
	@SuppressWarnings("deprecation")
	public static boolean number(String input) {
		if (isEmpty(input))
			return true;
		return NumberUtils.isNumber(input);
	}

	/**
	 * 
	 * @param input : 參數為數值且不為空值
	 * @return : boolean
	 */
	public static boolean numberNotEmpty(String input) {
		return notEmpty(input) && number(input);
	}

	/**
	 * 
	 * @param input : 驗證參數是否為數字
	 * @return : boolean
	 */
	public static boolean digit(String input) {
		if (isEmpty(input))
			return true;
		boolean result = NDIGIT_PATTERN.matcher(input).matches();
		return result;
	}

	/**
	 * 
	 * @param input : 驗證參數為數字且不為空值
	 * @return : boolean
	 */
	public static boolean digitNotEmpty(String input) {
		return notEmpty(input) && digit(input);
	}

	/**
	 *  字串是否為全為數字, 且長度為參數num值
	 * 
	 * @param input : input數字
	 * @param num : 驗證input的長度
	 * @return : boolean
	 */
	public static boolean nDigit(String input, int num) {
		if (isEmpty(input))
			return true;
		if (input.length() != num)
			return false;
		boolean result = NDIGIT_PATTERN.matcher(input).matches();
		return result;
	}

	/**
	 * 
	 *  字串是否為全為數字, 且長度"不"為參數num值
	 * 
	 * @param input : input數字
	 * @param num : 驗證input的長度
	 * @return : boolean
	 */
	public static boolean nDigitNotEmpty(String input, int num) {
		return notEmpty(input) && nDigit(input, num);
	}

	/**
	 * 
	 * @param input : 驗證參數為電話格式且不為空值
	 * @return : boolean
	 */
	public static boolean phoneNotEmpty(String input) {
		return notEmpty(input) && phone(input);
	}

	/**
	 * 驗證到input 格式不為空值且符合手機格式
	 * 
	 * @param input 
	 * @return boole
	 */
	public static boolean mobile(String input) {
		if (isEmpty(input)){
			return true;
		}
		boolean result = MOBILE_PATTERN.matcher(input).matches();
		return result;
	}

	/**
	 * 驗證input手機格式且不為空值
	 *
	 * @param input
	 * @return
	 */
	public static boolean mobileNotEmpty(String input) {
		return notEmpty(input) && mobile(input);
	}

	/**
	 * 
	 * 驗證年月是否正確
	 * 
	 * @param year 	四碼年分
	 * @param month 月份 
	 * @return boolean
	 */
	public static boolean validDate(String year, String month) {
		if (isEmpty(year) && isEmpty(month))
			return true;
		else if (isEmpty(year) || isEmpty(month))
			return false;
		if (nDigit(year, 4) && (nDigit(month, 1) || nDigit(month, 2))) {
			return (Integer.parseInt(year) > 1900) && (Integer.parseInt(year) < 2100) && (Integer.parseInt(month) > 0) && (Integer.parseInt(month) < 13);
		}
		return false;
	}

	/**
	 * 驗證年月是否正確
	 * 
	 * @param year 	四碼年分
	 * @param month 月份 
	 * @return boolean
	 */
	public static boolean validDate(int year, int month) {
		return validDate(String.valueOf(year), String.valueOf(month));
	}

	/**
	 * 
	 * 驗證年月是否正確
	 * 
	 * @param year 	四碼年分
	 * @param month 月份 
	 * @return boolean
	 */
	public static boolean validDate(String year, int month) {
		return validDate(year, String.valueOf(month));
	}

	/**
	 * 驗證年月是否正確
	 * 
	 * @param year 	四碼年分
	 * @param month 月份 
	 * @return boolean
	 */
	public static boolean validDate(int year, String month) {
		return validDate(String.valueOf(year), month);
	}

	/**
	 * 驗證年月是否正確
	 * 
	 * @param year 	四碼年分
	 * @param month 月份 
	 * @return boolean
	 */
	public static boolean validDateNotEmpty(String year, String month) {
		return notEmpty(year) && notEmpty(month) && validDate(year, month);
	}

	/**
	 * 驗證年月是否正確
	 * 
	 * @param year 	四碼年分
	 * @param month 月份 
	 * @return boolean
	 */
	public static boolean validDateNotEmpty(int year, int month) {
		return validDate(year, month);
	}

	/**
	 * 驗證年月是否正確
	 * 
	 * @param year 	四碼年分
	 * @param month 月份 
	 * @return boolean
	 */
	public static boolean validDateNotEmpty(String year, int month) {
		return notEmpty(year) && validDate(year, month);
	}

	/**
	 * 驗證年月是否正確
	 * 
	 * @param year 	四碼年分
	 * @param month 月份 
	 * @return boolean
	 */
	public static boolean validDateNotEmpty(int year, String month) {
		return notEmpty(month) && validDate(year, month);
	}

	/**
	 * 驗證 input 是否為合法身分正格式
	 * 
	 * @param input 身分證字號
	 * @return boolean
	 */
	public static boolean twId(String input) {
		int sum = 0;
		String head = "ABCDEFGHJKLMNPQRSTUVXYWZIO";
		if (isEmpty(input)){
			return true;
		}
		input = input.toUpperCase();
		if (input.length() != 10){
			return false;
		}
		if (input.charAt(1) != '1' && input.charAt(1) != '2') {
			return false;
		}
		if (input.charAt(0) < 'A' || input.charAt(0) > 'Z') {
			return false;
		}
		if (!nDigit(input.substring(1), 9)) {
			return false;
		}
		int regionNum = head.indexOf(input.charAt(0)) + 10;
		regionNum = (regionNum / 10) + (regionNum % 10) * 9;
		sum = (input.charAt(1) - '0') * 8 + (input.charAt(2) - '0') * 7 + (input.charAt(3) - '0') * 6 + (input.charAt(4) - '0') * 5 + (input.charAt(5) - '0') * 4 + (input.charAt(6) - '0') * 3 + (input.charAt(7) - '0') * 2 + (input.charAt(8) - '0') * 1 + (input.charAt(9) - '0') * 1 + regionNum;

		boolean result = (sum % 10 == 0) ? true : false;
		return result;
	}

	/**
	 * 驗證身分證格式
	 * 
	 * @param input 身分證字號
	 * @return boolean
	 */
	public static boolean twIdNotEmpty(String input) {
		return notEmpty(input) && twId(input);
	}

	/**
	 * 驗證統一編號格式
	 *
	 * @param input 統一編號
	 * @return boolean
	 */
	public static boolean comId(String input) {
		if (isEmpty(input))
			return true;
		input = input.toUpperCase();
		int a1, a2, a3, a4, a5, b1, b2, b3, b4;
		int c1, c2, c3, c4, d1, d2, d3, d4, d5, d6, d7, cd8;
		if (!nDigit(input, 8)) {
			return false;
		}
		d1 = input.charAt(0) - '0';
		d2 = input.charAt(1) - '0';
		d3 = input.charAt(2) - '0';
		d4 = input.charAt(3) - '0';
		d5 = input.charAt(4) - '0';
		d6 = input.charAt(5) - '0';
		d7 = input.charAt(6) - '0';
		cd8 = input.charAt(7) - '0';

		c1 = d1;
		c2 = d3;
		c3 = d5;
		c4 = cd8;
		a1 = (d2 * 2) / 10;
		b1 = (d2 * 2) % 10;
		a2 = (d4 * 2) / 10;
		b2 = (d4 * 2) % 10;
		a3 = (d6 * 2) / 10;
		b3 = (d6 * 2) % 10;
		a4 = (d7 * 4) / 10;
		b4 = (d7 * 4) % 10;
		a5 = (a4 + b4) / 10;

		if ((a1 + b1 + c1 + a2 + b2 + c2 + a3 + b3 + c3 + a4 + b4 + c4) % 10 == 0)
			return true;

		if (d7 == 7) {
			if ((a1 + b1 + c1 + a2 + b2 + c2 + a3 + b3 + c3 + a5 + c4) % 10 == 0)
				return true;
		}
		return false;
	}

	/**
	 * 驗證統一編號格式且不為空值
	 *
	 * @param input 統一編號
	 * @return boolean
	 */
	public static boolean comIdNotEmpty(String input) {
		return notEmpty(input) && comId(input);
	}

	/**
	 * 
	 * 驗證護照號碼格式
	 * 
	 * @param input 護照號碼
	 * @return boolean
	 */
	public static boolean foreignId(String input) {
		String head = "ABCDEFGHJKLMNPQRSTUVXYWZIO";
		if (isEmpty(input))
			return true;
		input = input.toUpperCase();
		if (input.length() != 10)
			return false;
		if (input.charAt(0) < 'A' || input.charAt(0) > 'Z' || input.charAt(1) < 'A' || input.charAt(1) > 'Z') {
			return false;
		}
		if (!nDigit(input.substring(2), 8)) {
			return false;
		}
		String id = (head.indexOf(input.charAt(0)) + 10) + "" + ((head.indexOf(input.charAt(1)) + 10) % 10) + "" + input.substring(2);

		int s = (id.charAt(0) - '0') + (id.charAt(1) - '0') * 9 + (id.charAt(2) - '0') * 8 + (id.charAt(3) - '0') * 7 + (id.charAt(4) - '0') * 6 + (id.charAt(5) - '0') * 5 + (id.charAt(6) - '0') * 4 + (id.charAt(7) - '0') * 3 + (id.charAt(8) - '0') * 2 + (id.charAt(9) - '0') + (id.charAt(10) - '0');

		if ((s % 10) != 0)
			return false;

		return true;
	}

	/**
	 * 
	 * 驗證護照號碼格式且不為空值
	 * 
	 * @param input 護照號碼
	 * @return boolean
	 */
	public static boolean foreignIdNotEmpty(String input) {
		return notEmpty(input) && foreignId(input);
	}

	/**
	 * 
	 * 有三種證照格式  只要符合其中一種格式就回傳 true
	 *
	 * @param input 證照號碼
	 * @return boolean
	 */
	public static boolean validId(String input) {
		return twId(input) || foreignId(input) || comId(input);
	}

	/**
	 * 
	 * 驗証證號不為空值
	 * 
	 * @param input 證照號碼
	 * @return boolean
	 */
	public static boolean validIdNotEmpty(String input) {
		return notEmpty(input) && validId(input);
	}

	/**
	 * Validate String
	 * 
	 * @param obj
	 *            : 欲驗證的物件(string)
	 * @param regex
	 *            : 是否有格式(null即不看格式)
	 * @param limitLength
	 *            : 長度限制(0為不限)
	 * @param nullAble
	 *            : 此input可否為null
	 * @return boolean
	 **/
	public static boolean validateString(Object obj, String regex, int limitLength, boolean nullAble, boolean canBeEmpty) {
		boolean result = true;
		if (obj == null) {
			// 可否接受null
			if (!nullAble) {
				result = false;
			}
		} else {
			String inputString = obj.toString();
			// 可否接受空字串
			if (inputString.isEmpty()) {
				if (!canBeEmpty) {
					result = false;
				}
			} else {
				// 是否看長度限制，limitLength<=0時不限
				if (limitLength <= 0) {
					// 是否有字串格式，且符合字串格式
					if (regex != null && !inputString.matches(regex)) {
						result = false;
					}
				} else {
					if (inputString.length() > limitLength) {
						result = false;
					} else {
						// 是否有字串格式，且符合字串格式
						if (regex != null && !inputString.matches(regex)) {
							result = false;
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * Validate String
	 * 
	 * @param obj
	 *            : 欲驗證的物件(string)，預設不帶格式，不看長度，不為null，不為空。
	 * @return boolean
	 **/
	public static boolean validateString(Object obj) {
		boolean result = true;
		result = validateString(obj, null, 0, false, false);
		return result;
	}

	/**
	 * Validate String
	 * 
	 * @param obj
	 *            : 欲驗證的物件(string)，有insert的長度限制，但可塞null。
	 * @return boolean
	 **/
	public static boolean validateString(Object obj, int limitLength) {
		boolean result = true;
		result = validateString(obj, null, limitLength, true, true);
		return result;
	}

	/**
	 * Validate String
	 * 
	 * @param obj
	 *            : 欲驗證的物件(string)，有insert的長度限制，且有格式限制，但可塞null。
	 * @return boolean
	 **/
	public static boolean validateString(Object obj, String regex,
			int limitLength) {
		boolean result = true;
		result = validateString(obj, regex, limitLength, true, true);
		return result;
	}

	/**
	 * 無視括號與減號，為9-11碼數字
	 * 
	 * @param input
	 *            : 字串 (xx)xxxxxxxx
	 * @return 驗證結果
	 */
	public static boolean phone(String input) {
		if (isEmpty(input))
			return true;
		// 括號跳脫
		input = input.replaceAll("\\(", "").replaceAll("\\)", "").replaceAll("-", "");

		return nDigit(input, 9) || nDigit(input, 10) || nDigit(input, 11);
	}

	/**
	 * 
	 * 把市話區碼和號碼接起來		reigonNum+phoneNum
	 * 
	 * @param reigonNum 市話區碼
	 * @param phoneNum 電話號碼
	 * @return boolean
	 */
	public static boolean phoneNotEmpty(String reigonNum, String phoneNum) {
		return phoneNotEmpty(reigonNum + phoneNum);
	}

	/**
	 * 
	 * 把市話區碼和號碼接起來		reigonNum+phoneNumPart1+phoneNumPart2
	 * 
	 * @param reigonNum 市話區碼
	 * @param phoneNumPart1 電話號碼(前四碼)
	 * @param phoneNumPart2 電話號碼(後四碼)
	 * @return
	 */
	public static boolean phoneNotEmpty(String reigonNum, String phoneNumPart1,
			String phoneNumPart2) {
		return phoneNotEmpty(reigonNum + phoneNumPart1 + phoneNumPart2);
	}
	
	/**
     * 驗証統一編號   
     */
    public static boolean checkTaxId(String taxId) {
        String taxIdReg = "^[0-9]{8}$";
        if(taxId!=null) {
            if(!taxId.matches(taxIdReg)) return false;
            int sum = 0;
            int[] chkArr = new int[]{1,2,1,2,1,2,4,1};
            
            for(int i = 0 ;i<taxId.length();i++) {
                sum += splitSum(Character.digit(taxId.charAt(i),10) * chkArr[i]);
            }
            //被 10 整除就為正確
            if(sum % 10 == 0) return true;
            //且 G 為 7 得話, 再加上 1 被 10 整除也為正確
            else if (Character.digit(taxId.charAt(6),10)==7 &&  (sum+1) % 10==0) return true;
        }
        return false;
    }
    
    /**
     * 如果和的值為二位數則將十位數和個位數相加
     */
    private static int splitSum(int sum) {
        return sum/10 + sum%10;
    }
}
