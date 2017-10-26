package com.madushanka.imotorist;

import com.madushanka.imotorist.entities.Offence;
import com.madushanka.imotorist.entities.Ticket;

/**
 * Created by madushanka on 10/22/17.
 */

public class TicketCreater {

    Ticket t;

    public Ticket getT() {
        return t;
    }

    public void setT(Ticket t) {
        this.t = t;
    }

    public String getOffenceList(Ticket t) {

        String s = "";

        for (Offence f : t.getOffences()) {

            String temp = "<tr><td>" + f.getDescription() + "</td> <td class=\"text-right\">" + f.getFine() + "</td></tr>\n";
            s = s + temp;
        }
        return s;
    }

    public String getRemark(Ticket t) {
        String s = " ";

        if (t.getRemarks() == null) {
            s = "";
        } else {
            s = "<div class=\"col-sm-12\"><label>Remarks:</label> <p>" + t.getRemarks() + "</p></div>\n";
        }

        return s;

    }


    public String getTicketData(Ticket t) {

        String veicle_classes = "";

        for (String c : t.getMotorist_vehicle_classes()) {
            veicle_classes = veicle_classes + c + ", ";
        }

        String s;

        s = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "    <head>\n" +
                "        <meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">\n" +
                "        <meta charset=\"utf-8\">\n" +
                "        <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "        <link media=\"all\" type=\"text/css\" rel=\"stylesheet\" href=\"file:///android_asset/iMotorist_files/frontend.css\">\n" +
                "    </head>\n" +
                "    <body id=\"app-layout\" class=\"frontend-ticket-post\">\n" +
                "    <div id=\"app\">\n" +
                "        <div id=\"results\">\n" +
                "                <div class=\"panel panel-minimal panel-default print-visible mb-0\">\n" +
                "                    <div class=\"panel-heading\">\n" +
                "                       <div class=\"col-xs-12 text-center mb-15 mt-15\"><img src=\"file:///android_asset/iMotorist_files/imotorist-logo.svg\" align=\"center\" alt=\"iMotorist Logo\"></div>\n" +
                "                       <div class=\"pl-15 pr-15\">\n" +
                "                           <div class=\"row mt-22 text-center\">\n" +
                "                               <div class=\"col-xs-12\">\n" +
                "                                   <h3 class=\"mt-0\"><strong>Spot Fine Ticket</strong></h3>\n" +
                "                               </div>\n" +
                "                               <div class=\"col-xs-12\">\n" +
                "                                   <h3 class=\"mt-0\">" + t.getTicket_no() + "</h3>\n" +
                "                               </div>\n" +
                "                               <div class=\"col-xs-12\">\n" +
                "                                   <h3 class=\"mt-0\">Rs. " + t.getTotal_amount() + "</h3>\n" +
                "                               </div>\n" +
                "                           </div>\n" +
                "                       </div>\n" +
                "                    </div><div class=\"panel-body\"><div class=\"col-lg-12\">\n" +

                "                    <div class=\"row\"><div class=\"col-xs-12\"><label>Full Name of the Driver:</label>\n" +
                "                        <p>" + t.getMotorist_name() + "</p></div> <div class=\"col-xs-12\"><label>Address of the Driver:</label>\n" +
                "                        <p>" + t.getMotorist_address() + "</p></div></div>\n" +
                "                    <div class=\"row\"><div class=\"col-xs-6\"><label>Vehicle No:</label> <p>" + t.getVehicle_no() + "</p></div>\n" +
                "                        <div class=\"col-xs-6\"><label>D/L No:</label> <p>" + t.getVlicense_no() + "</p></div>\n" +
                "                        <div class=\"col-xs-6\"><label>NIC No:</label> <p>" + t.getMotorist_nic() + "</p></div>\n" +
                "                        <div class=\"col-xs-6\"><label>Competent to Drive:</label> <p>\n" + veicle_classes +
                "                                                        </p></div></div>\n" +
                "                    <hr class=\"mt-10\"> <div class=\"row\"><div class=\"col-xs-12\"><label>Date and Time of Offence:</label>\n" +
                "                    <p>" + t.getOffence_datetime() + "</p></div> <div class=\"col-xs-12\"><label>Place:</label>\n" +
                "                    <p>" + t.getLocation() + " (" + t.getLat() + "," + t.getLng() + ")</p></div></div>\n" +
                "                    <div class=\"row\"><div class=\"col-xs-12\"><label>Police Station:</label> <p>" + t.getStation() + "</p></div>\n" +
                "                        <div class=\"col-xs-12\"><label>Issuing Officer:</label>\n" +
                "                            <p>" + t.getOfficer_name() + " (" + t.getOfficer_badge_no() + ")</p></div></div> <div class=\"row\">\n" +
                "                    <div class=\"col-xs-6\"><label>Valid From:</label> <p>" + t.getValid_from() + "</p></div> <div class=\"col-xs-6\">\n" +
                "                    <label>Valid To:</label> <p>" + t.getValid_to() + "</p></div> <div class=\"col-xs-6\"><label>Court:</label>\n" +
                "                    <p>" + t.getStation() + "</p></div> <div class=\"col-xs-6\"><label>Court Date:</label> <p>" + t.getCourt_name() + "</p></div></div>\n<div class=\"row mb-15\">" + getRemark(t) +
                "                    " +
                "                    </div></div><div class=\"col-xs-12\"><table class=\"table table-condensed table-hover\"><thead><tr><td><strong>Offence</strong></td> <td class=\"text-right\"><strong>Fine (Rs.)</strong></td></tr></thead> <tbody>\n" +
                getOffenceList(t) +
                "                    <tr class=\"fs-115\"><td class=\"text-right\"><strong>Subtotal</strong></td> <td class=\"text-right\">" + t.getTotal_amount() + "</td></tr></tbody></table></div></div></div> <div id=\"googleMap\" tabindex=\"-1\" role=\"dialog\" aria-hidden=\"true\" class=\"modal fade\"><div class=\"modal-dialog\"><div class=\"modal-content\"><div class=\"modal-header\"><button type=\"button\" data-dismiss=\"modal\" aria-label=\"Close\" class=\"close\"><span aria-hidden=\"true\">×</span></button></div> <div class=\"modal-body\"></div></div></div></div>\n" +
                "</div>\n" +
                "    </body></html>\n";


        return s;
    }
}