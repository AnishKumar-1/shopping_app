import axios from "axios";

export const getProduct = ()=>{
    return axios.get("http://localhost:8080/api/v1/product");
}