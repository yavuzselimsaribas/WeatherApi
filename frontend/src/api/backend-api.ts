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

export interface CityCategories {
    co: string;
    so2: string;
    o3: string;
}

export interface CityResults {
    date: string;
    cityCategories: CityCategories;
}

export interface City {
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
    totalCount: number;
}



export default {
    getCoordinates(cityName: string): Promise<AxiosResponse<Coordinate>> {
        return axiosInstance.get(`coordinates/${cityName}`);
    },
    fetchGeoCoordinates(cityName: string): Promise<AxiosResponse<Boolean>> {
        return axiosInstance.get(`/coordinates/queue/${cityName}`);
    },
    // Add the new post method here
    createHistoricalCityAirDataRequest(
        cityName: string,
        startDate?: string,
        endDate?: string,
        page?: number,      // Add page parameter for pagination
        pageSize?: number, // Add pageSize parameter for pagination
        sortDirection?: string,
    ): Promise<AxiosResponse<CityResponse>> {
        // Create the query string for the request, including the page and pageSize parameters
        let queryString = `/cities/${cityName}?startDate=${startDate}&endDate=${endDate}`;
        if (page !== undefined) {
            queryString += `&page=${page-1}`;
        }
        if (pageSize !== undefined) {
            queryString += `&size=${pageSize}`;
        }
        if (sortDirection !== undefined) {
            queryString += `&sortDirection=${sortDirection}`;
        }
        return axiosInstance.get<CityResponse>(queryString);
    }


}