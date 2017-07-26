package com.penn.test;

import org.junit.Test;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.expression.ParserContext.TEMPLATE_EXPRESSION;

/**
 * @Description : 文档上所测试类
 * @Project : spring-framework
 * @Program Name  : com.penn.test.SpelTest
 * @Author : zhongpingtang@yiruntz.com 唐忠平
 */
public class SpelTest {

	public ExpressionParser parser = new SpelExpressionParser();
	;

	/***
	 * 测试SpelExpressionParser
	 */
	@Test
	public void test1() {
		ExpressionParser parser = new SpelExpressionParser();
		Expression expression = parser.parseExpression("'hello world'.concat('!')");
		String value = expression.getValue(String.class);
		System.out.println(value);
		System.out.println(expression.getExpressionString());


	}


	/**
	 * 解析自定义的ParseContent
	 */
	@Test
	public void test2() {

		Expression expression = parser.parseExpression("hello,#{5+9}", TEMPLATE_EXPRESSION);
		System.out.println(expression.getValue());
	}


	@Test
	public void test3() {
		class Simple {
			public List<Boolean> booleanList = new ArrayList<Boolean>();
		}

		Simple simple = new Simple();

		simple.booleanList.add(true);

		StandardEvaluationContext simpleContext = new StandardEvaluationContext(simple);

		// false is passed in here as a string. SpEL and the conversion service will
		// correctly recognize that it needs to be a Boolean and convert it
		parser.parseExpression("booleanList[0]").setValue(simpleContext, "false");

		// b will be false
		Boolean b = simple.booleanList.get(0);
		System.out.println(b);
	}

	@Test
	public void test4() {
		class Simple {
			public List<Boolean> booleanList = new ArrayList<Boolean>();
		}

		Simple simple = new Simple();

		simple.booleanList.add(false);
		StandardEvaluationContext simpleClassContext = new StandardEvaluationContext(simple);
		System.out.println(parser.parseExpression("booleanList[0]").getValue(simpleClassContext));


		System.out.println("resolvers:" + simpleClassContext.getConstructorResolvers());

	}


	@Test
	public void test5() {
		class Demo {
			public List<String> list;
		}

		// Turn on:
		// - auto null reference initialization
		// - auto collection growing
		SpelParserConfiguration config = new SpelParserConfiguration(true, true);

		ExpressionParser parser = new SpelExpressionParser(config);

		Expression expression = parser.parseExpression("list[3]");

		Demo demo = new Demo();

		Object o = expression.getValue(demo);

		// demo.list will now be a real collection of 4 entries
		// Each entry is a new empty String
		System.out.println(demo.list.size());
	}





}
