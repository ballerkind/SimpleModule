package de.ballerkind.simplemodule.core.module.modules.Minecraft.statistic;

import java.util.List;

public class MinecraftStatistic {

	private long start;
	private List<Integer> data;

	public MinecraftStatistic(long start, List<Integer> data) {
		this.start = start;
		this.data = data;
	}

	public long getStart() {
		return start;
	}

	public List<Integer> getData() {
		return data;
	}

}