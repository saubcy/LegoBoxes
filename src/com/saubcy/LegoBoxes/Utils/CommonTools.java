package com.saubcy.LegoBoxes.Utils;

public class CommonTools {
	public static int getRandom(int min, int max) {
		return (int)((Math.random()*1000)%max + min);
	}
}
