<template>
  <div id="app">
    <h1>Historical Air Quality Data</h1>
    <h2>Enter a city name and a date range to get the air quality data for that city</h2>
    <a-input v-model:value = "cityName" placeholder="Enter a city name" />
    <h3>Cities are restricted to London, Barcelona, Ankara, Tokyo and Mumbai</h3>
    <div class="date-range">
      <a-input v-model:value="startDate" type="date" aria-label="Start Date" />
      <a-input v-model:value="endDate" type="date" aria-label="End Date" />
    </div>
    <h4>Dates are restricted from 27-11-2023 to Today's date</h4>
    <button @click="getAirQualityData">Get Air Quality Data</button>
    <a-table
        :columns="columns"
        :data-source="dataSource"
        :loading="loading"
        :pagination="state.pagination"
        @change = "handleTableChange"
    >
        <template #bodyCell="{ text, record }">
        <span :style="getCellStyle(record, text)">{{ text }}</span>
      </template>
    </a-table>
  </div>
</template>

<script lang="ts">
import backendApi, {City, CityResponse, Status} from "./api/backend-api";
import {defineComponent, ref} from "vue";


const columns = [
  {
    title: "City Name",
    dataIndex: "cityName",
    key: "cityName",
  },
  {
    title: "Date",
    dataIndex: ["cityResults", "date"],
    key: "date",
    slots: { customRender: "date" },
  },
  {
    title: "CO",
    dataIndex: ["cityResults", "cityCategories", "co"],
    key: "co"
  },
  {
    title: "O3",
    dataIndex: ["cityResults", "cityCategories", "o3"],
    key: "o3"
  },
  {
    title: "SO2",
    dataIndex: ["cityResults", "cityCategories", "so2"],
    key: "so2"
  },
];

export default defineComponent({
  name: "App",
  setup() {

    const cityName = ref("");
    const startDate = ref("");
    const endDate = ref("");
    const currentPage = ref(1);
    const pageSize = ref(10);
    const loading = ref(false);
    const state = ref({
      data: [] as City[],
      pagination: {
        current: currentPage.value,
        pageSize: pageSize.value,
        total: endDate.value- startDate.value,
        showSizeChanger: true,
        showQuickJumper: true,
        showTotal: (total: number) => `Total ${total} items`,
      },
    });

    const handleTableChange = (pagination: any) => {
      currentPage.value = pagination.current;
      pageSize.value = pagination.pageSize;
      state.value.pagination.current = pagination.current;
      state.value.pagination.pageSize = pagination.pageSize;
      getAirQualityData();
    };

    const getCellStyle = (record: City, text: string) => {
      if (record.cityResults.cityCategories.co === text) {
        return { color: "red" };
      }
      if (record.cityResults.cityCategories.o3 === text) {
        return { color: "blue" };
      }
      if (record.cityResults.cityCategories.so2 === text) {
        return { color: "green" };
      }
      return {};
    };

    const dataSource = ref<City[]>([]); // Use ref for reactive dataSource
    const getAirQualityData = async () => {
      loading.value = true;
      const maxRetries = 5;
      let retries = 0;
      let response: CityResponse | null = null;
      while (
          (!response || response.status === Status.PENDING) &&
          retries < maxRetries
          ) {
        response = (await backendApi.createHistoricalCityAirDataRequest(
            cityName.value,
            startDate.value,
            endDate.value,
            currentPage.value, // Send the current page number to the backend
            pageSize.value // Send the page size to the backend
        )).data as CityResponse;
        if (!response.cities) {
          retries++;
          console.log(`Retrying ${retries} time`);
          await new Promise((resolve) => setTimeout(resolve, 2000));
        }
      }
      if (response && response.cities) {
        // If we have a valid response with cities array, update the data
        dataSource.value = response.cities;
        state.value.pagination.total = response.totalCount;
        console.log(response.cities);
      }
      loading.value = false;
    };
    return {
      columns,
      cityName,
      startDate,
      endDate,
      loading,
      getAirQualityData,
      getCellStyle,
      dataSource,
      state,
      handleTableChange
    };
  }
});
</script>

<style lang="scss">
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  background-color: aliceblue;
  flex-direction: column;
  display: flex;
  justify-content: center;
  align-items: center;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}

.date-range {
  display: flex;
  width: 100%;
  flex-direction: row;
  justify-content: space-evenly;
  align-items: center;
}
</style>
