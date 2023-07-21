package com.example.web.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CityCategories {

    private String CO;
    private String SO2;
    private String O3;

    public CityCategories(double CO, double SO2, double O3) {
        if(0<= SO2 && SO2 < 20) {
            this.SO2 = "Good";
        } else if(20<= SO2 && SO2 < 80) {
            this.SO2 = "Fair";
        } else if(80<= SO2 && SO2 < 250) {
            this.SO2 = "Moderate";
        } else if(250<= SO2 && SO2 < 350) {
            this.SO2 = "Poor";
        } else if(350<= SO2) {
            this.SO2 = "Very Poor";
        }

        if(0<= O3 && O3 < 60) {
            this.O3 = "Good";
        } else if(60<= O3 && O3 < 100) {
            this.O3 = "Fair";
        } else if(100<= O3 && O3 < 140) {
            this.O3 = "Moderate";
        } else if(140<= O3 && O3 < 180) {
            this.O3 = "Poor";
        } else if(180<= O3) {
            this.O3 = "Very Poor";
        }

        if(0<= CO && CO < 4400) {
            this.CO = "Good";
        } else if(4400<= CO && CO < 9400) {
            this.CO = "Fair";
        } else if(9400<= CO && CO < 12400) {
            this.CO = "Moderate";
        } else if(12400<= CO && CO < 15400) {
            this.CO = "Poor";
        } else if(15400<= CO) {
            this.CO = "Very Poor";
        }

    }

}
