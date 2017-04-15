package com.ctt.dataclasses;

import java.io.Serializable;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import lombok.Getter;
import lombok.Setter;
@Entity
@Index
public class LocationEntity implements Serializable{
		@Id
		@Getter@Setter
		private String userName;
		@Getter@Setter
		private String placeName;
		@Getter@Setter
		private double longitude;
		@Getter@Setter
		private double latitude;   
		public double distanceTo(LocationEntity that) {
	        double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;
	        double lat1 = Math.toRadians(this.latitude);
	        double lon1 = Math.toRadians(this.longitude);
	        double lat2 = Math.toRadians(that.latitude);
	        double lon2 = Math.toRadians(that.longitude);

	        // great circle distance in radians, using law of cosines formula
	        double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
	                               + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

	        // each degree on a great circle of Earth is 60 nautical miles
	        double nauticalMiles = 60 * Math.toDegrees(angle);
	        double statuteMiles = STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
	        return statuteMiles;
	    }

	    // return string representation of this point
	    public String toString() {
	        return placeName + " (" + latitude + ", " + longitude + ")";
	    }

	  	 
}
