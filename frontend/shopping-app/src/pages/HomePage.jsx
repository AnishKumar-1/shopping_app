import Navbar from "../components/layout/Navbar"
import ProductList from "../components/product/ProductList"
import Footer from "../components/layout/Footer"
import { useEffect, useState } from "react"
import { getProduct } from "../services/productService"
import { getInventory } from "../services/inventoryService"

function HomePage() {

  useEffect(()=>{
    fetchProduct()
  },[])

  const [product,setProduct] = useState([])
    const [inventory,setInventory] = useState({});


  const fetchProduct = async () =>{
    const res=await getProduct()
    setProduct(res.data)
    res.data.forEach(async (p)=> {
      const inRes= await getInventory(p.id);
      console.log("Inventory:", inRes.data);
      setInventory(prev=>({...prev,[p.id]: inRes.data}));
    });

  };



  return (
    <>
      <Navbar />
      <ProductList products={product} inventory={inventory} />
            <Footer />

    </>
  )
}

export default HomePage
