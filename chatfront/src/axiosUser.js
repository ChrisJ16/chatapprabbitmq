import axios from "axios";

const commonHeaders = {
    "Content-Type": "application/json",
    "Access-Control-Allow-Origin": "*",
    "Access-Control": "*",
    "Access-Control-Allow-Headers": "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With",
};

const axiosInstanceUser = axios.create({
    baseURL: "http://localhost:8080",
    headers: commonHeaders,
});

export default axiosInstanceUser;
