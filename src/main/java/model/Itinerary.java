package model;

public class Itinerary {
    private int id;
    private int tourId;
    private int dayNumber;
    private String description;

    public Itinerary() {
    }

    public Itinerary(int id, int tourId, int dayNumber, String description) {
        this.id = id;
        this.tourId = tourId;
        this.dayNumber = dayNumber;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTourId() {
        return tourId;
    }

    public void setTourId(int tourId) {
        this.tourId = tourId;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Itinerary{" +
                "id=" + id +
                ", tourId=" + tourId +
                ", dayNumber=" + dayNumber +
                ", description='" + description + '\'' +
                '}';
    }
}
