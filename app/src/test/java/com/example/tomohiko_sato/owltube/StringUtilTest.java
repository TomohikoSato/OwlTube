package com.example.tomohiko_sato.owltube;

import com.example.tomohiko_sato.owltube.presentation.util.StringUtil;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class StringUtilTest {
	@Test
	public void 正確な視聴回数から表示用に丸めた視聴回数へ変換できる() {
		assertEquals("1.2万", StringUtil.convertDisplayViewCount("12345"));
		assertEquals("3千", StringUtil.convertDisplayViewCount("3969"));
		assertEquals("800", StringUtil.convertDisplayViewCount("833"));
		assertEquals("60", StringUtil.convertDisplayViewCount("62"));
		assertEquals("3", StringUtil.convertDisplayViewCount("3"));
		assertEquals("0", StringUtil.convertDisplayViewCount(""));
		assertEquals("0", StringUtil.convertDisplayViewCount(null));
	}
}
