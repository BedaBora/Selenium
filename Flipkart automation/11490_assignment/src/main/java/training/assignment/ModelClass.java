package training.assignment;

public class ModelClass {

	private String modelName;
	private float rating;
	private int reviewsCount;
	private int ratingCount;

	//--------------Getter-----------------//
	public String getModelName() {
		return modelName;
	}
	
	public float getRating() {
		return rating;
	}
	
	public int getReviewsCount() {
		return reviewsCount;
	}
	
	public int getRatingCount() {
		return ratingCount;
	}
	
	
	//----------------Setter-------------------//
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	
	public void setRating(float rating) {
		this.rating = rating;
	}
	
	public void setReviewsCount(int reviewsCount) {
		this.reviewsCount = reviewsCount;
	}
	
	public void setRatingCount(int ratingCount) {
		this.ratingCount = ratingCount;
	}
	
	//-------------------Print objects-------------//
	
	@Override
	public String toString() {
		return "ModelClass [modelName=" + modelName + ", rating=" + rating + ", reviewsCount=" + reviewsCount
				+ ", ratingCount=" + ratingCount + "]";
	}

}

