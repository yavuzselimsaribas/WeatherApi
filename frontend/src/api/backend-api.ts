import axios, { AxiosResponse } from 'axios';

const axiosInstance = axios.create({
    baseURL: 'http://localhost:8080/',
    timeout: 10000,
    headers: {
        'Content-Type': 'application/json'
    }
});

interface Coordinate {
    cityName: string;
    lat: number;
    lon: number;
}

interface CityCategories {
    CO: string;
    SO2: string;
    O3: string;
}

interface CityResults {
    date: string;
    cityCategories: CityCategories;
}

interface City {
    cityName: string;
    cityResults: CityResults;
}

export enum Status {
    PENDING = "PENDING",
    READY = "READY",
    FAILED = "FAILED"
}

export interface CityResponse {
    status: Status;
    cities: City[];
}



export default {
    getCoordinates(cityName: string): Promise<AxiosResponse<Coordinate>> {
        return axiosInstance.get(`coordinates/${cityName}`);
    },
    fetchGeoCoordinates(cityName: string): Promise<AxiosResponse<Boolean>> {
        return axiosInstance.get(`/coordinates/queue/${cityName}`);
    },
    // Add the new post method here
    createHistoricalCityAirDataRequest(cityName: string, startDate?: string, endDate?: string): Promise<AxiosResponse<CityResponse>> {
        const requestData = { startDate, endDate };
        console.log("Requesting for city: " + cityName +"for dates: " + startDate + " and " + endDate);
        return axiosInstance.post<CityResponse>(`/cities/${cityName}?startDate=${startDate}&endDate=${endDate}`, );
    }


}