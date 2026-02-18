import Navbar from "../components/layout/Navbar"
import ProductList from "../components/product/ProductList"
import Footer from "../components/layout/Footer"

function HomePage() {

  const products = [
    {
      id: 1,
      name: "Wireless Headphones",
      description: "High quality noise cancelling headphones",
      price: 2999,
      imageUrl: "https://sony.scene7.com/is/image/sonyglobalsolutions/WH1000XM6_Primary_image_Midnight_Blue?$primaryshotPreset$&fmt=png-alpha",
      inStock: true
    },
    {
      id: 2,
      name: "Smart Watch",
      description: "Track your fitness and notifications",
      price: 4999,
      imageUrl: "https://images.unsplash.com/photo-1516570161787-2fd917215a3d",
      inStock: false
    },
    {
      id: 3,
      name: "Gaming Mouse",
      description: "Ergonomic design with RGB lights",
      price: 1499,
      imageUrl: "https://m.media-amazon.com/images/I/61AcT0ZuO3L.jpg",
      inStock: true
    },
      {
      id: 4,
      name: "Wireless Headphones",
      description: "High quality noise cancelling headphones",
      price: 2999,
      imageUrl: "https://sony.scene7.com/is/image/sonyglobalsolutions/WH1000XM6_Primary_image_Midnight_Blue?$primaryshotPreset$&fmt=png-alpha",
      inStock: true
    },
    {
      id: 5,
      name: "Smart Watch",
      description: "Track your fitness and notifications",
      price: 4999,
      imageUrl: "https://images.unsplash.com/photo-1516570161787-2fd917215a3d",
      inStock: false
    },
    {
      id: 6,
      name: "Gaming Mouse",
      description: "Ergonomic design with RGB lights",
      price: 1499,
      imageUrl: "https://m.media-amazon.com/images/I/61AcT0ZuO3L.jpg",
      inStock: true
    },
      {
      id: 7,
      name: "Wireless Headphones",
      description: "High quality noise cancelling headphones",
      price: 2999,
      imageUrl: "https://sony.scene7.com/is/image/sonyglobalsolutions/WH1000XM6_Primary_image_Midnight_Blue?$primaryshotPreset$&fmt=png-alpha",
      inStock: true
    },
    {
      id: 8,
      name: "Smart Watch",
      description: "Track your fitness and notifications",
      price: 4999,
      imageUrl: "https://images.unsplash.com/photo-1516570161787-2fd917215a3d",
      inStock: false
    },
    {
      id: 9,
      name: "Gaming Mouse",
      description: "Ergonomic design with RGB lights",
      price: 1499,
      imageUrl: "https://m.media-amazon.com/images/I/61AcT0ZuO3L.jpg",
      inStock: true
    },
      {
      id: 10,
      name: "Wireless Headphones",
      description: "High quality noise cancelling headphones",
      price: 2999,
      imageUrl: "https://sony.scene7.com/is/image/sonyglobalsolutions/WH1000XM6_Primary_image_Midnight_Blue?$primaryshotPreset$&fmt=png-alpha",
      inStock: true
    },
    {
      id: 11,
      name: "Smart Watch",
      description: "Track your fitness and notifications",
      price: 4999,
      imageUrl: "https://images.unsplash.com/photo-1516570161787-2fd917215a3d",
      inStock: false
    },
    {
      id: 12,
      name: "Gaming Mouse",
      description: "Ergonomic design with RGB lights",
      price: 1499,
      imageUrl: "https://m.media-amazon.com/images/I/61AcT0ZuO3L.jpg",
      inStock: true
    }
  ]

  return (
    <>
      <Navbar />
      <ProductList products={products} />
            <Footer />

    </>
  )
}

export default HomePage
