<template>
  <div id="app">
    <div class="header">
      <img alt="Vue logo" src="./assets/logo.svg" />
    </div>
    <h1>Historical Air Quality Data</h1>
    <h2>Enter a city name and a date range to get the air quality data for that city</h2>
    <input v-model="cityName" type="text" placeholder="Enter city name" />
    <h3>Cities are restricted to  London, Barcelona, Ankara, Tokyo and Mumbai </h3>
    <div class="date-range">
      <input v-model="startDate" type="date" aria-label="Start Date" />
    <input v-model="endDate" type="date" aria-label="End Date" />
    </div>
    <h4>Dates are restricted from 27-11-2023 to Today's date</h4>
    <button @click="getAirQualityData">Get Air Quality Data</button>
    --create an ant-vue table to display air quality data
    --what we do is, we call the createHistoricalCityAirDataRequest function from the backend-api.ts file by clicking the button
    --the response is stored in the airPollutionData variable
    --CityResponse is the interface that we created in the backend-api.ts file which include response Status, and cities array: City[]
    --City is the interface that we created in the backend-api.ts file which include cityName and cityResult: CityResult
    --CityResult is the interface that we created in the backend-api.ts file which include date and cityCategories: CityCategories
    --CityCategories is the interface that we created in the backend-api.ts file which include three string values: CO, SO2, O3
    -- in this table, we want to display the cityName, date, CO, SO2, O3 for each city in cities array
    -- also, if airPollutionData is null, we don't want to display the table
    <a-table :data-source="airPollutionData?.cities">
      <a-table-column title="City Name" dataIndex="cityName" key="cityName"></a-table-column>
      <a-table-column title="Date" dataIndex="cityResult.date" key="date"></a-table-column>
      <a-table-column title="CO" dataIndex="cityResult.cityCategories.CO" key="CO"></a-table-column>
      <a-table-column title="SO2" dataIndex="cityResult.cityCategories.SO2" key="SO2"></a-table-column>
      <a-table-column title="O3" dataIndex="cityResult.cityCategories.O3" key="O3"></a-table-column>
    </a-table>
  </div>
</template>

<script lang="ts">
import backendApi, {CityResponse} from "./api/backend-api";
import {defineComponent} from "vue";

export default defineComponent({
  name: "App",
  data: (): { airPollutionData: CityResponse | null; cityName: string; startDate: string; endDate: string } => ({
    airPollutionData: null,
    cityName: "",
    startDate: "", // Default to today's date
    endDate: ""    // Default to today's date
  }),
  methods: {
    async getAirQualityData() {
      const { cityName, startDate, endDate } = this;
      this.airPollutionData = await backendApi.createHistoricalCityAirDataRequest(cityName, startDate, endDate);
    },
  },
});
</script>

<style lang="scss">
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  background-color: aliceblue;
  flex-direction: column;
  display: flex;
  justify-content: center;
  align-items: center;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
date-range {
  display: flex;
  width: 100%;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
}
</style>
