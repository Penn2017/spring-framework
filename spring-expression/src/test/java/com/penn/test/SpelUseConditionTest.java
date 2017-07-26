package com.penn.test;

import org.junit.Test;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.expression.spel.testresources.Inventor;
import org.springframework.expression.spel.testresources.PlaceOfBirth;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @Description : spel的使用情况的测试用例
 * @Project : spring-framework
 * @Program Name  : com.penn.test.SpelUseConditionTest
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
public class SpelUseConditionTest {
	ExpressionParser parser = new SpelExpressionParser();

	/**
	 * 测试文字性的用例
	 */
	@Test
	public void testLiteral() {


		// evals to "Hello World"
		String helloWorld = (String) parser.parseExpression("'Hello World'").getValue();
		System.out.println("helloWorld:" + helloWorld);

		double avogadrosNumber = (Double) parser.parseExpression("6.0221415E+23").getValue();
		System.out.println("avogadrosNumber:" + avogadrosNumber);
		// evals to 2147483647
		int maxValue = (Integer) parser.parseExpression("0x7FFFFFFF").getValue();
		System.out.println("maxValue:" + maxValue);

		boolean trueValue = (Boolean) parser.parseExpression("true").getValue();
		System.out.println("trueValue:" + trueValue);

		Object nullValue = parser.parseExpression("null").getValue();

		System.out.println("trueValue:" + nullValue);
	}

	/*************************************properties，array，list，map，index等等**************************************************/

	/**
	 *测试properties
	 */
	@Test
	public void testPropertiesArraysListMapsIndexers() {
		Inventor inventor = new Inventor("penn", new Date(), "CN");

		PlaceOfBirth placeOfBirth = new PlaceOfBirth("JX");
		inventor.setPlaceOfBirth(placeOfBirth);

		EvaluationContext context = new StandardEvaluationContext(inventor);
		// evals to 1856
		int year = (Integer) parser.parseExpression("Birthdate.year + 1900").getValue(context);
		System.out.println("year:" + year);

		String city = (String) parser.parseExpression("placeOfBirth.City").getValue(context);
		System.out.println("city:" + city);

	}

	/**
	 * test inline lists
	 */
	@Test
	public void testInlinelists() {
		// evaluates to a Java list containing the four numbers
		List numbers = (List) parser.parseExpression("{1,2,3,4}").getValue();
		System.out.println("numbers:" + numbers);

		List listOfLists = (List) parser.parseExpression("{{'a','b'},{'x','y'}}").getValue();
		System.out.println("listOfLists:" + listOfLists);
	}

	/**
	 *  map
	 */
	@Test
	public void testInlineMaps() {
		// evaluates to a Java map containing the two entries
		Map inventorInfo = (Map) parser.parseExpression("{name:'Nikola',dob:'10-July-1856'}").getValue();
		System.out.println("inventorInfo:" + inventorInfo);

		Map mapOfMaps = (Map) parser.parseExpression("{name:{first:'Nikola',last:'Tesla'},dob:{day:10,month:'July',year:1856}}").getValue();
		System.out.println("mapOfMaps: " + mapOfMaps);
	}


	/**
	 * array
	 */
	@Test
	public void arrayConstruction() {
		int[] numbers1 = (int[]) parser.parseExpression("new int[4]").getValue();
		System.out.println("numbers1:" + numbers1);

		// Array with initializer
		int[] numbers2 = (int[]) parser.parseExpression("new int[]{1,2,3}").getValue();
		System.out.println("numbers2:" + numbers1);

		// Multi dimensional array
		int[][] numbers3 = (int[][]) parser.parseExpression("new int[4][5]").getValue();
		System.out.println("numbers3:" + numbers1);

	}

	/*************************************method**************************************************/

	/**
	 * test method of spel
	 */
	@Test
	public void testMethod() {
		// string literal, evaluates to "bc"
		String c = parser.parseExpression("'abc'.substring(2, 3)").getValue(String.class);
		System.out.println("c:" + c);
	}

	/**
	 * test method of spel
	 */
	@Test
	public void testMethod2() {

		StandardEvaluationContext context = new StandardEvaluationContext(new Foo());
		String acceptConstant = parser.parseExpression("acceptConstant()").getValue(context,String.class);
		System.out.println("acceptConstant:" + acceptConstant);
	}



	/**
	 * test spel parse the functions
	 */
	@Test
	public void testFunctions() throws NoSuchMethodException {
		ExpressionParser parser = new SpelExpressionParser();
		StandardEvaluationContext context = new StandardEvaluationContext();


		context.registerFunction("capitalize",
				StringUtils.class.getDeclaredMethod("capitalize", new Class[] { String.class }));

		String capitalize = parser.parseExpression(
				"#capitalize('hello')").getValue(context, String.class);
		System.out.println(capitalize);


	}


	/*****************************************Operators*********************************************/

	/**
	 * oreators -- testRelationalOperators
	 */
	@Test
	public void testRelationalOperators() {
		// evaluates to true
		boolean trueValue = parser.parseExpression("2 == 2").getValue(Boolean.class);
		System.out.println("trueValue:" + trueValue);

		// evaluates to false
		boolean falseValue = parser.parseExpression("2 < -5.0").getValue(Boolean.class);
		System.out.println("falseValue:" + falseValue);

		//evaluates to true
		boolean trueValue2 = parser.parseExpression("'black' < 'block'").getValue(Boolean.class);
		System.out.println("trueValue2:" + trueValue2);

	}

	/**
	 *  test  instanceof and maches
	 */
	/**
	 * Be careful with primitive(原始的) types as they are immediately boxed up to the wrapper type,
	 * so 1 instanceof T(int) evaluates to false while 1 instanceof T(Integer) evaluates to true, as expected.
	 */
//	@Test
//	public void testInstanceofAndMatches(){
//
//		// evaluates to false
//		boolean falseValue = parser.parseExpression(
//				"'xyz' instanceof T(Integer)").getValue(Boolean.class);
//
//       // evaluates to true
//		boolean trueValue = parser.parseExpression(
//				"'5.00' matches '\^-?\\d+(\\.\\d{2})?$'").getValue(Boolean.class);
//
//       //evaluates to false
//		boolean falseValue1= parser.parseExpression(
//				"'5.0067' matches '\^-?\\d+(\\.\\d{2})?$'").getValue(Boolean.class);
//
//	}


	/**
	 * Each symbolic(象征的)
	 * operator can also be specified as a purely alphabetic(字母的) equivalent.
	 * This avoids problems where the symbols used have special meaning for the document type in which the expression is embedded
	 * (eg. an XML document). The textual(本文的) equivalents are shown here:
	 * lt (<), gt (>), le (⇐), ge (>=), eq (==), ne (!=), div (/), mod (%), not (!). These are case insensitive(感觉迟钝的).
	 */
	@Test
	public void testOperator2() {
		// -- AND --

		// evaluates to false
		boolean falseValue = parser.parseExpression("true and false").getValue(Boolean.class);
		System.out.println("falseValue:" + falseValue);

//		// evaluates to true
//		String expression = "isMember('Nikola Tesla') and isMember('Mihajlo Pupin')";
//		boolean trueValue = parser.parseExpression(expression).getValue( Boolean.class);
//		System.out.println("trueValue:"+trueValue);

		// -- OR --

		// evaluates to true
		boolean trueValue1 = parser.parseExpression("true or false").getValue(Boolean.class);
		System.out.println("trueValue1:" + trueValue1);

		// evaluates to true
//		String expression2 = "isMember('Nikola Tesla') or isMember('Albert Einstein')";
//		boolean trueValue3 = parser.parseExpression(expression).getValue( Boolean.class);
//		System.out.println("trueValue1:"+trueValue1);

		// -- NOT --

		// evaluates to false
		boolean falseValue4 = parser.parseExpression("!true").getValue(Boolean.class);

//		// -- AND and NOT --
//		String expression5 = "isMember('Nikola Tesla') and !isMember('Mihajlo Pupin')";
//		boolean falseValue6 = parser.parseExpression(expression).getValue( Boolean.class);

	}


	/***
	 * test assignment value to object.
	 */
	public void testAssignment() {
		Inventor inventor = new Inventor();
		StandardEvaluationContext inventorContext = new StandardEvaluationContext(inventor);

		parser.parseExpression("Name").setValue(inventorContext, "Alexander Seovic2");

		// alternatively

		String aleks = parser.parseExpression(
				"Name = 'Alexandar Seovic'").getValue(inventorContext, String.class);
	}


	/**
	 * test parser parse the types
	 */
	@Test
	public void testTypes() {
		Class dateClass = parser.parseExpression("T(java.util.Date)").getValue(Class.class);

		Class stringClass = parser.parseExpression("T(String)").getValue(Class.class);

		boolean trueValue = parser.parseExpression(
				"T(java.math.RoundingMode).CEILING < T(java.math.RoundingMode).FLOOR")
				.getValue(Boolean.class);
	}

	/**
	 * test parser constructors
	 */
	@Test
	public void testConstructors() {
		Inventor einstein = parser.parseExpression(
				"new org.spring.samples.spel.inventor.Inventor('Albert Einstein', 'German')")
				.getValue(Inventor.class);

		//create new inventor instance within add method of List
		parser.parseExpression("Members.add(new org.spring.samples.spel.inventor.Inventor('Albert Einstein', 'German'))").getValue();
	}

	/**
	 * test parser parse the variables
	 */
	@Test
	public void testVariables() {
		Inventor tesla = new Inventor("Nikola Tesla", "Serbian");
		StandardEvaluationContext context = new StandardEvaluationContext(tesla);
		context.setVariable("newName", "Mike Tesla");

		parser.parseExpression("Name = #newName").getValue(context);

		System.out.println(tesla.getName()); // "Mike Tesla"
	}

	/**
	 * test #this and #root variables
	 */
	@Test
	public void testThisAndRootVariables() {
		// create an array of integers
		List<Integer> primes = new ArrayList<Integer>();
		primes.addAll(Arrays.asList(2, 3, 5, 7, 11, 13, 17));

		// create parser and set variable 'primes' as the array of integers
		ExpressionParser parser = new SpelExpressionParser();
		StandardEvaluationContext context = new StandardEvaluationContext();
		context.setVariable("primes", primes);

		// all prime numbers > 10 from the list (using selection ?{...})
		// evaluates to [11, 13, 17]
		List<Integer> primesGreaterThanTen = (List<Integer>) parser.parseExpression("#primes.?[#this>10]").getValue(context);
		System.out.println("primesGreaterThanTen:"+primesGreaterThanTen);
	}




	/**
	 * test the bean Refance.
	 */
	@Test
	public void testBeanRefance(){
//		ExpressionParser parser = new SpelExpressionParser();
//		StandardEvaluationContext context = new StandardEvaluationContext();
//		context.setBeanResolver(new MyBeanResolver());
//
//        // This will end up calling resolve(context,"foo") on MyBeanResolver during evaluation
//		Object bean = parser.parseExpression("@foo").getValue(context);
	}


	@Test
	public void testTernaryOperator(){
//		parser.parseExpression("Name").setValue(societyContext, "IEEE");
//		societyContext.setVariable("queryName", "Nikola Tesla");
//
//		 String expression = "isMember(#queryName)? #queryName + ' is a member of the ' " +
//				"+ Name + ' Society' : #queryName + ' is not a member of the ' + Name + ' Society'";
//
//		String queryResultString = parser.parseExpression(expression)
//				.getValue( String.class);
//        // queryResultString = "Nikola Tesla is a member of the IEEE Society"
	}

	/**
	 * the elvisOperator is the shortening of temary operator
	 * Name?:'Elvis Presley'
	 */
	public void testElvisOperator(){
//		ExpressionParser parser = new SpelExpressionParser();
//
//		Inventor tesla = new Inventor("Nikola Tesla", "Serbian");
//		StandardEvaluationContext context = new StandardEvaluationContext(tesla);
//
//		String name = parser.parseExpression("Name?:'Elvis Presley'").getValue(context, String.class);
//
//		System.out.println(name); // Nikola Tesla
//
//		tesla.setName(null);
//
//		name = parser.parseExpression("Name?:'Elvis Presley'").getValue(context, String.class);
//
//		System.out.println(name); // Elvis Presley
	}

	/**
	 * test safe Navigation Operator
	 * PlaceOfBirth?.City
	 */
	@Test
	public void testSafeNavigationOperator(){
		ExpressionParser parser = new SpelExpressionParser();

		Inventor tesla = new Inventor("Nikola Tesla", "Serbian");
		tesla.setPlaceOfBirth(new PlaceOfBirth("Smiljan"));

		StandardEvaluationContext context = new StandardEvaluationContext(tesla);

		String city = parser.parseExpression("PlaceOfBirth?.City").getValue(context, String.class);
		System.out.println(city); // Smiljan

		tesla.setPlaceOfBirth(null);

		city = parser.parseExpression("PlaceOfBirth?.City").getValue(context, String.class);

		System.out.println(city); // null - does not throw NullPointerException!!!
	}

	/**
	 * test collectionSelection
	 *
	 * .?[selectionExpression]
	 */
	@Test
	public void testCollectionSelection(){
		List<Inventor> list = (List<Inventor>) parser.parseExpression(
				"Members.?[Nationality == 'Serbian']").getValue();
	}

	/**
	 * test collection projection
	 */
	@Test
	public void testCollectionProjection(){
		// returns ['Smiljan', 'Idvor' ]
		List placesOfBirth = (List)parser.parseExpression("Members.![placeOfBirth.city]");
	}


	/**
	 * test expression templating
	 *
	 * mixing of literal(文字的) text with one or more evaluation blocks.
	 */
	@Test
	public void testExpressionTemplation(){
		String randomPhrase = parser.parseExpression(
				"random number is #{T(java.lang.Math).random()}",
				new TemplateParserContext()).getValue(String.class);

          // evaluates to "random number is 0.7038186818312008"
	}


	class Foo{
		public String acceptConstant(String unUse){
			return "hello world";
		}
	}


}
