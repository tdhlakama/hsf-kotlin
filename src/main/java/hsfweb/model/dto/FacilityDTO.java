package hsfweb.model.dto;

import hsfweb.controller.endpoints.BaseModel;

/**
 * Created by tdhla on 10/4/2017.
 */
public class FacilityDTO extends BaseModel implements Comparable<FacilityDTO> {

    public static final Double KILOMETRE_IN_METERS = 1000.0;

    private String facilityType;
    private String latitude;
    private String longitude;
    private Double distanceFromLocation;
    private String distanceDescriptionFromLocation;
    private String contactDetail;
    private String addressDetail;

    public String getFacilityType() {
        return facilityType;
    }

    public void setFacilityType(String facilityType) {
        this.facilityType = facilityType;
    }


    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Double getDistanceFromLocation() {
        return distanceFromLocation;
    }

    public void setDistanceFromLocation(Double distanceFromLocation) {
        this.distanceFromLocation = distanceFromLocation;
    }

    public String getDistanceDescriptionFromLocation() {
        return distanceDescriptionFromLocation;
    }

    public void setDistanceDescriptionFromLocation(String distanceDescriptionFromLocation) {
        this.distanceDescriptionFromLocation = distanceDescriptionFromLocation;
    }

    public void assignDistanceDescription() {

        Double distanceFromCurrentLocation = getDistanceFromLocation();

        if (distanceFromCurrentLocation == null) {
            setDistanceDescriptionFromLocation("Location not specified");
        }

        final String distanceUnit;
        final String distanceDescription;

        if (distanceFromCurrentLocation >= KILOMETRE_IN_METERS) {

            distanceUnit = "Km from current location";
            distanceDescription = DistanceUtil.formatNumber(distanceFromCurrentLocation / 1000) + " " + distanceUnit;
        } else {
            distanceUnit = "m from current location";
            distanceDescription = DistanceUtil.formatNumber(distanceFromCurrentLocation) + " " + distanceUnit;
        }
        setDistanceDescriptionFromLocation(distanceDescription);
    }


    @Override
    public int compareTo(FacilityDTO o) {
        if (this == o) return 0;
        if (o == null) return 1;

        if (getDistanceFromLocation() != null && o.getDistanceFromLocation() != null) {

            return getDistanceFromLocation().compareTo(o.getDistanceFromLocation());

        } else if (getDistanceFromLocation() != null && o.getDistanceFromLocation() == null) {

            return -1;

        } else if (getDistanceFromLocation() == null && o.getDistanceFromLocation() != null) {

            return 1;

        } else {

            return getName().compareToIgnoreCase(o.getName());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FacilityDTO facility = (FacilityDTO) o;

        if (getId() != null ? !getId().equals(facility.getId()) : facility.getId() != null) return false;
        return getName() != null ? getName().equalsIgnoreCase(facility.getName()) : facility.getName() == null;

    }

    public boolean getHasCoordinate() {
        if (latitude == null && longitude == null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        return result;
    }

    public String getContactDetail() {
        return contactDetail;
    }

    public void setContactDetail(String contactDetail) {
        this.contactDetail = contactDetail;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }
}
