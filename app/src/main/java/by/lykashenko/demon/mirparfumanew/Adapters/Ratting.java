package by.lykashenko.demon.mirparfumanew.Adapters;

import com.google.gson.annotations.SerializedName;

/**
 * Created by demon on 24.05.2017.
 */

public class Ratting {

	@SerializedName("thread")
	private String thread;

	@SerializedName("rating")
	private Integer rating;

	public String getThread() {
		return thread;
	}

	public void setThread(String thread) {
		this.thread = thread;
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}
}
