import axios from "axios"

export const getInventory = (productId) =>{
    return axios.get(`http://localhost:8081/api/v1/inventories/product/${productId}`);
}