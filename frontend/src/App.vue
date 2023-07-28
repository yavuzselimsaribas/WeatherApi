<template>
  <div id="app">
    <h1 class="pa-header">Historical Air Quality Data</h1>
    <a-input v-model:value="cityName" class="city-input" placeholder="Enter a city name" />

    <div v-if="showCityNameError" class="error-message">
      <span class="error-icon">⚠️</span>
      <span class="error-text">Please enter a city name.</span>
      <span class="close-icon" @click="hideCityNameError">❌</span>
    </div>

    <div class="date-range">
      <div class="date-input-container">
        <a-space direction="vertical" :size="12">
          <a-range-picker v-model:value="value1" :disabled-date="disabledDate" :presets="rangePresets" @change="onRangeChange"/>
        </a-space>
      </div>
    </div>
    <button @click="getAirQualityData" class="btn-get-data" :disabled="loading" >Get Air Quality Data</button>

    <div v-if="!dataLoaded">
      <p>Your data will be visible after you give a command</p>
    </div>

    <a-table
        v-if="dataLoaded"
        class="result-table"
        :columns="columns"
        :data-source="dataSource"
        :loading="loading"
        :pagination="state.pagination"
        @change="handleTableChange"
    >
      <template #bodyCell="{ text, record }">
        <span :style="getCellStyle(record, text)">{{ text }}</span>
      </template>
    </a-table>
  </div>
</template>


<script lang="ts">
import backendApi, {City, CityResponse} from "./api/backend-api";
import {defineComponent,  ref} from "vue";
import dayjs, { Dayjs } from 'dayjs';
import {columns} from "@/components/columns";


export default defineComponent({
  name: "App",
  setup() {

    type RangeValue = [Dayjs, Dayjs];
    const value1 = ref<RangeValue>();
    const showCityNameError = ref(false);
    const dataLoaded = ref(false); // Add dataLoaded variable and set to false initially
    const cityName = ref("");
    const currentPage = ref(1);
    const pageSize = ref(10);
    const loading = ref(false);
    const sortOrder = ref("ASC");

    const state = ref({
      data: [] as City[],
      pagination: {
        current: currentPage.value,
        pageSize: pageSize.value,
        total: 0,
        showSizeChanger: true,
        showQuickJumper: true,
        showTotal: (total: number) => `Total ${total} items`,
      },
    });


    const disabledDate = (current: Dayjs) => {
      return current > dayjs().endOf('day') || current < dayjs('2021-11-01');
    };

    const handleTableChange = (pagination: any) => {
      currentPage.value = pagination.current;
      pageSize.value = pagination.pageSize;
      state.value.pagination.current = pagination.current;
      state.value.pagination.pageSize = pagination.pageSize;
      // if(sorter.order === "ascend") {
      //   sortOrder.value = "ASC";
      // } else if(sorter.order === "descend") {
      //   sortOrder.value = "DSC";
      // }
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
    const getAirQualityData = async  () => {
      if(cityName.value === "") {
        showCityNameError.value = true;
        return;
      }
      showCityNameError.value = false;
      loading.value = true;
      const maxRetries = 20;
      let retries = 0;
      let response: CityResponse | null = null;
      while (
          (!response || !response.cities) &&
          retries < maxRetries
          ) {
        try {
          response = (await backendApi.createHistoricalCityAirDataRequest(
              cityName.value,
              value1.value[0].format("YYYY-MM-DD"),
              value1.value[1].format("YYYY-MM-DD"),
              currentPage.value, // Send the current page number to the backend
              pageSize.value, // Send the page size to the backend
              sortOrder.value // Send the sort direction to the backend
          )).data as CityResponse;
        } catch (e) {
          loading.value = false;
          console.log(e);
        }
        if (!response.cities) {
          retries++;
          console.log(`Retrying ${retries} time`);
          await new Promise((resolve) => setTimeout(resolve, 500));
        }
      }
      if (response && response.cities) {
        dataSource.value = response.cities;
        state.value.pagination.total = calculateTotalCount(
            value1.value[0].format("YYYY-MM-DD"),
            value1.value[1].format("YYYY-MM-DD")
        );
        dataLoaded.value = true; // Set dataLoaded to true when data is loaded
      }
      loading.value = false;
    };

    const hideCityNameError = () => {
      // Function to hide the city name error message
      showCityNameError.value = false;
    };

    const calculateTotalCount = (startDate: string, endDate: string) => {
      const start = new Date(startDate);
      const end = new Date(endDate);
      const diffTime = Math.abs(end.getTime() - start.getTime());
      return Math.ceil(diffTime / (1000 * 60 * 60 * 24)) +1;
    };

    const onRangeChange = (dates: RangeValue, dateStrings: string[]) => {
      if (dates) {
        console.log('From: ', dates[0], ', to: ', dates[1]);
        console.log('From: ', dateStrings[0], ', to: ', dateStrings[1]);
      } else {
        console.log('Clear');
      }
    };

    const rangePresets = ref([
      { label: 'Last 7 Days', value: [dayjs().add(-7, 'd'), dayjs()] },
      { label: 'Last 14 Days', value: [dayjs().add(-14, 'd'), dayjs()] },
      { label: 'Last 30 Days', value: [dayjs().add(-30, 'd'), dayjs()] },
      { label: 'Last 90 Days', value: [dayjs().add(-90, 'd'), dayjs()] },
    ]);

    return {
      columns,
      cityName,
      loading,
      getAirQualityData,
      getCellStyle,
      dataSource,
      state,
      handleTableChange,
      dataLoaded,// Return dataLoaded
      value1,
      disabledDate,
      showCityNameError,
      hideCityNameError,
      rangePresets,
      onRangeChange,
    };
  }
});
</script>

<style lang="scss">
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  text-align: center;
  margin-top: 60px;
}

.pa-header {
  font-size: 2.5rem;
  font-weight: bold;
  margin-bottom: 1rem;
}

.container {
  margin-bottom: 1rem;
}

.city-input {
  margin-bottom: 1rem;
}

.date-range {
  display: flex;
  flex-direction: row;
  justify-content: space-around;
  align-items: center;
  width: 100%;
  margin-bottom: 1rem;
}

.date-input {
  width: 100%;
}

.date-info {
  font-size: 0.9rem;
  color: #888;
  margin-bottom: 1rem;
}

.date-input-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-top: 1rem;
}

.date-label {
  font-size: 0.9rem;
  color: #888;
}


.btn-get-data {
  background-color: #DFA878;
  color: white;
  font-size: 1rem;
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.btn-get-data:hover {
  background-color: #6C3428;
}

.result-table {
  width: 100%;
  margin-top: 2rem;
}

.result-table th {
  background-color: #f1f1f1;
  padding: 1rem;
  text-align: left;
}

.result-table td {
  padding: 1rem;
}

.result-table tr:nth-child(even) {
  background-color: #f9f9f9;
}

.result-table tr:hover {
  background-color: #e5e5e5;
}

.error-message {
  display: flex;
  align-items: center;
  color: red;
  margin-top: 0.5rem;
}

.error-icon {
  margin-right: 0.5rem;
}

.close-icon {
  margin-left: 0.5rem;
  cursor: pointer; /* Set the cursor to pointer (click icon) */
}

</style>