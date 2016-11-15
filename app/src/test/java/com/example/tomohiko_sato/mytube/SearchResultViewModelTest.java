package com.example.tomohiko_sato.mytube;

import com.example.tomohiko_sato.mytube.presentation.SearchActivity;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class SearchResultViewModelTest {

	@Test
	public void viewCountの数字が正しく表示される() {
		final SearchActivity.SearchResultViewModel vm = new SearchActivity.SearchResultViewModel(null, null, null, null);

		assertEquals("1.2万", vm.convertDisplayViewCount("12345"));
		assertEquals("3千", vm.convertDisplayViewCount("3969"));
		assertEquals("800", vm.convertDisplayViewCount("833"));
		assertEquals("60", vm.convertDisplayViewCount("62"));
		assertEquals("3", vm.convertDisplayViewCount("3"));
		assertEquals("0", vm.convertDisplayViewCount(""));
		assertEquals("0", vm.convertDisplayViewCount(null));
	}
}
