import ProductCard from "./ProductCard"
import "./ProductList.css"

function ProductList({ products,inventory }) {
  return (
    <div className="product-list">
      {products.map((product) => (
        <ProductCard key={product.id} product={product} inventory={inventory[product.id]}/>
      ))}
    </div>
  )
}

export default ProductList
