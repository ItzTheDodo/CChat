package me.ItzTheDodo.CChat.ChatRooms;

import java.util.HashMap;

import me.ItzTheDodo.CChat.ChatRooms.utils.RankLevel;

public class ChatSettings {
	
	private String prefix;
	private HashMap<String, RankLevel> ranks;
	private HashMap<String, String> rank_colours;
	private boolean nsfw;
	
	public ChatSettings(String pre, HashMap<String, RankLevel> rank, HashMap<String, String> rank_colour, boolean not_safe_for_work) {
		prefix = pre;
		ranks = rank;
		rank_colours = rank_colour;
		nsfw = not_safe_for_work;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public HashMap<String, RankLevel> getRanks() {
		return ranks;
	}

	public void setRanks(HashMap<String, RankLevel> ranks) {
		this.ranks = ranks;
	}

	public HashMap<String, String> getRank_colours() {
		return rank_colours;
	}

	public void setRank_colours(HashMap<String, String> rank_colours) {
		this.rank_colours = rank_colours;
	}

	public boolean isNSFW() {
		return nsfw;
	}

	public void setNSFW(boolean nsfw) {
		this.nsfw = nsfw;
	}

}
