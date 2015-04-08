package com.intern.utils;

/**
 * 元组 让一个方法返回多个参数
 * 
 * @author bird Apr 8, 2015 3:48:12 PM
 */
public class TwoTuple<A, B> {

	public final A first;
	public final B second;

	public TwoTuple(A a, B b) {
		first = a;
		second = b;
	}
}
