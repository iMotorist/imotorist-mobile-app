package com.madushanka.imotorist.entities;

import java.util.List;

/**
 * Created by madushanka on 10/21/17.
 */

public class Ticket {

    String id,
            total_amount,
            motorist_name,
            motorist_address,
            vehicle_no,
            motorist_nic,
            offence_datetime,
            location,
            lat,
            lng,
            station,
            officer_name,
            officer_badge_no,
            valid_from,
            valid_to,
            court_name,
            ourt_date,
            remarks,
            license_no,
            ticket_no;

    List<Offence> offences;
    List<String> motorist_vehicle_classes;
    boolean paid;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public String getMotorist_name() {
        return motorist_name;
    }

    public void setMotorist_name(String motorist_name) {
        this.motorist_name = motorist_name;
    }

    public String getMotorist_address() {
        return motorist_address;
    }

    public void setMotorist_address(String motorist_address) {
        this.motorist_address = motorist_address;
    }

    public String getVehicle_no() {
        return vehicle_no;
    }

    public void setVehicle_no(String vehicle_no) {
        this.vehicle_no = vehicle_no;
    }

    public String getMotorist_nic() {
        return motorist_nic;
    }

    public void setMotorist_nic(String motorist_nic) {
        this.motorist_nic = motorist_nic;
    }

    public String getOffence_datetime() {
        return offence_datetime;
    }

    public void setOffence_datetime(String offence_datetime) {
        this.offence_datetime = offence_datetime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getOfficer_name() {
        return officer_name;
    }

    public void setOfficer_name(String officer_name) {
        this.officer_name = officer_name;
    }

    public String getOfficer_badge_no() {
        return officer_badge_no;
    }

    public void setOfficer_badge_no(String officer_badge_no) {
        this.officer_badge_no = officer_badge_no;
    }

    public String getValid_from() {
        return valid_from;
    }

    public void setValid_from(String valid_from) {
        this.valid_from = valid_from;
    }

    public String getValid_to() {
        return valid_to;
    }

    public void setValid_to(String valid_to) {
        this.valid_to = valid_to;
    }

    public String getCourt_name() {
        return court_name;
    }

    public void setCourt_name(String court_name) {
        this.court_name = court_name;
    }

    public String getOurt_date() {
        return ourt_date;
    }

    public void setOurt_date(String ourt_date) {
        this.ourt_date = ourt_date;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<Offence> getOffences() {
        return offences;
    }

    public void setOffences(List<Offence> offences) {
        this.offences = offences;
    }

    public List<String> getMotorist_vehicle_classes() {
        return motorist_vehicle_classes;
    }

    public void setMotorist_vehicle_classes(List<String> motorist_vehicle_classes) {
        this.motorist_vehicle_classes = motorist_vehicle_classes;
    }

    public String getVlicense_no() {
        return license_no;
    }

    public void setVlicense_no(String vlicense_no) {
        this.license_no = vlicense_no;
    }

    public String getTicket_no() {
        return ticket_no;
    }

    public void setTicket_no(String ticket_no) {
        this.ticket_no = ticket_no;
    }
}

