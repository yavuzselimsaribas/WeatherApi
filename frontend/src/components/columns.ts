export const columns = [
    {
        title: "City Name",
        dataIndex: "cityName",
        key: "cityName"
    },
    {
        title: "Date",
        dataIndex: ["cityResults", "date"],
        key: "date",
        sorter: true
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
